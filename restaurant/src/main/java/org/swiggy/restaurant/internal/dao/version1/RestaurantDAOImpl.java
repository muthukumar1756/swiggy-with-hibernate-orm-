package org.swiggy.restaurant.internal.dao.version1;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.swiggy.restaurant.internal.exception.FoodDataLoadFailureException;
import org.swiggy.restaurant.internal.exception.MenuCardNotFoundException;
import org.swiggy.restaurant.internal.exception.FoodCountAccessException;
import org.swiggy.restaurant.internal.exception.RestaurantDataLoadFailureException;
import org.swiggy.restaurant.internal.dao.RestaurantDAO;
import org.swiggy.database.connection.DataBaseConnection;
import org.swiggy.restaurant.model.Food;
import org.swiggy.restaurant.model.FoodType;
import org.swiggy.restaurant.model.Restaurant;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * <p>
 * Implements the data base service of the restaurant related operation.
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public final class RestaurantDAOImpl implements RestaurantDAO {

    private static final Logger LOGGER = LogManager.getLogger(RestaurantDAOImpl.class);
    private static RestaurantDAO restaurantDAO;
    private final Connection connection;

    private RestaurantDAOImpl() {
        connection = DataBaseConnection.getConnection();
    }

    /**
     * <p>
     * Gets the object of the restaurant database implementation class.
     * </p>
     *
     * @return The restaurant database service implementation object
     */
    public static RestaurantDAO getInstance() {
        if (null == restaurantDAO) {
            return restaurantDAO = new RestaurantDAOImpl();
        }

        return restaurantDAO;
    }

    /**
     * {@inheritDoc}
     *
     * @param restaurant Represents the restaurant
     * @return True if restaurant profile is created, false otherwise
     */
    @Override
    public boolean createRestaurantProfile(final Restaurant restaurant) {
        final String query = """
                insert into restaurant (name, phone_number, email_id, password) values (?, ?, ?, ?) returning id""";

        try (final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, restaurant.getName());
            preparedStatement.setString(2, restaurant.getPhoneNumber());
            preparedStatement.setString(3, restaurant.getEmailId());
            preparedStatement.setString(4, restaurant.getPassword());
            final ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();
            restaurant.setId(resultSet.getInt(1));

            return true;
        } catch (SQLException message) {
            LOGGER.error(message.getMessage());
            throw new RestaurantDataLoadFailureException(message.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param restaurantDataType Represents the type of data of the restaurant
     * @param restaurantData Represents the data of the restaurant
     * @param password Represents the password of the restaurant
     * @return The restaurant object
     */
    public Optional<Restaurant> getRestaurant(final String restaurantDataType, final String restaurantData,
                                                  final String password) {
        final String query = String.join("",
                "select id, name, phone_number, email_id, password from restaurant where ",
                restaurantDataType, " = ? and password = ?");

        try (final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, restaurantData);
            preparedStatement.setString(2, password);
            final ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                final Restaurant restaurant = new Restaurant();

                restaurant.setId(resultSet.getInt(1));
                restaurant.setName(resultSet.getString(2));
                restaurant.setPhoneNumber(resultSet.getString(3));
                restaurant.setEmailId(resultSet.getString(4));
                restaurant.setPassword(resultSet.getString(5));

                return Optional.of(restaurant);
            }
        } catch (SQLException message) {
            LOGGER.error(message.getMessage());
            throw new RestaurantDataLoadFailureException(message.getMessage());
        }

        return Optional.empty();
    }

    /**
     * {@inheritDoc}
     *
     * @param restaurantId Represents the id of the restaurant
     * @return The restaurant object
     */
    @Override
    public Optional<Restaurant> getRestaurantById(final long restaurantId) {
        final String query = "select id, name, phone_number, email_id, password from restaurant where id = ?";

        try (final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, restaurantId);
            final ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                final Restaurant restaurant = new Restaurant();

                restaurant.setId(resultSet.getInt(1));
                restaurant.setName(resultSet.getString(2));
                restaurant.setPhoneNumber(resultSet.getString(3));
                restaurant.setEmailId(resultSet.getString(4));
                restaurant.setPassword(resultSet.getString(5));

                return Optional.of(restaurant);
            }
        } catch (SQLException message) {
            LOGGER.error(message.getMessage());
            throw new RestaurantDataLoadFailureException(message.getMessage());
        }

        return Optional.empty();
    }

    /**
     * {@inheritDoc}
     *
     * @param restaurants Represents list of restaurants
     */
    @Override
    public boolean loadRestaurantList(final List<Restaurant> restaurants) {
        final String query = "insert into restaurant (name) values (?) returning id";

        for (final Restaurant restaurant : restaurants) {
            final String restaurantName = restaurant.getName();

            try (final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, restaurantName);
                final ResultSet resultSet = preparedStatement.executeQuery();

                resultSet.next();
                final int restaurantId = resultSet.getInt(1);

                restaurant.setId(restaurantId);
            } catch (SQLException message) {
                LOGGER.error(message.getMessage());
                throw new RestaurantDataLoadFailureException(message.getMessage());
            }
        }

        return true;
    }

    /**
     * {@inheritDoc}
     *
     * @return The list of all restaurants
     */
    @Override
    public Optional<List<Restaurant>> getRestaurants() {
        final String query = "select id, name, phone_number, email_id, password from restaurant";

        try (final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            final ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.isBeforeFirst()) {
                final List<Restaurant> restaurants = new ArrayList<>();

                while (resultSet.next()) {
                    final Restaurant restaurant = new Restaurant();

                    restaurant.setId(resultSet.getInt(1));
                    restaurant.setName(resultSet.getString(2));
                    restaurant.setPhoneNumber(resultSet.getString(3));
                    restaurant.setEmailId(resultSet.getString(4));
                    restaurant.setPassword(resultSet.getString(5));
                    restaurants.add(restaurant);
                }

                return Optional.of(restaurants);
            }
        } catch (SQLException message) {
            LOGGER.error(message.getMessage());
            throw new FoodDataLoadFailureException(message.getMessage());
        }

        return Optional.empty();
    }

    /**
     * {@inheritDoc}
     *
     * @param food Represents the food added by the restaurant
     * @param restaurantId Represents the id of the restaurant
     * @return True if food is added, false otherwise
     */
    @Override
    public boolean addFood(final Food food, final long restaurantId) {
        try {
            connection.setAutoCommit(false);
            final String query = """
                    insert into food (name, rate, type, quantity) values(?, ?, ?, ?) returning id""";

            try (final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, food.getName());
                preparedStatement.setFloat(2, food.getRate());
                preparedStatement.setInt(3, FoodType.getId(food.getType()));
                preparedStatement.setInt(4, food.getQuantity());
                final ResultSet resultSet = preparedStatement.executeQuery();

                resultSet.next();

                food.setId(resultSet.getInt(1));
                mapFoodsWithRestaurant(food.getId(), restaurantId);
            }
            connection.commit();

            return true;
        } catch (SQLException message) {
            try {
                connection.rollback();
            } catch (SQLException exception) {
                LOGGER.error(message.getMessage());
                throw new FoodDataLoadFailureException(exception.getMessage());
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException message) {
                LOGGER.error(message.getMessage());
                throw new FoodDataLoadFailureException(message.getMessage());
            }
        }

        return false;
    }

    /**
     * {@inheritDoc}
     *
     * @param menuCard Contains the list of foods from the restaurant
     */
    @Override
    public void loadMenuCard(final Map<Food, Long> menuCard) {
        final String query = " insert into food(name, rate, type, quantity) values(?, ?, ?, ?) returning id";

        try {
            connection.setAutoCommit(false);

            for (final Map.Entry<Food, Long> restaurantFood : menuCard.entrySet()) {
                final Food food = restaurantFood.getKey();
                final Long restaurantId = restaurantFood.getValue();

                try (final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, food.getName());
                    preparedStatement.setFloat(2, food.getRate());
                    preparedStatement.setInt(3, FoodType.getId(food.getType()));
                    preparedStatement.setInt(4, food.getQuantity());
                    final ResultSet resultSet = preparedStatement.executeQuery();

                    resultSet.next();
                    final int foodId = resultSet.getInt(1);

                    food.setId(foodId);
                    mapFoodsWithRestaurant(food.getId(), restaurantId);
                }
            }
            connection.commit();
        } catch (SQLException message) {
            try {
                connection.rollback();
            } catch (SQLException exception) {
                LOGGER.error(message.getMessage());
                throw new FoodDataLoadFailureException(exception.getMessage());
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException message) {
                LOGGER.error(message.getMessage());
                throw new FoodDataLoadFailureException(message.getMessage());
            }
        }
    }

    /**
     * <p>
     * Maps the food with restaurant.
     * </p>
     *
     * @param foodId Represents the id of the food
     * @param restaurantId Represents the id of the restaurant
     */
    private void mapFoodsWithRestaurant(final long foodId, final long restaurantId) {
        final String query = "insert into restaurant_food (food_id, restaurant_id) values(?, ?)";

        try (final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, foodId);
            preparedStatement.setLong(2, restaurantId);
            preparedStatement.executeUpdate();
        } catch (SQLException message) {
            LOGGER.error(message.getMessage());
            throw new FoodDataLoadFailureException(message.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param foodId Represents the id of the food
     * @return Available quantity of food from the restaurant
     */
    public Optional<Integer> getFoodQuantity(final long foodId) {
        final String query = "select quantity from food where id = ?";

        try (final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, foodId);
            final ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();
            final Integer quantity = resultSet.getInt(1);

            return Optional.of(quantity);
        } catch (SQLException message) {
            LOGGER.error(message.getMessage());
            throw new FoodCountAccessException(message.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param restaurantId Represents the id of the Restaurant
     * @param foodTypeId Represents the id of the food type.
     * @return The list of menucard having foods
     */
    @Override
    public Optional<List<Food>> getMenuCard(final long restaurantId, final int foodTypeId) {
        final String query = """
                select f.id, f.name, f.rate, f.type, f.quantity from food f
                join restaurant_food rf on f.id = rf.food_id
                join restaurant r on rf.restaurant_id = r.id where r.id = ? and f.type in (?, ?)""";

        try (final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, restaurantId);

            if (1 == foodTypeId) {
                preparedStatement.setInt(2, 1);
                preparedStatement.setInt(3, -1);
            } else if (2 == foodTypeId) {
                preparedStatement.setInt(2, 2);
                preparedStatement.setInt(3, -1);
            } else {
                preparedStatement.setInt(2, 1);
                preparedStatement.setInt(3, 2);
            }
            final ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.isBeforeFirst()) {
                final List<Food> menucard = new ArrayList<>();

                while (resultSet.next()) {
                    final Food food = new Food();

                    food.setId(resultSet.getInt(1));
                    food.setName(resultSet.getString(2));
                    food.setRate(resultSet.getFloat(3));
                    food.setType(FoodType.getTypeById(resultSet.getInt(4)));
                    food.setQuantity(resultSet.getInt(5));
                    menucard.add(food);
                }

                return Optional.of(menucard);
            }
        } catch (SQLException message) {
            LOGGER.error(message.getMessage());
            throw new MenuCardNotFoundException(message.getMessage());
        }

        return Optional.empty();
    }

    /**
     * {@inheritDoc}
     *
     * @param foodId Represents the id of the food
     * @return True if food is removed, false otherwise
     */
    @Override
    public boolean removeFood(final long foodId) {
        final String query = "delete from food where id = ?";

        try (final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, foodId);

            return 0 < preparedStatement.executeUpdate();
        } catch (SQLException message) {
            LOGGER.error(message.getMessage());
            throw new FoodDataLoadFailureException(message.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param restaurantId Represents the id of the restaurant
     * @param restaurantData Represents the data of the restaurant to be updated
     * @param type Represents the type of data of the restaurant to be updated
     * @return True if data is updated, false otherwise
     */
    @Override
    public boolean updateRestaurantData(final long restaurantId, final String type, final String restaurantData) {
        final String query = String.join("", "update restaurant set ", type, " = ? where id = ?");

        try (final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, restaurantData);
            preparedStatement.setLong(2, restaurantId);

            return 0 < preparedStatement.executeUpdate();
        } catch (SQLException message) {
            LOGGER.error(message.getMessage());
            throw new RestaurantDataLoadFailureException(message.getMessage());
        }
    }
}