package org.swiggy.user.internal.controller;

import org.swiggy.user.model.User;
import org.swiggy.user.internal.service.UserService;
import org.swiggy.user.internal.service.impl.UserServiceImpl;
import org.swiggy.user.model.UserData;

import java.util.Optional;

/**
 * <p>
 * Handles the user related operation and responsible for processing user input.
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public class UserController {

    private static UserController userController;
    private final UserService userService;

    private UserController() {
        userService = UserServiceImpl.getInstance();
    }

    /**
     * <p>
     * Gets the object of the user controller class.
     * </p>
     *
     * @return The user controller class object
     */
    public static UserController getInstance() {
        if (null == userController) {
            userController = new UserController();
        }

        return userController;
    }

    /**
     * <p>
     * Creates the new user profile.
     * </p>
     *
     * @param user Represents the {@link User}
     * @return True if user is created, false otherwise
     */
    public boolean createUserProfile(final User user) {
        return userService.createUserProfile(user);
    }

    /**
     * <p>
     * Gets the user profile if the phone_number and password matches.
     * </p>
     *
     * @param userDataType Represents the data type of the user
     * @param userData Represents the data of the user
     * @param password Represents the password of the user
     * @return The user object
     */
    public Optional<User> getUser(final UserData userDataType, final String userData, final String password) {
        return userService.getUser(Optional.of(userDataType), userData, password);
    }

    /**
     * <p>
     * Gets the user profile if the id matches.
     * </p>
     *
     * @param userId Represents the password of the user
     * @return The user object
     */
    public Optional<User> getUserById(final long userId) {
        return userService.getUserById(userId);
    }

    /**
     * <p>
     * Updates the data of the user.
     * </p>
     *
     * @param userId Represents the id of {@link User}
     * @param userData Represents the data to be updated
     * @param type Represents the type of data to be updated
     */
    public void updateUserData(final long userId, final Optional<UserData> type, final String userData) {
        userService.updateUserData(userId, type, userData);
    }
}
