package org.swiggy.restaurant.initializer;

import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.swiggy.restaurant.internal.controller.RestaurantController;
import org.swiggy.restaurant.internal.exception.RestaurantFileAccessException;
import org.swiggy.restaurant.internal.exception.FoodDataLoadFailureException;
import org.swiggy.restaurant.model.Food;
import org.swiggy.restaurant.model.FoodType;
import org.swiggy.restaurant.model.Restaurant;

/**
 * <p>
 * Initializes the data of restaurants and foods.
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public final class RestaurantInitializer {

    private static final Logger LOGGER = LogManager.getLogger(RestaurantInitializer.class);
    private static RestaurantInitializer restaurantInitializer;
    private final RestaurantController restaurantController;

    private RestaurantInitializer() {
        restaurantController = RestaurantController.getInstance();
    }
    
    /**
     * <p>
     * Gets the object of the restaurant initializer class.
     * </p>
     *
     * @return The restaurant initializer object
     */
    public static RestaurantInitializer getInstance() {
        if (null == restaurantInitializer) {
            restaurantInitializer = new RestaurantInitializer();
        }

        return restaurantInitializer;
    }

    /**
     * <p>
     * Loads the data of the restaurant.
     * </p>
     */
    public void loadRestaurantsData() {
        try (final InputStream inputStream = ClassLoader.getSystemResourceAsStream("Restaurants.properties")) {
            final Properties properties = new Properties();
            final List<Restaurant> restaurants = new ArrayList<>();

            properties.load(inputStream);

            for (final Object key : properties.keySet()) {
                final String name = properties.getProperty((String) key);
                final Restaurant restaurant = new Restaurant();

                restaurant.setName(name);
                restaurants.add(restaurant);
            }

            if (restaurantController.loadRestaurantList(restaurants)) {
                loadMenuCardData(restaurants);
            }
        } catch (IOException message) {
            LOGGER.error(message.getMessage());
            throw new RestaurantFileAccessException(message.getMessage());
        }
    }

    /**
     * <p>
     * Creates food objects from loaded restaurant paths.
     * </p>
     *
     * @param restaurants Represents all the restaurants
     */
    private void loadMenuCardData(final List<Restaurant> restaurants) {
        final Map<Food, Long> menuCard = new HashMap<>();

        for (final Restaurant restaurant : restaurants) {
            final String restaurantDataPath = String.join("", restaurant.getName().toLowerCase(),
                    ".properties");

            try (final InputStream inputStream = ClassLoader.getSystemResourceAsStream(restaurantDataPath)) {
                final Properties properties = new Properties();

                properties.load(inputStream);

                for (final Object key : properties.keySet()) {
                    final String value = properties.getProperty(String.valueOf(key));
                    final String[] restaurantProperty = value.split(",");
                    final String name = restaurantProperty[0];
                    final int rate = Integer.parseInt(restaurantProperty[1]);
                    final String type = restaurantProperty[2];
                    final int foodQuantity = Integer.parseInt(restaurantProperty[3]);

                    if (type.equalsIgnoreCase(FoodType.VEG.name())) {
                        menuCard.put(new Food(name, rate, FoodType.VEG, foodQuantity), restaurant.getId());
                    } else {
                        menuCard.put(new Food(name, rate, FoodType.NONVEG, foodQuantity), restaurant.getId());
                    }
                }
            } catch (IOException message) {
                LOGGER.error(message.getMessage());
                throw new FoodDataLoadFailureException(message.getMessage());
            }
        }
        restaurantController.loadMenuCard(menuCard);
    }
}