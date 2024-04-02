package org.swiggy.user.internal.service.impl;

import org.swiggy.user.internal.dao.OrderDAO;
import org.swiggy.user.internal.dao.version2.OrderDAOImpl;
import org.swiggy.user.model.Address;
import org.swiggy.user.model.Order;
import org.swiggy.user.model.User;
import org.swiggy.user.internal.service.OrderService;

import java.util.List;
import java.util.Optional;

/**
 * <p>
 * Implements the service of the user order related operation.
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public final class OrderServiceImpl implements OrderService {

    private static OrderService orderService;
    private final OrderDAO orderDAO;

    private OrderServiceImpl() {
        orderDAO = OrderDAOImpl.getInstance();
    }

    /**
     * <p>
     * Gets the cart service implementation class object.
     * </p>
     *
     * @return The cart service implementation object
     */
    public static OrderService getInstance() {
        if (null == orderService) {
            orderService = new OrderServiceImpl();
        }

        return orderService;
    }

    /**
     * {@inheritDoc}
     *
     * @param orderList Represents the list of order items
     * @return True if the order is placed, false otherwise
     */
    @Override
    public boolean placeOrder(final List<Order> orderList) {
        return orderDAO.placeOrder(orderList);
    }

    /**
     * {@inheritDoc}
     *
     * @param address Represents the address of the user
     * @return True if the address is added, false otherwise
     */
    @Override
    public boolean addAddress(final Address address) {
        return orderDAO.addAddress(address);
    }

    /**
     * {@inheritDoc}
     *
     * @param userId Represents the id of the {@link User}
     * @return List of addresses of the user
     */
    public Optional<List<Address>> getAddress(final long userId) {
        return orderDAO.getAddress(userId);
    }

    /**
     * {@inheritDoc}
     *
     * @param userId Represents the id of the {@link User}
     * @return List having all the orders placed by the user
     */
    @Override
    public Optional<List<Order>> getOrders(final long userId) {
        return orderDAO.getOrders(userId);
    }
}
