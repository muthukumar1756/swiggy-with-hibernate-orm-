package org.swiggy.user.internal.dao.version2;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.swiggy.common.hibernate.QueryBuilder;
import org.swiggy.common.hibernate.SessionBuilder;
import org.swiggy.common.hibernate.SessionHandler;
import org.swiggy.common.hibernate.TransactionHandler;

import org.swiggy.user.internal.dao.CartDAO;
import org.swiggy.user.model.Cart;
import org.swiggy.user.model.User;

import java.util.List;
import java.util.Optional;

/**
 * <p>
 * Implements the data base service of the cart related operation.
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public class CartDAOImpl implements CartDAO {

    private static final Logger LOGGER = LogManager.getLogger(CartDAOImpl.class);
    private static CartDAO cartDAO;
    private SessionBuilder sessionBuilder;

    private CartDAOImpl() {
        sessionBuilder = SessionBuilder.getSessionBuilder().getSessionFactory();
    }

    /**
     * <p>
     * Gets the object of the cart database implementation class.
     * </p>
     *
     * @return The cart database service implementation object
     */
    public static CartDAO getInstance() {
        if (null == cartDAO) {
            return cartDAO = new CartDAOImpl();
        }

        return cartDAO;
    }

    /**
     * {@inheritDoc}
     *
     * @param cart Represents the cart of the user
     * @return True if the food is added to the user cart, false otherwise
     */
    @Override
    public boolean addFoodToCart(final Cart cart) {
         Optional<TransactionHandler> transaction = Optional.empty();

        try (final SessionHandler session = sessionBuilder.buildSession()) {
            transaction = Optional.of(session.getTransaction());

            transaction.get().begin();

            if (isCartEntryExist(cart.getUserId(), cart.getRestaurantId()) || isUserCartEmpty(cart.getUserId())) {
                session.save(cart);
                transaction.get().commit();

                return true;
            }
        } catch (Exception message) {
            if (transaction.isPresent()) {
                transaction.get().rollBack();
            }
            LOGGER.warn(message.getMessage());
        }

        return false;
    }

    /**
     * <p>
     * Checks the user and the restaurant id for already having an entry.
     * </p>
     *
     * @param userId Represents the id of the {@link User}
     * @param restaurantId Represents the id of the restaurant
     * @return True if the user and restaurant entry is exist in cart, false otherwise
     */
    private boolean isCartEntryExist(final long userId, final long restaurantId) {
        final String query = """
                select count(*) from cart c where c.user_id = :userId and c.restaurant_id = :restaurantId
                and c.status = 1""";
         Optional<TransactionHandler> transaction = Optional.empty();

        try (final SessionHandler session = sessionBuilder.buildSession()) {
            transaction = Optional.of(session.getTransaction());

            transaction.get().begin();
            final QueryBuilder queryObject = session.createQuery(query, Long.class);

            queryObject.setParameter("userId", userId);
            queryObject.setParameter("restaurantId", restaurantId);

            final Long result = (Long) queryObject.getSingleResult();

            transaction.get().commit();

            return 0 >= result;
        } catch (Exception message) {
            if (transaction.isPresent()) {
                transaction.get().rollBack();
            }
            LOGGER.warn(message.getMessage());
        }

        return false;
    }

    /**
     * <p>
     * Checks the user has any entry in the cart.
     * </p>
     *
     * @param userId Represents the id of the {@link User}
     * @return True if the user entry is exist in cart, false otherwise
     */
    private boolean isUserCartEmpty(final long userId) {
        final String query = "select count(*) from cart c where c.user_id = :userId and c.status = 1";
         Optional<TransactionHandler> transaction = Optional.empty();

        try (final SessionHandler session = sessionBuilder.buildSession()) {
            transaction = Optional.of(session.getTransaction());

            transaction.get().begin();
            final QueryBuilder queryObject = session.createQuery(query, Long.class);

            queryObject.setParameter("userId", userId);
            final Long result = (Long) queryObject.getSingleResult();
            transaction.get().commit();

            return 0 >= result;
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
     * @param userId Represents the id 0f the {@link User}
     * @return The list of all foods from the user cart
     */
    @Override
    public Optional<List<Cart>> getCart(final long userId) {
        final String query = """
                select c.id, f.id, f.name, r.id, r.name, c.quantity, c.total_amount, c.status from food f
                join cart c on f.id = c.food_id
                join restaurant r on c.restaurant_id = r.id
                join users u on c.user_id = u.id where u.id = :userId and c.status = 1""";
         Optional<TransactionHandler> transaction = Optional.empty();

        try (final SessionHandler session = sessionBuilder.buildSession()) {
            transaction = Optional.of(session.getTransaction());

            transaction.get().begin();
            final QueryBuilder queryObject = session.createQuery(query, Cart.class);

            queryObject.setParameter("userId", userId);
            final List<Cart> cartList = queryObject.executeQuery();
            transaction.get().commit();

            return Optional.of(cartList);
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
     * @param cartId Represents the id of the user cart
     * @return True if the food is removed,false otherwise
     */
    @Override
    public boolean removeFood(final long cartId) {
        final String query = "delete from cart c where c.id = :cartId and c.status = 1";
         Optional<TransactionHandler> transaction = Optional.empty();

        try (final SessionHandler session = sessionBuilder.buildSession()) {
            transaction = Optional.of(session.getTransaction());

            transaction.get().begin();
            final QueryBuilder queryObject = session.createQuery(query);

            queryObject.setParameter("cartId", cartId);
            final int result = queryObject.executeUpdate();
            transaction.get().commit();

            return 0 < result;
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
     * @return The true if the cart is cleared, false otherwise
     */
    @Override
    public boolean clearCart(final long userId) {
        final String query = "delete from cart c where c.user_id = :userId and c.status = 1";
         Optional<TransactionHandler> transaction = Optional.empty();

        try (final SessionHandler session = sessionBuilder.buildSession()) {
            transaction = Optional.of(session.getTransaction());

            transaction.get().begin();
            final QueryBuilder queryObject = session.createQuery(query);

            queryObject.setParameter("userId", userId);
            final long result = queryObject.executeUpdate();

            transaction.get().commit();

            return 0 < result;
        } catch(Exception message) {
            if (transaction.isPresent()) {
                transaction.get().rollBack();
            }
            LOGGER.warn(message.getMessage());
        }

        return false;
    }
}