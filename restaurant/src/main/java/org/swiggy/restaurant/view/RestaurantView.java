package org.swiggy.restaurant.view;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

import org.swiggy.validator.regexvalidator.DataValidator;

import org.swiggy.common.hashgenerator.PasswordHashGenerator;
import org.swiggy.common.inputhandler.InputHandler;
import org.swiggy.common.inputhandler.impl.InputHandlerImpl;

import org.swiggy.restaurant.internal.controller.RestaurantController;
import org.swiggy.restaurant.model.Food;
import org.swiggy.restaurant.model.FoodType;
import org.swiggy.restaurant.model.Restaurant;
import org.swiggy.restaurant.model.RestaurantData;

/**
 * <p>
 * Displays restaurants details and menucard of the selected restaurant.
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public final class RestaurantView {

    private static final Logger LOGGER = LogManager.getLogger(RestaurantView.class);
    private static RestaurantView restaurantView;
    private final InputHandler inputHandler;
    private final RestaurantController restaurantController;
    private final DataValidator dataValidator;

    private RestaurantView() {
        inputHandler = InputHandlerImpl.getInstance();
        restaurantController = RestaurantController.getInstance();
        dataValidator = DataValidator.getInstance();
    }

    /**
     * <p>
     * Gets the object of the restaurant view class.
     * </p>
     *
     * @return The restaurant view object
     */
    public static RestaurantView getInstance() {
        if (null == restaurantView) {
            restaurantView = new RestaurantView();
        }

        return restaurantView;
    }

    /**
     * <p>
     * Displays the main menu and gets the choice for signup or login.
     * </p>
     */
    public void displayMainMenu() {
        LOGGER.info("""
                1.Signup
                2.Login
                3.Exit""");
        final int value = inputHandler.getValue();

        switch (value) {
            case 1 -> signUp();
            case 2 -> login();
            case 3 -> exit();
            default -> {
                LOGGER.warn("Invalid Choice");
                displayMainMenu();
            }
        }
    }

    /**
     * <p>
     * Handles the signup process.
     * </p>
     */
    private void signUp() {
        LOGGER.info("Restaurant Signup Or Enter * To Go Back");
        final Restaurant restaurant = new Restaurant(getName(), getPhoneNumber(), getEmailId(), getPassword());

        if (restaurantController.createRestaurantProfile(restaurant)) {
            LOGGER.info("You Have To Add Atleast One Food From Your Restaurant");
            addFood(restaurant.getId());
            displayHomePageMenu(restaurant.getId());
        } else {
            LOGGER.warn("Restaurant Already Exists");
            displayMainMenu();
        }
    }

    /**
     * <p>
     * Gets the valid username.
     * </p>
     *
     * @return The valid username of the restaurant
     */
    private String getName() {
        LOGGER.info("Enter Your Name");
        final String name = inputHandler.getInfo();

        backOptionCheck(name);

        if (!dataValidator.validateUserName(name)) {
            LOGGER.warn("Enter A Valid Restaurant Name");
            getName();
        }

        return name;
    }

    /**
     * <p>
     * Gets the valid mobile number.
     * </p>
     *
     * @return The mobile number of the restaurant
     */
    private String getPhoneNumber() {
        LOGGER.info("Enter Your Phone Number");
        final String phoneNumber = inputHandler.getInfo();

        backOptionCheck(phoneNumber);

        if (!dataValidator.validatePhoneNumber(phoneNumber)) {
            LOGGER.warn("Enter A Valid Phone Number");
            getPhoneNumber();
        }

        return phoneNumber;
    }

    /**
     * <p>
     * Gets the valid email.
     * </p>
     *
     * @return The valid email of the restaurant
     */
    private String getEmailId() {
        LOGGER.info("Enter Your EmailId");
        final String emailId = inputHandler.getInfo();

        backOptionCheck(emailId);

        if (!dataValidator.validateEmailId(emailId)) {
            LOGGER.warn("Enter A Valid EmailId");
            getEmailId();
        }

        return emailId;
    }

    /**
     * <p>
     * Gets the password from the user after validating the password.
     * </p>
     *
     * @return The validated password of the restaurant
     */
    private String getPassword() {
        LOGGER.info("Enter Your Password");
        final String password = inputHandler.getInfo();

        backOptionCheck(password);

        if (!dataValidator.validatePassword(password)) {
            LOGGER.warn("Enter A Valid Password");
            getPassword();
        }

        return PasswordHashGenerator.getInstance().hashPassword(password);
    }

    /**
     * <p>
     * Checks for the back option
     * </p>
     *
     * @param back Represents the input to checked for the back option
     */
    private void backOptionCheck(final String back) {
        if ("back".equals(back)) {
            displayMainMenu();
        }
    }

    /**
     * <p>
     * Gets the restaurant details for login process.
     * </p>
     */
    private void login() {
        LOGGER.info("""
                 Login With
                 1.Phone Number
                 2.Email Id""");
        Optional<Restaurant> restaurant = Optional.empty();
        final int value = inputHandler.getValue();

        switch (value) {
            case 1 ->
                    restaurant = restaurantController.getRestaurant(RestaurantData.PHONE_NUMBER, getPhoneNumber(), getPassword());
            case 2 ->
                    restaurant = restaurantController.getRestaurant(RestaurantData.EMAIL_ID, getEmailId(), getPassword());
            default -> {
                LOGGER.warn("Enter Valid Option");
                login();
            }
        }

        if (restaurant.isPresent()) {
            LOGGER.warn("Restaurant Not Registered or Incorrect Password");
            login();
        }
        displayHomePageMenu(restaurant.get().getId());
    }

    /**
     * <p>
     * Displays the home page for the restaurant.
     * </p>
     *
     * @param restaurantId Represents the id of the restaurant
     */
    private void displayHomePageMenu(final long restaurantId) {
        LOGGER.info("""
                To Go Back Enter *
                1.Add Food
                2.Remove Food
                3.Edit Profile
                4.Logout""");
        final int value = inputHandler.getValue();

        if (-1 == value) {
            displayMainMenu();
        }

        switch (value) {
            case 1 -> addFood(restaurantId);
            case 2 -> removeFood(restaurantId);
            case 3 -> updateRestaurantData(restaurantId);
            case 4 -> displayMainMenu();
            default -> {
                LOGGER.info("Enter Valid Input");
                displayHomePageMenu(restaurantId);
            }
        }
    }

    /**
     * <p>
     * Gets the restaurant details for login process.
     * </p>
     *
     * @param restaurantId Represents the id of the restaurant
     * */
    private void addFood(final long restaurantId) {
        LOGGER.info("Enter The Food Name");
        final String name = inputHandler.getInfo();

        LOGGER.info("Enter The Food Rate");
        final float rate = inputHandler.getValue();

        LOGGER.info("Enter The Food Quantity");
        final int quantity = inputHandler.getValue();

        restaurantController.addFood(new Food(name, rate, getType(), quantity), restaurantId);
        displayHomePageMenu(restaurantId);
    }

    /**
     * <p>
     * Gets the Food type from the restaurant.
     * </p>
     *
     * @return The Food type id.
     */
    private FoodType getType() {
        LOGGER.info("""
                Enter the Food Type
                1.Veg
                2.NonVeg""");
        final FoodType foodType = FoodType.getTypeById(inputHandler.getValue());

        if (null == foodType) {
            LOGGER.warn("Enter A Valid Id");
            getType();
        }

        return foodType;
    }

    /**
     * <p>
     * Removes the food from the restaurant menucard.
     * </p>
     *
     * @param restaurantId Represents the id of the restaurant
     */
    private void removeFood(final long restaurantId) {
        LOGGER.info("""
                Enter Type Of Food To Be Removed
                1.VEG
                2.NONVEG
                3.VEG & NONVEG""");
        final int foodType = inputHandler.getValue();

        if (-1 == foodType) {
            displayHomePageMenu(restaurantId);
        }

        if (0 < foodType && 4 > foodType) {
            final Optional<List<Food>> menucard = getMenucard(restaurantId, foodType);

            if (menucard.isPresent()) {
                LOGGER.warn("Your Restaurant Currently Doesn't Have Any Available Items");
                displayHomePageMenu(restaurantId);
            }
            displayFoods(menucard.get());
            LOGGER.info("Enter FoodId To Remove");
            final long foodId = selectFood(restaurantId, menucard.get());

            restaurantController.removeFood(foodId);
            LOGGER.info("The Food Was Removed Successfully");
            displayHomePageMenu(restaurantId);
        } else {
            LOGGER.warn("Enter A Valid Option");
            removeFood(restaurantId);
        }
    }

    /**
     * <p>
     * Displays the menucard of the restaurant.
     * </p>
     *
     * @param menucard Represents the menucard of the restaurant
     */
    public void displayFoods(final List<Food> menucard) {
        LOGGER.info("""
                Available Items:
                ID | Name | Rate | Category""");

        for (final Food food : menucard) {
            LOGGER.info(String.format("%d %s %.2f %s", menucard.indexOf(food) + 1,
                    food.getName(), food.getRate(), food.getType()));
        }
    }

    /**
     * <p>
     * Gets the selection of food from the user.
     * </p>
     *
     * @param restaurantId Represents the id of the restaurant
     * @param menucard Represents the menucard of the restaurant
     */
    private long selectFood(final long restaurantId, final List<Food> menucard) {
        final int selectedIndex = inputHandler.getValue();

        if (-1 == selectedIndex) {
            displayHomePageMenu(restaurantId);
        }
        final int foodNumber = selectedIndex - 1;

        if (0 <= foodNumber && menucard.size() >= foodNumber) {
            final Food selectedFood = menucard.get(foodNumber);

            return selectedFood.getId();
        } else {
            LOGGER.warn("Enter A Valid Option From The Menucard");
            return selectFood(restaurantId, menucard);
        }
    }

    /**
     * <p>
     * Gets all the restaurants.
     * </p>
     *
     * @return The map having all the restaurants
     */
    public Optional<List<Restaurant>> getRestaurants() {
        return restaurantController.getRestaurants();
    }

    /**
     * <p>
     * Gets the menucard from the restaurant.
     * </p>
     *
     * @param restaurantId Represents the id of the current {@link Restaurant}
     */
    public Optional<List<Food>> getMenucard(final long restaurantId, final int foodType) {
        return restaurantController.getMenuCard(restaurantId, foodType);
    }

    /**
     * <p>
     * Gets the quantity of food.
     * </p>
     *
     * @param foodId Represents the id of the food
     * @return Available quantity of food from the restaurant
     */
    public Optional<Integer> getFoodQuantity(final long foodId) {
        return restaurantController.getFoodQuantity(foodId);
    }

    /**
     * <p>
     * Displays the data of restaurant.
     * </p>
     *
     * @param restaurantId Represents the id of the restaurant
     */
    private void displayRestaurantData(final long restaurantId) {
        final Optional<Restaurant> restaurant = restaurantController.getRestaurantById(restaurantId);

        LOGGER.info("\nYour Current Data\n");
        LOGGER.info(String.format("Name : %s", restaurant.get().getName()));
        LOGGER.info(String.format("Phone Number : %s", restaurant.get().getPhoneNumber()));
        LOGGER.info(String.format("Email Id : %s", restaurant.get().getEmailId()));
    }

    /**
     * <p>
     * Updates the restaurant information based on the chosen option.
     * </p>
     *
     * @param restaurantId Represents the id of the restaurant
     */
    private void updateRestaurantData(final long restaurantId) {
        displayRestaurantData(restaurantId);
        LOGGER.info("""
                1.Update Name
                2.Update Phone Number
                3.Update EmailId
                4.Update Password""");
        final int value = inputHandler.getValue();

        if (-1 == value) {
            displayHomePageMenu(restaurantId);
        }

        switch (value) {
            case 1 -> restaurantController.updateRestaurantData(restaurantId, getName(), RestaurantData.NAME);
            case 2 -> restaurantController.updateRestaurantData(restaurantId, getPhoneNumber(),
                    RestaurantData.PHONE_NUMBER);
            case 3 -> restaurantController.updateRestaurantData(restaurantId, getEmailId(), RestaurantData.EMAIL_ID);
            case 4 -> restaurantController.updateRestaurantData(restaurantId, getPassword(), RestaurantData.PASSWORD);
            default -> {
                LOGGER.warn("Enter A Valid Option");
                updateRestaurantData(restaurantId);
            }
        }
        updateRestaurantData(restaurantId);
    }

    /**
     * <p>
     * Exits from the application.
     * </p>
     */
    private void exit() {
        System.exit(0);
    }
}