package org.swiggy.user.internal.controller;

import org.swiggy.user.model.Address;
import org.swiggy.user.model.Order;
import org.swiggy.user.model.User;
import org.swiggy.user.internal.service.OrderService;
import org.swiggy.user.internal.service.impl.OrderServiceImpl;

import java.util.List;
import java.util.Optional;

/**
 * <p>
 * Handles the order related operation and responsible for receiving user input and processing it.
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public class OrderController {

    private static OrderController orderController;
    private final OrderService orderService;

    private OrderController() {
        orderService = OrderServiceImpl.getInstance();
    }

    /**
     * <p>
     * Gets the object of the order controller class.
     * </p>
     *
     * @return The order controller object
     */
    public static OrderController getInstance() {
        if (null == orderController) {
            orderController = new OrderController();
        }

        return orderController;
    }

    /**
     * <p>
     * places the user orders.
     * </p>
     *
     * @param orderList Represents the list of order items
     * @return True if the order is placed, false otherwise
     */
    public boolean placeOrder(final List<Order> orderList) {
        return orderService.placeOrder(orderList);
    }

    /**
     * <p>
     * Stores the address of the user.
     * </p>
     *
     * @param address Represents the address of the user
     */
    public void addAddress(final Address address) {
        orderService.addAddress(address);
    }

    /**
     * <p>
     * Displays all the addresses of the user.
     * </p>
     *
     * @param userId Represents the id of the {@link User}
     * @return List of addresses of the user
     */
    public Optional<List<Address>> getAddress(final long userId) {
        return orderService.getAddress(userId);
    }

    /**
     * <p>
     * Gets the orders placed by the user.
     * </p>
     *
     * @param userId Represents the id of the {@link User}
     * @return List having all the orders placed by the user
     */
    public Optional<List<Order>> getOrders(final long userId) {
        return orderService.getOrders(userId);
    }
}
