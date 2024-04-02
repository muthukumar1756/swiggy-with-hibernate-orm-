package org.swiggy.restaurant.internal.controller;

import org.swiggy.restaurant.model.Food;
import org.swiggy.restaurant.model.Restaurant;
import org.swiggy.restaurant.model.RestaurantData;
import org.swiggy.restaurant.internal.service.RestaurantService;
import org.swiggy.restaurant.internal.service.impl.RestaurantServiceImpl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * <p>
 * Handles the restaurant related operation and responsible for receiving input and processing it.
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public final class RestaurantController {

    private static RestaurantController restaurantController;
    private final RestaurantService restaurantService;

    private RestaurantController() {
        restaurantService = RestaurantServiceImpl.getInstance();
    }

    /**
     * <p>
     * Gets the restaurant controller object.
     * </p>
     *
     * @return The restaurant controller object
     */
    public static RestaurantController getInstance() {
        if (null == restaurantController) {
            restaurantController = new RestaurantController();
        }

        return restaurantController;
    }

    /**
     * <p>
     * Creates the restaurant profile.
     * </p>
     *
     * @param restaurant Represents the restaurant
     * @return True if restaurant profile is created, false otherwise
     */
    public boolean createRestaurantProfile(final Restaurant restaurant) {
        return restaurantService.createRestaurantProfile(restaurant);
    }

    /**
     * <p>
     * Gets the restaurant if the phone_number and password matches.
     * </p>
     *
     * @param restaurantDataType Represents the type of data of the restaurant
     * @param restaurantData Represents the data of the restaurant
     * @param password Represents the password of the restaurant
     * @return The restaurant object
     */
    public Optional<Restaurant> getRestaurant(final RestaurantData restaurantDataType, final String restaurantData,
                                              final String password) {
        return restaurantService.getRestaurant(Optional.of(restaurantDataType), restaurantData, password);
    }

    /**
     * <p>
     * Gets the restaurant if the id matches.
     * </p>
     *
     * @param restaurantId Represents the id of the restaurant
     * @return The restaurant object
     */
    public Optional<Restaurant> getRestaurantById(final long restaurantId) {
        return restaurantService.getRestaurantById(restaurantId);
    }

    /**
     * <p>
     * Gets all the restaurants
     * </p>
     *
     * @return The list of all restaurants
     */
    public Optional<List<Restaurant>> getRestaurants() {
        return restaurantService.getRestaurants();
    }

    /**
     * <p>
     * Loads all the restaurants data.
     * </p>
     *
     * @param restaurants Represents list of restaurants
     */
    public boolean loadRestaurantList(final List<Restaurant> restaurants) {
        return restaurantService.loadRestaurantList(restaurants);
    }

    /**
     * <p>
     * Loads the menucard of the restaurant.
     * </p>
     *
     * @param menuCard Contains the list of foods from the restaurant
     */
    public void loadMenuCard(final Map<Food, Long> menuCard) {
        restaurantService.loadMenuCard(menuCard);
    }

    /**
     * <p>
     * Gets the available food quantity in the restaurant.
     * </p>
     *
     * @param foodId Represents the id of the {@link Food}
     * @return Available quantity from the restaurant
     */
    public Optional<Integer> getFoodQuantity(final long foodId) {
        return restaurantService.getFoodQuantity(foodId);
    }

    /**
     * <p>
     * Loads the food details given from restaurant.
     * </p>
     *
     * @param food Represents the current food added by the restaurant
     * @param restaurantId Represents the id of the restaurant
     */
    public void addFood(final Food food, final long restaurantId) {
        restaurantService.addFood(food, restaurantId);
    }

    /**
     * <p>
     * Removes the food from the restaurant.
     * </p>
     *
     * @param foodId Represents the id of the food
     */
    public void removeFood(final long foodId) {
        restaurantService.removeFood(foodId);
    }

    /**
     * <p>
     * Gets the menucard of the restaurant.
     * </p>
     *
     * @param restaurantId Represents the id of the restaurant
     * @param foodTypeId Represents the id of the food type.
     * @return The list of menucard having foods
     */
    public Optional<List<Food>> getMenuCard(final long restaurantId, final int foodTypeId) {
        return restaurantService.getMenuCard(restaurantId, foodTypeId);
    }

    /**
     * <p>
     * Updates the data of the current restaurant user.
     * </p>
     *
     * @param restaurantId Represents the id of the restaurant
     * @param restaurantData Represents the data of the restaurant to be updated
     * @param type Represents the type of data of the restaurant to be updated
     */
    public void updateRestaurantData(final long restaurantId, final String restaurantData,
                                     final RestaurantData type) {
        restaurantService.updateRestaurantData(restaurantId, restaurantData, Optional.of(type));
    }
}