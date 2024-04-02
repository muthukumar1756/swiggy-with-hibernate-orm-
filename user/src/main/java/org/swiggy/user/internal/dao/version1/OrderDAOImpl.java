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

import org.swiggy.restaurant.model.Food;

import org.swiggy.user.internal.exception.AddressDataLoadFailureException;
import org.swiggy.user.internal.exception.OrderDataNotFoundException;
import org.swiggy.user.internal.exception.OrderPlacementFailureException;
import org.swiggy.user.internal.dao.OrderDAO;
import org.swiggy.user.model.Address;
import org.swiggy.user.model.AddressType;
import org.swiggy.user.model.Order;
import org.swiggy.user.model.User;

/**
 * <p>
 * Implements the data base service of the order related operation
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public final class OrderDAOImpl implements OrderDAO {

    private static final Logger LOGGER = LogManager.getLogger(OrderDAOImpl.class);
    private static OrderDAO orderDAO;
    private final Connection connection;

    private OrderDAOImpl() {
        connection = DataBaseConnection.getConnection();
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
        try {
            connection.setAutoCommit(false);
            final String query = "insert into orders (user_id, cart_id, address_id) values(?, ?, ?)";

            try (final PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                for (final Order order : orderList) {
                    preparedStatement.setLong(1, order.getUserId());
                    preparedStatement.setLong(2, order.getCartId());
                    preparedStatement.setLong(3, order.getAddressId());
                    preparedStatement.executeUpdate();
                }
                updateCartStatus(orderList);
                connection.commit();

                return true;
            }
        } catch (SQLException message) {
            try {
                connection.rollback();
            } catch (SQLException exception) {
                LOGGER.error(message.getMessage());
                throw new OrderPlacementFailureException(message.getMessage());
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException message) {
                LOGGER.error(message.getMessage());
                throw new OrderPlacementFailureException(message.getMessage());
            }
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
        final String query = "update cart set status = 2 where id = ?";

        try (final PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            for (final Order order : orderList) {
                preparedStatement.setLong(1, order.getCartId());

                if (0 < preparedStatement.executeUpdate()) {
                    updateQuantity(order.getFoodId(), order.getQuantity());
                }
            }
        } catch (SQLException message) {
            LOGGER.error(message.getMessage());
            throw new AddressDataLoadFailureException(message.getMessage());
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
        final String query = "update food f set f.quantity = quantity - ? where f.id = ?";

        try (final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, quantity);
            preparedStatement.setLong(2, foodId);

            preparedStatement.executeUpdate();
        } catch (SQLException message) {
            LOGGER.error(message.getMessage());
            throw new OrderPlacementFailureException(message.getMessage());
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
        final String query = """
                insert into address (user_id, house_number, street_name, area_name, city_name, pincode, address_type)
                values (?, ?, ?, ?, ?, ?, ?) returning id""";

        try (final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, address.getUserId());
            preparedStatement.setString(2, address.getHouseNumber());
            preparedStatement.setString(3, address.getStreetName());
            preparedStatement.setString(4, address.getAreaName());
            preparedStatement.setString(5, address.getCityName());
            preparedStatement.setString(6, address.getPincode());
            preparedStatement.setInt(7, AddressType.getId(address.getAddressType()));
            final ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();
            final int addressId = resultSet.getInt(1);

            address.setId(addressId);

            return true;
        } catch (SQLException message) {
            LOGGER.error(message.getMessage());
            throw new AddressDataLoadFailureException(message.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param userId Represents the id of the {@link User}
     */
    @Override
    public Optional<List<Address>> getAddress(final long userId) {
        final String query = "select * from address where user_id = ?";

        try (final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, userId);
            final ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.isBeforeFirst()) {
                final List<Address> addressList = new ArrayList<>();

                while (resultSet.next()) {
                    final Address address = new Address();

                    address.setId(resultSet.getInt(1));
                    address.setUserId(resultSet.getInt(2));
                    address.setHouseNumber(resultSet.getString(3));
                    address.setStreetName(resultSet.getString(4));
                    address.setAreaName(resultSet.getString(5));
                    address.setCityName(resultSet.getString(6));
                    address.setPincode(resultSet.getString(7));
                    final AddressType addressType = AddressType.getTypeById(resultSet.getInt(8));

                    address.setAddressType(addressType);
                    addressList.add(address);
                }

                return Optional.of(addressList);
            }
        } catch (SQLException message) {
            LOGGER.error(message.getMessage());
            throw new AddressDataLoadFailureException(message.getMessage());
        }

        return Optional.empty();
    }

    /**
     * {@inheritDoc}
     *
     * @param userId Represents the id of the {@link User}
     * @return List having all the orders placed by the user
     */
    @Override
    public Optional<List<Order>> getOrders(final long userId) {
        final String query = """
                select o.id, c.id, f.id, f.name, r.id, r.name, c.quantity, c.total_amount, o.address_id from orders o
                join cart c on o.cart_id = c.id
                join food f on c.food_id = f.id
                join restaurant r on c.restaurant_id = r.id
                where o.user_id = ? and c.status = 2""";

        try (final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, userId);
            final ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.isBeforeFirst()) {
                final List<Order> orderList = new ArrayList<>();

                while (resultSet.next()) {
                    final Order order = new Order();

                    order.setId(resultSet.getInt(1));
                    order.setCartId(resultSet.getLong(2));
                    order.setFoodId(resultSet.getLong(3));
                    order.setFoodName(resultSet.getString(4));
                    order.setRestaurantId(resultSet.getLong(5));
                    order.setRestaurantName(resultSet.getString(6));
                    order.setQuantity(resultSet.getInt(7));
                    order.setAmount(resultSet.getFloat(8));
                    order.setAddressId(resultSet.getLong(9));
                    order.setUserId(userId);
                    orderList.add(order);
                }

                return Optional.of(orderList);
            }
        } catch (SQLException message) {
            LOGGER.error(message.getMessage());
            throw new OrderDataNotFoundException(message.getMessage());
        }

        return Optional.empty();
    }
}