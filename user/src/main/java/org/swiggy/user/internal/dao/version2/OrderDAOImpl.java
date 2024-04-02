package org.swiggy.user.internal.dao.version2;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.swiggy.common.hibernate.QueryBuilder;
import org.swiggy.common.hibernate.SessionBuilder;
import org.swiggy.common.hibernate.SessionHandler;
import org.swiggy.common.hibernate.TransactionHandler;

import org.swiggy.restaurant.model.Food;

import org.swiggy.user.internal.dao.OrderDAO;
import org.swiggy.user.model.Address;
import org.swiggy.user.model.Order;
import org.swiggy.user.model.User;

import java.util.List;
import java.util.Optional;

/**
 * <p>
 * Implements the data base service of the order related operation
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public class OrderDAOImpl implements OrderDAO {

    private static final Logger LOGGER = LogManager.getLogger(OrderDAOImpl.class);
    private static OrderDAO orderDAO;
    private SessionBuilder sessionBuilder;

    private OrderDAOImpl() {
        sessionBuilder = SessionBuilder.getSessionBuilder().getSessionFactory();
    }

    /**
     * <p>
     * Gets the object of the order database implementation class.
     * </p>
     *
     * @return The order database service implementation object
     */
    public static OrderDAO getInstance(){
        if (null == orderDAO) {
            orderDAO = new OrderDAOImpl();
        }

        return orderDAO;
    }

    /**
     * {@inheritDoc}
     *
     * @param orderList Represents the list of order items
     * @return True if the order is placed, false otherwise
     */
    @Override
    public boolean placeOrder(final List<Order> orderList) {
        Optional<TransactionHandler> transaction = Optional.empty();

        try (final SessionHandler session = sessionBuilder.buildSession()) {
            transaction = Optional.of(session.getTransaction());

            transaction.get().begin();

            for (final Order order : orderList) {
                session.save(order);
            }
            updateCartStatus(orderList);
            transaction.get().commit();

            return true;
        } catch(Exception message) {
            if (transaction.isPresent()) {
                transaction.get().rollBack();
            }
            LOGGER.warn(message.getMessage());
        }

        return false;
    }

    /**
     * <p>
     * Updates the status of the cart.
     * </p>
     *
     * @param orderList Represents the list of order items
     */
    private void updateCartStatus(final List<Order> orderList) {
        final String query = "update cart c set c.status = 2 where c.id = :cartId";
        Optional<TransactionHandler> transaction = Optional.empty();

        try (final SessionHandler session = sessionBuilder.buildSession()) {
            transaction = Optional.of(session.getTransaction());

            transaction.get().begin();
            final QueryBuilder queryBuilder = session.createQuery(query);

            for (final Order order : orderList) {
                queryBuilder.setParameter("cartId", order.getCartId());

                if (0 < queryBuilder.executeUpdate()) {
                    updateQuantity(order.getFoodId(), order.getQuantity());
                }
            }

            transaction.get().commit();
        } catch(Exception message) {
            if (transaction.isPresent()) {
                transaction.get().rollBack();
            }
            LOGGER.warn(message.getMessage());
        }
    }

    /**
     * <p>
     * Updates the food quantity in restaurant after ordered by user.
     * </p>
     *
     * @param foodId Represents the id of the {@link Food}
     * @param quantity quantity Represents the quantity of the food
     */
    private void updateQuantity(final long foodId, final int quantity) {
        final String query = "update food f set f.food_quantity = food_quantity - :quantity where f.id = :foodId";
        Optional<TransactionHandler> transaction = Optional.empty();

        try (final SessionHandler session = sessionBuilder.buildSession()) {
            transaction = Optional.of(session.getTransaction());

            transaction.get().begin();
            final QueryBuilder queryBuilder = session.createQuery(query, Food.class);

            queryBuilder.setParameter("quantity", quantity);
            queryBuilder.setParameter("foodId", foodId);
            queryBuilder.executeUpdate();
            transaction.get().commit();
        } catch(Exception message) {
            if (transaction.isPresent()) {
                transaction.get().rollBack();
            }
            LOGGER.warn(message.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param address Represents the address of the user
     * @return True if the address is added, false otherwise
     */
    @Override
    public boolean addAddress(final Address address) {
        Optional<TransactionHandler> transaction = Optional.empty();

        try (final SessionHandler session = sessionBuilder.buildSession()) {
            transaction = Optional.of(session.getTransaction());

            transaction.get().begin();
            session.save(address);
            transaction.get().commit();

            return true;
        } catch (Exception message) {
            if (transaction.isPresent()) {
                transaction.get().rollBack();
            }
            LOGGER.warn(message.getMessage());
        }

        return false;
    }

    /**
     * {@inheritDoc}
     *
     * @param userId Represents the id of the {@link User}
     */
    @Override
    public Optional<List<Address>> getAddress(final long userId) {
        final String query = "select * from address where user_id = :userId";
        Optional<TransactionHandler> transaction = Optional.empty();

        try (final SessionHandler session = sessionBuilder.buildSession()) {
            transaction = Optional.of(session.getTransaction());

            transaction.get().begin();
            final QueryBuilder queryBuilder = session.createQuery(query, Address.class);

            queryBuilder.setParameter("userId", userId);
            final List<Address> addressList = queryBuilder.executeQuery();

            transaction.get().commit();

            return Optional.of(addressList);
        } catch (Exception message) {
            if (transaction.isPresent()) {
                transaction.get().rollBack();
            }
            LOGGER.warn(message.getMessage());
        }

        return Optional.empty();
    }

    /**
     * {@inheritDoc}
     *
     * @param userId Represents the id of the {@link User}
     */
    @Override
    public Optional<List<Order>> getOrders(final long userId) {
        final String query = """
                select o.id, c.id, f.id, f.name, r.id, r.name, c.quantity, c.total_amount, o.address_id from orders o
                join cart c on o.cart_id = c.id
                join food f on c.food_id = f.id
                join restaurant r on c.restaurant_id = r.id
                where o.user_id = :userId and c.status = 2""";
        Optional<TransactionHandler> transaction = Optional.empty();

        try (final SessionHandler session = sessionBuilder.buildSession()) {
            transaction = Optional.of(session.getTransaction());

            transaction.get().begin();
            final QueryBuilder queryBuilder = session.createQuery(query, Order.class);

            queryBuilder.setParameter("userId", userId);
            final List<Order> orderList = queryBuilder.executeQuery();

            transaction.get().commit();

            return Optional.of(orderList);
        } catch (Exception message) {
            if (transaction.isPresent()) {
                transaction.get().rollBack();
            }
            LOGGER.warn(message.getMessage());
        }

        return Optional.empty();
    }
}
