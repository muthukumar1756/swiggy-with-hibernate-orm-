package org.swiggy.user.view;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.swiggy.restaurant.model.Restaurant;

import org.swiggy.user.internal.controller.CartController;
import org.swiggy.user.model.Cart;
import org.swiggy.user.model.User;

import org.swiggy.common.inputhandler.InputHandler;
import org.swiggy.common.inputhandler.impl.InputHandlerImpl;

/**
 * <p>
 * Displays and updates the cart of the user
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
final class CartView {

    private static final Logger LOGGER = LogManager.getLogger(CartView.class);
    private static CartView cartView;
    private final InputHandler inputHandler;
    private final RestaurantDataView restaurantDataView;
    private final UserView userView;
    private final OrderView orderView;
    private final CartController cartController;

    private CartView() {
        inputHandler = InputHandlerImpl.getInstance();
        restaurantDataView = RestaurantDataView.getInstance();
        userView = UserView.getInstance();
        orderView = OrderView.getInstance();
        cartController = CartController.getInstance();
    }

    /**
     * <p>
     * Gets the object of the cart view class.
     * </p>
     *
     * @return The cart view object
     */
    public static CartView getInstance() {
        if (null == cartView) {
            cartView = new CartView();
        }

        return cartView;
    }

    /**
     * <p>
     * Adds the selected food to the user cart.
     * </p>
     *
     * @param cart Represents the cart of the user
     * @return True if the food is added to the user cart, false otherwise
     */
    public boolean addFoodToCart(final Cart cart) {
        return cartController.addFoodToCart(cart);
    }

    /**
     * <p>
     * Gets all the items in the user cart.
     * </p>
     *
     * @param userId Represents the id of the {@link User}
     * @return The cart list having list of cart items
     */
    public Optional<List<Cart>> getCartList(final long userId) {
        return cartController.getCart(userId);
    }

    /**
     * <p>
     * Displays all the items in the user cart.
     * </p>
     *
     * @param userId Represents the id of the {@link User}
     * @param restaurantId Represents the id of the {@link Restaurant}
     */
    public void displayCart(final long userId, final long restaurantId) {
        final Optional<List<Cart>> cart = getCartList(userId);
        float totalAmount = 0;

        LOGGER.info("""
                Items In Your Cart
                ID | Food Name | Quantity | Rate | Restaurant Name""");

        for(final Cart cartItem : cart.get()) {
            LOGGER.info(String.format("%d %s %d %.2f %s", cart.get().indexOf(cartItem) + 1, cartItem.getFoodName(),
                    cartItem.getQuantity(), cartItem.getAmount(), cartItem.getRestaurantName()));
            totalAmount += cartItem.getAmount();
        }
        LOGGER.info(String.format("Total Amount: RS %.2f \n", totalAmount));
        displayCartMenu(userId, restaurantId, cart.get());
    }

    /**
     * <p>
     * Handles the users choice to place order or remove food from the user cart.
     * </p>
     *
     * @param userId Represents the id of {@link User}
     * @param restaurantId Represents the id of the {@link Restaurant}
     * @param cart Represents the {@link Cart} of the user
     */
    public void displayCartMenu(final long userId, final long restaurantId, final List<Cart> cart) {
        LOGGER.info("""
                1.Place Order
                2.Remove Item From Cart
                3.Clear All Item From Cart
                4.Add More Food""");
        final int userChoice = inputHandler.getValue();

        if (-1 == userChoice) {
            restaurantDataView.addFoodOrPlaceOrder(userId, restaurantId);
        }

        switch (userChoice) {
            case 1 -> orderView.placeOrder(userId, restaurantId);
            case 2 -> removeFood(userId, restaurantId, cart);
            case 3 -> clearCart(userId);
            case 4 -> restaurantDataView.addFoodOrPlaceOrder(userId, restaurantId);
            default -> {
                LOGGER.warn("Enter A Valid Option");
                displayCartMenu(userId, restaurantId, cart);
            }
        }
    }

    /**
     * <p>
     * Gets the users choice to remove the food from the user cart.
     * </p>
     *
     * @param userId Represents the id of {@link User}
     * @param restaurantId Represents the id of the {@link Restaurant}
     * @param cart Represents the {@link Cart} list of the current user
     */
    private void removeFood(final long userId, final long restaurantId, final List<Cart> cart) {
        LOGGER.info("Enter The Item Number To Remove");
        final int itemNumber = inputHandler.getValue();

        if (-1 == itemNumber) {
            displayCartMenu(userId, restaurantId, cart);
        }
        final int selectedIndex = itemNumber - 1;

        if (selectedIndex >= 0 && selectedIndex < cart.size()) {
            final Cart cartItem = cart.get(selectedIndex);

            if (cartController.removeFood(cartItem.getId())) {
                    LOGGER.info("The Item Is Removed");
            }
        } else {
            LOGGER.warn("Enter The Valid Item Number");
        }
        displayCart(userId, restaurantId);
    }

    /**
     * <p>
     * Handles the users choice to display restaurants or logout.
     * </p>
     *
     * @param userId Represents the id of {@link User}
     */
    public void displayRestaurantsOrLogout(final long userId) {
        LOGGER.info("""
                1.Continue Food Ordering
                2.Logout""");
        final int value = inputHandler.getValue();

        if (-1 == value) {
            restaurantDataView.displayRestaurants(userId);
        }

        switch (value) {
            case 1 -> restaurantDataView.displayRestaurants(userId);
            case 2 -> {
                LOGGER.info("Your Account Is Logged Out");
                userView.displayMainMenu();
            }
            default -> {
                LOGGER.warn("Invalid Option");
                displayRestaurantsOrLogout(userId);
            }
        }
    }

    /**
     * <p>
     * Removes all the food from the user cart.
     * </p>
     *
     * @param userId Represents the id of the current {@link User}
     */
    public void clearCart(final long userId) {
        if (cartController.clearCart(userId)) {
            LOGGER.info("Your Cart Is Empty");
        }
        displayRestaurantsOrLogout(userId);
    }
}