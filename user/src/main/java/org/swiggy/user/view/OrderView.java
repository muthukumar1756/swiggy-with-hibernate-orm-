package org.swiggy.user.view;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.swiggy.user.internal.controller.OrderController;
import org.swiggy.user.model.Address;
import org.swiggy.user.model.Cart;
import org.swiggy.user.model.Order;
import org.swiggy.user.model.User;

import org.swiggy.restaurant.model.Restaurant;

import org.swiggy.common.inputhandler.InputHandler;
import org.swiggy.common.inputhandler.impl.InputHandlerImpl;

/**
 * <p>
 * Handles the food orders by the users
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
final class OrderView {

    private static final Logger LOGGER = LogManager.getLogger(OrderView.class);
    private static OrderView orderView;
    private final InputHandler inputHandler;
    private final UserView userView;
    private final CartView cartView;
    private final RestaurantDataView restaurantDataView;
    private final OrderController orderController;

    private OrderView() {
        inputHandler = InputHandlerImpl.getInstance();
        restaurantDataView = RestaurantDataView.getInstance();
        userView = UserView.getInstance();
        cartView = CartView.getInstance();
        orderController = OrderController.getInstance();
    }

    /**
     * <p>
     * Gets the object of the order view class.
     * </p>
     *
     * @return The order view object
     */
    public static OrderView getInstance() {
        if (null == orderView) {
            orderView = new OrderView();
        }

        return orderView;
    }

    /**
     * <p>
     * Places the order of the user selected items.
     * </p>
     *
     * @param userId Represents the id of the current {@link User}
     * @param restaurantId Represents the id of the {@link Restaurant} selected by the user
     */
    public void placeOrder(final long userId, final long restaurantId) {
        final Optional<List<Cart>> cartList = cartView.getCartList(userId);

        if (!cartList.isPresent()) {
            LOGGER.info("Select Address\n1.From Previous Oder\n2.New Address");
            long addressId = 0;
            final int value = inputHandler.getValue();

            if (-1 == value) {
                cartView.displayCartMenu(userId, restaurantId, cartList.get());
            }

            switch (value) {
                case 1 -> addressId = displayAddress(userId);
                case 2 -> addressId = getDeliveryAddress(userId);
                default -> LOGGER.info("Enter A Valid Input");
            }
            final List<Order> orderList = getOrders(userId, cartList.get(), addressId);

            if (orderController.placeOrder(orderList)) {
                LOGGER.info("\nYour Order Is Placed..\nWill Shortly Delivered To Your Address");
                cartView.displayRestaurantsOrLogout(userId);
            }
        } else {
            handleEmptyCart(userId);
        }
    }

    /**
     * <p>
     * Gets the orders placed by the user.
     * </p>
     *
     * @param userId Represents the id of the current {@link User}
     * @param cartList Represents the list of cart items
     * @param addressId Represents the id of the user chosen address
     * @return orderList Represents the list of order items
     */
    private List<Order> getOrders(final long userId, final List<Cart> cartList, final long addressId) {
        final List<Order> orderList = new ArrayList<>();

        for (final Cart cartItem : cartList) {
            final Order order = new Order();

            order.setCartId(cartItem.getId());
            order.setUserId(userId);
            order.setRestaurantId(cartItem.getRestaurantId());
            order.setFoodId(cartItem.getFoodId());
            order.setQuantity(cartItem.getQuantity());
            order.setAmount(cartItem.getAmount());
            order.setAddressId(addressId);
            orderList.add(order);
        }

        return orderList;
    }

    /**
     * <p>
     * Displays all the addresses of the user.
     * </p>
     *
     * @param userId Represents the id of the current {@link User}
     */
    private long displayAddress(final long userId) {
        final Optional<List<Address>> addresses = orderController.getAddress(userId);
        final List<Address> addressList = addresses.get();

        if (addressList.isEmpty()) {
            LOGGER.info("You Didn't Have Any Previous Order Addresses");

            return getDeliveryAddress(userId);
        } else {
            for (final Address address : addressList) {
                LOGGER.info(String.format("%d %s %s %s %s %s", addressList.indexOf(address) + 1,
                        address.getHouseNumber(), address.getStreetName(), address.getAreaName(), address.getCityName(),
                        address.getPincode()));
            }
            LOGGER.info("Enter The Delivery Address Id");
            final int index = inputHandler.getValue() - 1;

            return addressList.get(index).getId();
        }
    }

    /**
     * <p>
     * Gets the delivery address of the current user.
     * </p>
     *
     * @param userId Represents the id of the current {@link User}
     * @return The address of the current user
     */
    private long getDeliveryAddress(final long userId) {
        final Address address = new Address();

        LOGGER.info("""
                Fill Your Address
                Enter Your House Number""");
        final String houseNumber = inputHandler.getInfo();

        LOGGER.info("Enter Your Street Name");
        final String streetName = inputHandler.getInfo();

        LOGGER.info("Enter Your Area Name");
        final String areaName = inputHandler.getInfo();

        LOGGER.info("Enter Your City Name");
        final String cityName = inputHandler.getInfo();

        LOGGER.info("Enter Your Pin Code");
        final String pinCode = inputHandler.getInfo();

        address.setHouseNumber(houseNumber);
        address.setStreetName(streetName);
        address.setAreaName(areaName);
        address.setCityName(cityName);
        address.setPincode(pinCode);
        address.setUserId(userId);
        orderController.addAddress(address);

        return address.getId();
    }

    /**
     * <p>
     * Handles the user cart when the cart is empty.
     * </p>
     *
     * @param userId Represents the id of the current {@link User}
     */
    private void handleEmptyCart(final long userId) {
        LOGGER.info("""
                Your Order Is Empty
                Please Select A option From Below:
                1.To Order Foods
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
                LOGGER.warn("Enter A Valid Option");
                handleEmptyCart(userId);
            }
        }
    }

    /**
     * <p>
     * Displays the orders placed by the user.
     * </p>
     *
     * @param userId Represents the id of the current {@link User}
     */
    public void displayOrders(final long userId) {
        final Optional<List<Order>> orders = orderController.getOrders(userId);

        if (orders.isPresent()) {
            LOGGER.info("No Placed Orders");
        } else {
            LOGGER.info("""
                    Your Orders
                    Name| Food | Quantity | Amount""");

            for (final Order order : orders.get()) {
                LOGGER.info(String.format("%s %s %d %.2f", order.getRestaurantName(), order.getFoodName(),
                        order.getQuantity(), order.getAmount()));
            }
        }
    }
}
