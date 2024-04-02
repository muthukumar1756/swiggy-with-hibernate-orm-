package org.swiggy.restaurant.internal.restcontroller;

import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.PathParam;

import java.util.List;
import java.util.Optional;

import org.swiggy.common.json.JsonFactory;
import org.swiggy.common.json.JsonArray;
import org.swiggy.common.json.JsonObject;

import org.swiggy.restaurant.internal.service.RestaurantService;
import org.swiggy.restaurant.internal.service.impl.RestaurantServiceImpl;
import org.swiggy.restaurant.model.Restaurant;
import org.swiggy.restaurant.model.Food;
import org.swiggy.restaurant.model.RestaurantData;

import org.swiggy.validator.hibernatevalidator.ValidatorFactory;
import org.swiggy.validator.validatorgroup.Restaurant.LoginRestaurantValidation;
import org.swiggy.validator.validatorgroup.Restaurant.PostRestaurantValidator;
import org.swiggy.validator.validatorgroup.Restaurant.GetRestaurantValidator;
import org.swiggy.validator.validatorgroup.Restaurant.PutRestaurantValidator;
import org.swiggy.validator.validatorgroup.food.DeleteFoodValidator;
import org.swiggy.validator.validatorgroup.food.PostFoodValidator;
import org.swiggy.validator.validatorgroup.food.GetFoodValidator;

