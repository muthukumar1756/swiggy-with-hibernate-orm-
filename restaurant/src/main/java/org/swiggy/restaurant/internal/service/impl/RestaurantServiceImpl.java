package org.swiggy.restaurant.internal.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.swiggy.common.hashgenerator.PasswordHashGenerator;

import org.swiggy.restaurant.internal.dao.RestaurantDAO;
import org.swiggy.restaurant.internal.dao.version2.RestaurantDAOImpl;
import org.swiggy.restaurant.model.Food;
import org.swiggy.restaurant.model.Restaurant;
import org.swiggy.restaurant.model.RestaurantData;
import org.swiggy.restaurant.internal.service.RestaurantService;

/**
 * <p>
 * Implements the service of the restaurant related operation.
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public final class RestaurantServiceImpl implements RestaurantService {

    private static RestaurantService restaurantService;
    private final RestaurantDAO restaurantDAO;

    private RestaurantServiceImpl() {
        restaurantDAO = RestaurantDAOImpl.getInstance();
    }

    /**
     * <p>
     * Gets the restaurant service implementation object.
     * </p>
     *
     * @return The restaurant service implementation object
     */
    public static RestaurantService getInstance() {
        if (null == restaurantService) {
            restaurantService = new RestaurantServiceImpl();
        }

        return restaurantService;
    }

    /**
     * {@inheritDoc}
     *
     * @param restaurant Represents the restaurant
     * @return True if restaurant profile is created, false otherwise
     */
    @Override
    public boolean createRestaurantProfile(final Restaurant restaurant) {
        final String hashPassword = PasswordHashGenerator.getInstance().hashPassword(restaurant.getPassword());

        restaurant.setPassword(hashPassword);

        return restaurantDAO.createRestaurantProfile(restaurant);
    }

    /**
     * {@inheritDoc}
     *
     * @param restaurantDataType Represents the type of data of the restaurant
     * @param restaurantData Represents the data of the restaurant
     * @param password Represents the password of the restaurant
     * @return The restaurant object
     */
    @Override
    public Optional<Restaurant> getRestaurant(final Optional<RestaurantData> restaurantDataType, final String restaurantData,
                                    final String password) {
        final String hashPassword = PasswordHashGenerator.getInstance().hashPassword(password);

        return restaurantDAO.getRestaurant(restaurantDataType.get().name(), restaurantData, hashPassword);
    }

    /**
     * {@inheritDoc}
     *
     * @param restaurantId Represents the id of the restaurant
     * @return The restaurant object
     */
    public Optional<Restaurant> getRestaurantById(final long restaurantId) {
        return restaurantDAO.getRestaurantById(restaurantId);
    }

    /**
     * {@inheritDoc}
     *
     * @param restaurants Represents list of restaurants
     */
    @Override
    public boolean loadRestaurantList(final List<Restaurant> restaurants) {
        return restaurantDAO.loadRestaurantList(restaurants);
    }

    /**
     * {@inheritDoc}
     *
     * @param menuCard Contains the list of foods from the restaurant
     */
    public void loadMenuCard(final Map<Food, Long> menuCard) {
        restaurantDAO.loadMenuCard(menuCard);
    }

    /**
     * {@inheritDoc}
     *
     * @param food Represents the current food added by the restaurant
     * @param restaurantId Represents the id of the Restaurant
     * @return  True if food is added, false otherwise
     */
    @Override
    public boolean addFood(final Food food, final long restaurantId) {
        return restaurantDAO.addFood(food, restaurantId);
    }

    /**
     * {@inheritDoc}
     *
     * @return The list of all restaurants
     */
    @Override
    public Optional<List<Restaurant>> getRestaurants() {
        return restaurantDAO.getRestaurants();
    }

    /**
     * {@inheritDoc}
     *
     * @param foodId Represents the id of the food
     * @return Available quantity of food from the restaurant
     */
    @Override
    public Optional<Integer> getFoodQuantity(final long foodId) {
        return restaurantDAO.getFoodQuantity(foodId);
    }

    /**
     * {@inheritDoc}
     *
     * @param restaurantId Represents the id of the restaurant
     * @param foodTypeId Represents the id of the food type.
     * @return The list of menucard having foods
     */
    @Override
    public Optional<List<Food>> getMenuCard(final long restaurantId, final int foodTypeId) {
        return restaurantDAO.getMenuCard(restaurantId, foodTypeId);
    }

    /**
     * {@inheritDoc}
     *
     * @param foodId Represents the id of the food
     * @return True if food is removed, false otherwise
     */
    @Override
    public boolean removeFood(final long foodId) {
        return restaurantDAO.removeFood(foodId);
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
    public boolean updateRestaurantData(final long restaurantId, final String restaurantData,
                                     final Optional<RestaurantData> type) {
        return restaurantDAO.updateRestaurantData(restaurantId, type.get().name(), restaurantData);
    }
}