package org.swiggy.user.internal.service;

import org.swiggy.user.model.Address;
import org.swiggy.user.model.Order;
import org.swiggy.user.model.User;

import java.util.List;
import java.util.Optional;

/**
 * <p>
 * Provides the services for the user orders.
 * </p>
 *
 * @author Muthu kumar v
 * @version 1.0
 */
public interface OrderService {

    /**
     * <p>
     * places the user orders.
     * </p>
     *
     * @param orderList Represents the list of order items
     * @return True if the order is placed, false otherwise
     */
    boolean placeOrder(final List<Order> orderList);

    /**
     * <p>
     * Stores the address of the user.
     * </p>
     *
     * @param address Represents the address of the user
     * @return True if the address is added, false otherwise
     */
    boolean addAddress(final Address address);

    /**
     * <p>
     * Displays all the addresses of the user.
     * </p>
     *
     * @param userId Represents the id of the {@link User}
     * @return List of addresses of the user
     */
    Optional<List<Address>> getAddress(final long userId);

    /**
     * <p>
     * Gets the orders placed by the user.
     * </p>
     *
     * @param userId Represents the id of the {@link User}
     * @return List having all the orders placed by the user
     */
    Optional<List<Order>> getOrders(final long userId);
}