/**
 * <p>
 * Handles the restaurant related operation and responsible for receiving input through rest api and processing it.
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
@Path("/restaurant")
public final class RestaurantController {

    private static RestaurantController restaurantController;
    private final RestaurantService restaurantService;
    private final JsonFactory jsonFactory;
    private final ValidatorFactory validatorFactory;

    private RestaurantController() {
        restaurantService = RestaurantServiceImpl.getInstance();
        jsonFactory = JsonFactory.getInstance();
        validatorFactory = ValidatorFactory.getInstance();
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
     * Creates the new restaurant profile.
     * </p>
     *
     * @param restaurant represents the data of the restaurant
     * @return byte array of json object
     */
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public byte[] createRestaurantProfile(final Restaurant restaurant) {
        final JsonArray jsonViolations = validatorFactory.validate(restaurant, PostRestaurantValidator.class);

        if (!jsonViolations.isEmpty()) {
            return jsonViolations.asBytes();
        }
        final JsonObject jsonObject = jsonFactory.createObjectNode();

        if (restaurantService.createRestaurantProfile(restaurant)) {
            return jsonObject.put("Status", "Successful restaurant profile was created").asBytes();
        }

        return jsonObject.put("Status", "Unsuccessful restaurant profile creation failed").asBytes();
    }

    /**
     * <p>
     * Gets the restaurant if the id matches.
     * </p>
     *
     * @param restaurantId Represents the id of the restaurant
     * @return byte array of json object
     */
    @Path("/{restaurantId}")
    @GET
    @Produces("application/json")
    public byte[] getRestaurantById(@PathParam("restaurantId") final long restaurantId) {
        final Restaurant restaurantPojo = new Restaurant();

        restaurantPojo.setId(restaurantId);
        final JsonArray jsonViolations = validatorFactory.validate(restaurantPojo, GetRestaurantValidator.class);

        if (!jsonViolations.isEmpty()) {
            return jsonViolations.asBytes();
        }
        final JsonObject jsonObject = jsonFactory.createObjectNode();
        final Optional<Restaurant> restaurant = restaurantService.getRestaurantById(restaurantId);

        if (restaurant.isPresent()) {
            return jsonObject.put("Status", "Enter a valid restaurant id").asBytes();
        }

        return jsonObject.build(restaurant).asBytes();
    }

    /**
     * <p>
     * Gets the restaurant profile if the phone_number and password matches.
     * </p>
     *
     * @param restaurantData Represents the data of the restaurant
     * @return byte array of json object
     */
    @Path("/login")
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public byte[] restaurantLogin(final String restaurantData) {
        final JsonArray jsonViolations = jsonFactory.createArrayNode();
        final JsonObject jsonObject = jsonFactory.createObjectNode().toJsonNode(restaurantData);
        final String loginType = jsonObject.get("type");
        Optional<RestaurantData> type = Optional.empty();

        try {
            type = Optional.of(RestaurantData.valueOf(loginType));
        } catch (IllegalArgumentException exception) {
            jsonViolations.add(jsonFactory.createObjectNode().put("Error", "Enter valid type of data to login"));
        }
        final String value = jsonObject.get("restaurantdata");
        final String password = jsonObject.get("password");
        final Restaurant restaurant = new Restaurant();

        restaurant.setPassword(password);

        if (type.isPresent()) {
            switch (type.get()) {
                case PHONE_NUMBER -> restaurant.setPhoneNumber(value);
                case EMAIL_ID -> restaurant.setEmailId(value);
            }
        }
        jsonViolations.addArray(validatorFactory.validate(restaurant, LoginRestaurantValidation.class));

        if (!jsonViolations.isEmpty()) {
            return jsonViolations.asBytes();
        }
        final Optional<Restaurant> restaurantPojo = restaurantService.getRestaurant(type, value, password);

        if (restaurantPojo.isPresent()) {
            final String message = String.join("","Restaurant login successful welcome ",
                    restaurantPojo.get().getName());

            return jsonFactory.createObjectNode().put("Status", message).asBytes();
        }

        return jsonFactory.createObjectNode().put("Status", "Restaurant login failed").asBytes();
    }

    /**
     * <p>
     * Gets all the restaurants.
     * </p>
     *
     * @return byte array of json object
     */
    @GET
    @Produces("application/json")
    public byte[] getAllRestaurants() {
        final Optional<List<Restaurant>> restaurants = restaurantService.getRestaurants();
        final JsonArray jsonArray = jsonFactory.createArrayNode();

        if (restaurants.isPresent()) {
            return jsonFactory.createObjectNode().put("Status", "No available restaurants").asBytes();
        }

        return jsonArray.build(restaurants).asBytes();
    }

    /**
     * <p>
     * Gets the menucard from the restaurant
     * </p>
     *
     * @param restaurantId Represents the id of the restaurant
     * @param menuCardTypeId Represents the id of the food type.
     * @return byte array of json object
     */
    @Path("/{restaurantId}/{foodTypeId}")
    @GET
    @Produces("application/json")
    public byte[] getMenuCard(@PathParam("restaurantId") final long restaurantId,
                                  @PathParam("foodTypeId") final int menuCardTypeId) {
        final Restaurant restaurant = new Restaurant();

        restaurant.setId(restaurantId);
        final JsonArray jsonViolations = validatorFactory.validate(restaurant, GetFoodValidator.class);

        if (!jsonViolations.isEmpty()) {
            return jsonViolations.asBytes();
        }
        final Optional<List<Food>> menuCard = restaurantService.getMenuCard(restaurantId, menuCardTypeId);

        if (menuCard.isPresent()) {
            return jsonFactory.createObjectNode().put("Status", "No available foods or enter valid restaurant id")
                    .asBytes();
        }

        return jsonFactory.createArrayNode().build(menuCard).asBytes();
    }

    /**
     * <p>
     * Adds food to the restaurant.
     * </p>
     *
     * @param food Represents the food
     * @param restaurantId Represents the id of the restaurant
     * @return byte array of json object
     */
    @Path("/{restaurantId}")
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public byte[] addFood(final Food food, @PathParam("restaurantId") final long restaurantId) {
        final Restaurant restaurant = new Restaurant();

        restaurant.setId(restaurantId);
        final JsonArray jsonViolations = validatorFactory.validate(restaurant, GetRestaurantValidator.class);

        if (!jsonViolations.isEmpty()) {
            return jsonViolations.asBytes();
        }
        jsonViolations.addArray(validatorFactory.validate(food, PostFoodValidator.class));

        if (!jsonViolations.isEmpty()) {
            return jsonViolations.asBytes();
        }
        final JsonObject jsonObject = jsonFactory.createObjectNode();

        if (restaurantService.addFood(food, restaurantId)) {
            return jsonObject.put("Status", "Successful food was added").asBytes();
        }

        return jsonObject.put("Status", "Unsuccessful adding food was failed").asBytes();
    }

    /**
     * <p>
     * Removes the food from the restaurant.
     * </p>
     *
     * @param foodId Represents the id of the food
     * @return byte array of json object
     */
    @Path("/{foodId}")
    @DELETE
    @Produces("application/json")
    public byte[] removeFood(@PathParam("foodId") final long foodId) {
        final Food food = new Food();

        food.setId(foodId);
        final JsonArray jsonViolations = validatorFactory.validate(food, DeleteFoodValidator.class);

        if (!jsonViolations.isEmpty()) {
            return jsonViolations.asBytes();
        }
        final JsonObject jsonObject = jsonFactory.createObjectNode();

        if (restaurantService.removeFood(foodId)) {
            return jsonObject.put("Status", "Successful food was removed").asBytes();
        }

        return jsonObject.put("Status", "Unsuccessful removing food was failed").asBytes();
    }

    /**
     * <p>
     * Updates the data of the restaurant.
     * </p>
     *
     * @param restaurantId Represents the id of the restaurant
     * @param restaurantData Represents the data of the restaurant to be updated
     * @return byte array of json object
     */
    @Path("/{restaurantId}")
    @PUT
    @Consumes("application/json")
    @Produces("application/json")
    public byte[] updateRestaurantData(@PathParam("restaurantId") final long restaurantId,
                                     final String restaurantData)  {
        final JsonArray jsonViolations = jsonFactory.createArrayNode();
        final JsonObject jsonObject = jsonFactory.createObjectNode().toJsonNode(restaurantData);
        final String typeString = jsonObject.get("type");
        Optional<RestaurantData> type = Optional.empty();

        try {
            type = Optional.of(RestaurantData.valueOf(typeString));
        } catch (IllegalArgumentException exception) {
            jsonViolations.add(jsonFactory.createObjectNode()
                    .put("Error", "Enter valid type of data to be updated"));
        }
        final String updateValue = jsonObject.get("restaurantdata");
        final Restaurant restaurant = new Restaurant();

        restaurant.setId(restaurantId);

        if (type.isPresent()) {
            switch (type.get()) {
                case NAME -> restaurant.setName(updateValue);
                case PASSWORD -> restaurant.setPassword(updateValue);
                case EMAIL_ID -> restaurant.setEmailId(updateValue);
                case PHONE_NUMBER -> restaurant.setPhoneNumber(updateValue);
            }
        }
        jsonViolations.addArray(validatorFactory.validate(restaurant, PutRestaurantValidator.class));

        if (!jsonViolations.isEmpty()) {
            return jsonViolations.asBytes();
        }

        if (restaurantService.updateRestaurantData(restaurantId, updateValue, type)) {
            return jsonFactory.createObjectNode().put("Status", "Successful restaurant profile updated").asBytes();
        }

        return jsonFactory.createObjectNode().put("Status", "Unsuccessful restaurant profile updation failed").
                asBytes();
    }
}