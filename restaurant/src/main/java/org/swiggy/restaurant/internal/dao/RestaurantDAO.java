package org.swiggy.restaurant.internal.dao;

import org.swiggy.restaurant.model.Food;
import org.swiggy.restaurant.model.Restaurant;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * <p>
 * Provides data base service for the restaurant
 * </p>
 *
 * @author Muthu kumar v
 * @version 1.1
 */
public interface RestaurantDAO {

     /**
      * <p>
      * Creates the restaurant profile.
      * </p>
      *
      * @param restaurant Represents the restaurant
      * @return True if restaurant profile is created, false otherwise
      */
     boolean createRestaurantProfile(final Restaurant restaurant);

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
     Optional<Restaurant> getRestaurant(final String restaurantDataType, final String restaurantData,
                                            final String password);

     /**
      * <p>
      * Gets the restaurant if the id matches.
      * </p>
      *
      * @param restaurantId Represents the id of the restaurant
      * @return The restaurant object
      */
     Optional<Restaurant> getRestaurantById(final long restaurantId);

     /**
      * <p>
      * Gets all the restaurants
      * </p>
      *
      * @return The list of all restaurants
      */
     Optional<List<Restaurant>> getRestaurants();

     /**
      * <p>
      * Loads all the restaurants data.
      * </p>
      *
      * @param restaurants Represents list of restaurants
      */
     boolean loadRestaurantList(final List<Restaurant> restaurants);

     /**
      * <p>
      * Loads the food details given from restaurant.
      * </p>
      *
      * @param food Represents the current food added by the restaurant
      * @param restaurantId Represents the id of the restaurant
      * @return True if food is added, false otherwise
      */
     boolean addFood(final Food food, final long restaurantId);

     /**
      * <p>
      * Loads the menucard of the restaurant.
      * </p>
      *
      * @param menuCard Contains the list of foods from the restaurant
      */
     void loadMenuCard(final Map<Food, Long> menuCard);

     /**
      * <p>
      * Gets the available food quantity in the restaurant .
      * </p>
      *
      * @param foodId Represents the id of the food
      * @return Available quantity of food from the restaurant
      */
     Optional<Integer> getFoodQuantity(final long foodId);

     /**
      * <p>
      * Gets the menucard of the selected restaurant by the user.
      * </p>
      *
      * @param restaurantId Represents the id of the restaurant
      * @param foodTypeId Represents the id of the food type.
      * @return The list of menucard having foods
      */
     Optional<List<Food>> getMenuCard(final long restaurantId, final int foodTypeId);

     /**
      * <p>
      * Removes the food from the restaurant.
      * </p>
      *
      * @param foodId Represents the id of the food
      * @return True if food is removed, false otherwise
      */
     boolean removeFood(final long foodId);

     /**
      * <p>
      * Updates the data of the current restaurant user.
      * </p>
      *
      * @param restaurantId Represents the id of the restaurant
      * @param restaurantData Represents the data of the restaurant to be updated
      * @param type Represents the type of data of the restaurant to be updated
      * @return True if data is updated, false otherwise
      */
     boolean updateRestaurantData(final long restaurantId, final String type, final String restaurantData);
}