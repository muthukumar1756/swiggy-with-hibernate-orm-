package org.swiggy.user.view;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.swiggy.user.internal.controller.CartController;
import org.swiggy.user.model.Cart;
import org.swiggy.user.model.User;

import org.swiggy.restaurant.model.Food;
import org.swiggy.restaurant.model.Restaurant;
import org.swiggy.restaurant.view.RestaurantView;

import org.swiggy.common.inputhandler.InputHandler;
import org.swiggy.common.inputhandler.impl.InputHandlerImpl;

/**
 * <p>
 * Displays the data from the restaurant
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
final class RestaurantDataView {

    private static final Logger LOGGER = LogManager.getLogger(RestaurantDataView.class);
    private static RestaurantDataView restaurantDataView;
    private final InputHandler inputHandler;
    private final UserView userView;
    private final RestaurantView restaurantView;
    private final CartView cartView;
    private final CartController cartController;

    private RestaurantDataView() {
        inputHandler = InputHandlerImpl.getInstance();
        restaurantView = RestaurantView.getInstance();
        userView = UserView.getInstance();
        cartView = CartView.getInstance();
        cartController = CartController.getInstance();
    }

    /**
     * <p>
     * Gets the object of the restaurant view class.
     * </p>
     *
     * @return The restaurant view object
     */
    public static RestaurantDataView getInstance() {
        if (null == restaurantDataView) {
            restaurantDataView = new RestaurantDataView();
        }

        return restaurantDataView;
    }

    /**
     * <p>
     * Displays the available restaurants.
     * </p>
     *
     * @param userId Represents the id of the current {@link User}
     */
    public void displayRestaurants(final long userId) {
        LOGGER.info("""
                    To Go Back Enter *
                    Available Restaurants In Your Area:""");
        final Optional<List<Restaurant>> restaurantList = restaurantView.getRestaurants();

        for (final Restaurant restaurant : restaurantList.get()) {
            LOGGER.info(String.format("%d %s", restaurantList.get().indexOf(restaurant) + 1, restaurant.getName()));
        }
        getRestaurant(userId, restaurantList.get());
    }

    /**
     * <p>
     * Gets the selection of a restaurant by the user.
     * </p>
     *
     * @param userId Represents the id of the current {@link User}
     */
    private void getRestaurant(final long userId, final List<Restaurant> restaurantList) {
        final int restaurantNumber = inputHandler.getValue();

        if (-1 == restaurantNumber) {
            userView.displayHomePageMenu(userId);
        }

        if (0 <= restaurantNumber && restaurantList.size() >= restaurantNumber) {
            final Restaurant restaurant = restaurantList.get(restaurantNumber - 1);

            if (null == restaurant) {
                LOGGER.warn("Select A Valid Restaurant Id");
                displayRestaurants(userId);
            }

            getMenucard(userId, restaurant.getId());
        } else {
            LOGGER.warn("Select The Valid Option");
            displayRestaurants(userId);
        }
    }

    /**
     * <p>
     * Gets the menucard according to the user chosen food category.
     * </p>
     *
     * @param userId Represents the id of the current {@link User}
     * @param restaurantId Represents the id of the current {@link Restaurant}
     */
    private void getMenucard(final long userId, final long restaurantId) {
        LOGGER.info("""
                    Select Food Type
                    1.VEG
                    2.NONVEG
                    3.VEG & NONVEG""");
        final int foodType = inputHandler.getValue();

        if (-1 == foodType) {
            displayRestaurants(userId);
        }

        if (0 < foodType && 4 > foodType) {
            final Optional<List<Food>> menucard = restaurantView.getMenucard(restaurantId, foodType);

            if (menucard.isPresent()) {
                LOGGER.warn("The Chosen Restaurant Currently Doesn't Have Any Available Items");
                displayRestaurants(userId);
            }
            displayFoods(menucard.get());
            selectFood(userId, restaurantId, menucard.get());
        } else {
            LOGGER.warn("Enter A Valid Option");
            getMenucard(userId, restaurantId);
        }
    }

    /**
     * <p>
     * Displays the menucard of the restaurant selected by the user.
     * </p>
     *
     * @param menucard Represents the menucard of the selected restaurant
     */
    public void displayFoods(final List<Food> menucard) {
        LOGGER.info("""
                    Available Items:
                    ID | Name | Rate | Category""");

        for (final Food food : menucard) {
            LOGGER.info(String.format("%d %s %.2f %s", menucard.indexOf(food) + 1,
                    food.getName(), food.getRate(), food.getType()));
        }
    }

    /**
     * <p>
     * Gets the selection of food by the user.
     * </p>
     *
     * @param userId Represents the id of the current {@link User}
     * @param restaurantId Represents the id of the current {@link Restaurant}
     * @param menucard Represents the menucard of the selected restaurant
     */
    private void selectFood(final long userId, final long restaurantId, final List<Food> menucard) {
        LOGGER.info("Enter FoodId To Add To Cart");
        final int selectedIndex = inputHandler.getValue();

        if (-1 == selectedIndex) {
            getMenucard(userId, restaurantId);
        }
        final int foodNumber = selectedIndex - 1;

        if (0 <= foodNumber && menucard.size() >= foodNumber) {
            final Food selectedFood = menucard.get(foodNumber);
            final int quantity = getQuantity(userId, selectedFood.getId());

            addFoodToCart(userId, restaurantId, selectedFood, quantity);
        } else {
            LOGGER.warn("Enter A Valid Option From The Menucard");
            selectFood(userId, restaurantId, menucard);
        }
        addFoodOrPlaceOrder(userId, restaurantId);
    }

    /**
     * <p>
     * Adds the selected food to the user cart.
     * </p>
     *
     * @param userId Represents the id of the current {@link User}
     * @param restaurantId Represents the id of the {@link Restaurant}
     * @param food Represents the {@link Food} selected by user
     * @param quantity Represents the quantity of selected food by the user
     */
    private void addFoodToCart(final long userId, final long restaurantId, final Food food, final int quantity) {
        final Cart cart = new Cart();

        cart.setUserId(userId);
        cart.setRestaurantId(restaurantId);
        cart.setFoodId(food.getId());
        cart.setQuantity(quantity);
        cart.setAmount(food.getRate() * quantity);

        if (!cartView.addFoodToCart(cart)) {
            handleFoodsFromVariousRestaurants(userId, restaurantId, food, quantity);
        }
    }

    /**
     * <p>
     * Handles the condition of user cart having foods from single restaurant.
     * </p>
     *
     * @param userId Represents the id of the current {@link User}
     * @param restaurantId Represents the id of the {@link Restaurant}
     * @param food Represents the {@link Food} selected by user
     * @param quantity Represents the quantity of selected food by the user
     */
    private void handleFoodsFromVariousRestaurants(final long userId, final long restaurantId, final Food food,
                                                   final int quantity) {
        LOGGER.warn("""
                    Your Cart Contains Items From Other Restaurant!.
                    Would You Like To Reset Your Cart For Adding Items From This Restaurant ?
                    1 To Reset Cart
                    2 To Cancel""");
        final int value = inputHandler.getValue();

        switch (value) {
            case 1 -> {
                cartController.clearCart(userId);
                addFoodToCart(userId, restaurantId, food, quantity);
            }
            case 2 -> LOGGER.info("Cancelled");
            default -> LOGGER.info("Enter a Valid Input");
        }
    }

    /**
     * <p>
     * Gets the quantity of selected food by the user.
     * </p>
     *
     * @param userId Represents the id of the current {@link User}
     * @param foodId Represents the id of the current {@link Food} selected by user
     */
    private int getQuantity(final long userId, final long foodId) {
        LOGGER.info("Enter The Quantity");
        final int quantity = inputHandler.getValue();

        if (-1 == quantity) {
            displayRestaurants(userId);
        }
        final Optional<Integer> foodQuantity = restaurantView.getFoodQuantity(foodId);
        final int availableQuantity = foodQuantity.get() - quantity;

        if (0 > availableQuantity) {
            LOGGER.info("The Entered Quantity Is Not Available");
            getQuantity(userId, foodId);
        }

        return quantity;
    }

    /**
     * <p>
     * Displays and handles the user choice to add extra foods or to place order.
     * </p>
     *
     * @param userId Represents the id of the current {@link User}
     * @param restaurantId Represents the id of the {@link Restaurant}
     */
    public void addFoodOrPlaceOrder(final long userId, final long restaurantId) {
        LOGGER.info("""
                    Do You Want To Add More Food
                    1.Add More Food
                    2.Place Order""");
        final int userChoice = inputHandler.getValue();

        if (-1 == userChoice) {
            getMenucard(userId, restaurantId);
        }

        switch (userChoice) {
            case 1 -> getMenucard(userId, restaurantId);
            case 2 -> cartView.displayCart(userId, restaurantId);
            default -> {
                LOGGER.warn("Enter A Valid Option");
                addFoodOrPlaceOrder(userId, restaurantId);
            }
        }
    }
}