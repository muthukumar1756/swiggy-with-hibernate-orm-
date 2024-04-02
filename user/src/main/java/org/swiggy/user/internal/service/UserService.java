package org.swiggy.user.internal.service;

import org.swiggy.user.model.User;
import org.swiggy.user.model.UserData;

import java.util.Optional;

/**
 * <p>
 * Provides the services for the user.
 * </p>
 *
 * @author Muthu kumar v
 * @version 1.0
 */
public interface UserService {

    /**
     * <p>
     * Creates the new user profile.
     * </p>
     *
     * @param user Represents the {@link User}
     * @return True if user is created, false otherwise
     */
    boolean createUserProfile(final User user);

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
    Optional<User> getUser(final Optional<UserData> userDataType, final String userData, final String password);

    /**
     * <p>
     * Gets the user profile if the id matches.
     * </p>
     *
     * @param userId Represents the password of the user
     * @return The user object
     */
    Optional<User> getUserById(final long userId);

    /**
     * <p>
     * Updates the data of the user.
     * </p>
     *
     * @param userId Represents the id of {@link User}
     * @param userData Represents the data to be updated
     * @param type Represents the type of data to be updated
     * @return True if user data is updated, false otherwise
     */
    boolean updateUserData(final long userId, final Optional<UserData> type, final String userData);
}
