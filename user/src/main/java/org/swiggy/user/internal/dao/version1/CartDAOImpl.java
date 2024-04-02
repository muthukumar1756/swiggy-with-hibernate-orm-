package org.swiggy.user.internal.dao.version1;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.swiggy.database.connection.DataBaseConnection;
import org.swiggy.restaurant.internal.exception.RestaurantDataLoadFailureException;

import org.swiggy.user.internal.exception.CartDataNotFoundException;
import org.swiggy.user.internal.exception.CartUpdateFailureException;
import org.swiggy.user.internal.dao.CartDAO;
import org.swiggy.user.model.Cart;
import org.swiggy.user.model.CartStatus;
import org.swiggy.user.model.User;

/**
 * <p>
 * Implements the data base service of the cart related operation.
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public final class CartDAOImpl implements CartDAO {

    private static final Logger LOGGER = LogManager.getLogger(CartDAOImpl.class);
    private static CartDAO cartDAO;
    private final Connection connection;

    private CartDAOImpl() {
        connection = DataBaseConnection.getConnection();
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
        try {
            connection.setAutoCommit(false);

            if (isCartEntryExist(cart.getUserId(), cart.getRestaurantId()) || isUserCartEmpty(cart.getUserId())) {
                final String query = """
                        insert into cart (user_id, restaurant_id, food_id, quantity, total_amount) values
                        (?, ?, ?, ?, ?) returning id""";

                try (final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setLong(1, cart.getUserId());
                    preparedStatement.setLong(2, cart.getRestaurantId());
                    preparedStatement.setLong(3, cart.getFoodId());
                    preparedStatement.setInt(4, cart.getQuantity());
                    preparedStatement.setFloat(5, cart.getAmount());
                    final ResultSet resultSet = preparedStatement.executeQuery();

                    resultSet.next();
                    final int cartId = resultSet.getInt(1);

                    cart.setId(cartId);
                    connection.commit();

                    return true;
                }
            }
        } catch (SQLException message) {
            try {
                connection.rollback();
            } catch (SQLException exception) {
                LOGGER.error(message.getMessage());
                throw new CartUpdateFailureException(message.getMessage());
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException message) {
                LOGGER.error(message.getMessage());
                throw new CartUpdateFailureException(message.getMessage());
            }
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
        final String query = "select count(*) from cart where user_id = ? and restaurant_id = ? and status = 1";

        try (final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, userId);
            preparedStatement.setLong(2, restaurantId);
            final ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();
            final int result = resultSet.getInt(1);

            return 0 < result;
        } catch (SQLException message) {
            LOGGER.error(message.getMessage());
            throw new RestaurantDataLoadFailureException(message.getMessage());
        }
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
        final String query = "select count(*) from cart where user_id = ? and status = 1";

        try (final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, userId);
            final ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();
            final int result = resultSet.getInt(1);

            return 0 >= result;
        } catch (SQLException message) {
            LOGGER.error(message.getMessage());
            throw new RestaurantDataLoadFailureException(message.getMessage());
        }
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
                join users u on c.user_id = u.id where u.id = ? and c.status = 1""";

        try (final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, userId);
            final ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.isBeforeFirst()) {
                final List<Cart> cartList = new ArrayList<>();
                while (resultSet.next()) {
                    final Cart cart = new Cart();

                    cart.setId(resultSet.getLong(1));
                    cart.setFoodId(resultSet.getLong(2));
                    cart.setFoodName(resultSet.getString(3));
                    cart.setRestaurantId(resultSet.getLong(4));
                    cart.setRestaurantName(resultSet.getString(5));
                    cart.setQuantity(resultSet.getInt(6));
                    cart.setAmount(resultSet.getFloat(7));
                    final CartStatus cartStatus = CartStatus.getTypeById(resultSet.getInt(8));

                    cart.setCartStatus(cartStatus);
                    cart.setUserId(userId);
                    cartList.add(cart);
                }

                return Optional.of(cartList);
            }
        } catch (SQLException message) {
            LOGGER.error(message.getMessage());
            throw new CartDataNotFoundException(message.getMessage());
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
        final String query = "delete from cart where id = ? and status = 1";

        try (final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, cartId);

            return 0 < preparedStatement.executeUpdate();
        } catch (SQLException message) {
            LOGGER.error(message.getMessage());
            throw new CartUpdateFailureException(message.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param userId Represents the id of the {@link User}
     * @return The true if the cart is cleared, false otherwise
     */
    @Override
    public boolean clearCart(final long userId) {
        final String query = "delete from cart where user_id = ? and status = 1";

        try (final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, userId);

            return 0 < preparedStatement.executeUpdate();
        } catch (SQLException message) {
            LOGGER.error(message.getMessage());
            throw new CartUpdateFailureException(message.getMessage());
        }
    }
}