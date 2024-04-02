package org.swiggy.user.view;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.swiggy.common.hashgenerator.PasswordHashGenerator;
import org.swiggy.common.inputhandler.InputHandler;
import org.swiggy.common.inputhandler.impl.InputHandlerImpl;

import org.swiggy.validator.regexvalidator.DataValidator;

import org.swiggy.user.internal.controller.UserController;
import org.swiggy.user.model.User;
import org.swiggy.user.model.UserData;

import java.util.Optional;

/**
 * <p>
 * Handles user creation, authentication and updates.
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public final class UserView {

    private static final Logger LOGGER = LogManager.getLogger(UserView.class);
    private static UserView userView;
    private final InputHandler inputHandler;
    private final UserController userController;
    private final DataValidator dataValidator;
    private final RestaurantDataView restaurantDataView;
    private final OrderView orderView;

    private UserView() {
        inputHandler = InputHandlerImpl.getInstance();
        userController = UserController.getInstance();
        dataValidator = DataValidator.getInstance();
        restaurantDataView = RestaurantDataView.getInstance();
        orderView = OrderView.getInstance();
    }

    /**
     * <p>
     * Gets the object of the user view class.
     * </p>
     *
     * @return The user view object
     */
    public static UserView getInstance() {
        if (null == userView) {
            userView = new UserView();
        }

        return userView;
    }

    /**
     * <p>
     * Displays the main menu and gets the option for signup or login.
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
                LOGGER.warn("Invalid UserChoice");
                displayMainMenu();
            }
        }
    }

    /**
     * <p>
     * Handles the user signup process.
     * </p>
     */
    private void signUp() {
        LOGGER.info("User Signup Or Enter * To Go Back");
        final User user = new User(getName(), getPhoneNumber(), getEmailId(), getPassword());

        if (userController.createUserProfile(user)) {
            displayHomePageMenu(user.getId());
        } else {
            LOGGER.warn("User Already Exists");
            displayMainMenu();
        }
    }

    /**
     * <p>
     * Gets the valid username from the user.
     * </p>
     *
     * @return The valid username of the user
     */
    private String getName() {
        LOGGER.info("Enter Your Name");
        final String name = inputHandler.getInfo();

        isBackNavigation(name);

        if (!dataValidator.validateUserName(name)) {
            LOGGER.warn("Enter A Valid User Name");
            getName();
        }

        return name;
    }

    /**
     * <p>
     * Gets the valid mobile number from the user.
     * </p>
     *
     * @return The mobile number of the user
     */
    private String getPhoneNumber() {
        LOGGER.info("Enter Your Phone Number");
        final String phoneNumber = inputHandler.getInfo();

        isBackNavigation(phoneNumber);

        if (!dataValidator.validatePhoneNumber(phoneNumber)) {
            LOGGER.warn("Enter A Valid Phone Number");
            getPhoneNumber();
        }

        return phoneNumber;
    }

    /**
     * <p>
     * Gets the valid email from the user.
     * </p>
     *
     * @return The valid email of the user
     */
    private String getEmailId() {
        LOGGER.info("Enter Your EmailId");
        final String emailId = inputHandler.getInfo();

        isBackNavigation(emailId);

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
     * @return The validated password of the user
     */
    private String getPassword() {
        LOGGER.info("Enter Your Password");
        final String password = inputHandler.getInfo();

        isBackNavigation(password);

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
    private void isBackNavigation(final String back) {
        if ("back".equals(back)) {
            displayMainMenu();
        }
    }

    /**
     * <p>
     * Gets the user details for login process.
     * </p>
     */
     private void login() {
         LOGGER.info("""
                 Login With
                 1.Phone Number
                 2.Email Id""");
         Optional<User> user = Optional.empty();
         final int value = inputHandler.getValue();

         if (-1 == value) {
             displayMainMenu();
         }

         switch (value) {
             case 1 -> user = userController.getUser(UserData.PHONE_NUMBER, getPhoneNumber(), getPassword());
             case 2 -> user = userController.getUser(UserData.EMAIL_ID, getEmailId(), getPassword());
             default -> {
                 LOGGER.warn("Enter Valid Option");
                 login();
             }
         }

        if (user.isPresent()) {
            LOGGER.warn("User Not Registered Or Incorrect Password");
            login();
        }
        displayHomePageMenu(user.get().getId());
    }
    
    /**
     * <p>
     * Displays the home page menu of the application
     * </p>
     *
     * @param userId Represents the id of the {@link User}
     */
    public void displayHomePageMenu(final long userId) {
        LOGGER.info("""
                To Go Back Enter *
                1.Display Restaurants
                2.Edit User Profile
                3.View Orders
                4.Logout""");
        final int value = inputHandler.getValue();

        if (-1 == value) {
            displayMainMenu();
        }

        switch (value) {
            case 1 -> restaurantDataView.displayRestaurants(userId);
            case 2 -> updateUserData(userId);
            case 3 -> {
                orderView.displayOrders(userId);
                displayHomePageMenu(userId);
            }
            case 4 -> displayMainMenu();
            default -> {
                LOGGER.warn("Enter A Valid Option");
                displayHomePageMenu(userId);
            }
        }
    }

    /**
     * <p>
     * Displays the data of user.
     * </p>
     *
     * @param userId Represents the id of the {@link User}
     */
    private void displayUserData(final long userId) {
        final Optional<User> currentUser = userController.getUserById(userId);

        LOGGER.info("\nYour Current Data\n");
        LOGGER.info(String.format("User Name : %s", currentUser.get().getName()));
        LOGGER.info(String.format("Phone Number : %s", currentUser.get().getPhoneNumber()));
        LOGGER.info(String.format("Email Id : %s", currentUser.get().getEmailId()));
    }

    /**
     * <p>
     * Updates the users information based on the chosen option.
     * </p>
     *
     * @param userId Represents the id of the {@link User}
     */
    private void updateUserData(final long userId) {
        displayUserData(userId);
        LOGGER.info("""
                1.Update Name
                2.Update Phone Number
                3.Update EmailId
                4.Update Password""");
        final int value = inputHandler.getValue();

        if (-1 == value) {
            displayHomePageMenu(userId);
        }

        switch (value) {
            case 1 -> userController.updateUserData(userId, Optional.of(UserData.NAME), getName());
            case 2 -> userController.updateUserData(userId, Optional.of(UserData.PHONE_NUMBER), getPhoneNumber());
            case 3 -> userController.updateUserData(userId, Optional.of(UserData.EMAIL_ID), getEmailId());
            case 4 -> userController.updateUserData(userId, Optional.of(UserData.PASSWORD), getPassword());
            default -> {
                LOGGER.warn("Enter A Valid Option");
                updateUserData(userId);
            }
        }
        updateUserData(userId);
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