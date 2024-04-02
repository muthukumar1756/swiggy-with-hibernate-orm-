package org.swiggy.user.internal.service.impl;

import org.swiggy.common.hashgenerator.PasswordHashGenerator;
import org.swiggy.user.internal.dao.UserDAO;
import org.swiggy.user.internal.dao.version2.UserDAOImpl;
import org.swiggy.user.model.User;
import org.swiggy.user.internal.service.UserService;
import org.swiggy.user.model.UserData;

import java.util.Optional;

/**
 * <p>
 * Implements the service of the user related operation.
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public final class UserServiceImpl implements UserService {

    private static UserService userService;
    private final UserDAO userDAO;

    private UserServiceImpl() {
        userDAO = UserDAOImpl.getInstance();
    }

    /**
     * <p>
     * Gets the object of the user service implementation class.
     * </p>
     *
     * @return The user service implementation class object
     */
    public static UserService getInstance() {
        if (null == userService) {
            userService = new UserServiceImpl();
        }

        return userService;
    }

    /**
     * {@inheritDoc}
     *
     * @param user Represents the {@link User}
     * @return True if user is created, false otherwise
     */
    @Override
    public boolean createUserProfile(final User user) {
        final String hashPassword = PasswordHashGenerator.getInstance().hashPassword(user.getPassword());

        user.setPassword(hashPassword);

       return userDAO.createUserProfile(user);
    }

    /**
     * {@inheritDoc}
     *
     * @param userDataType Represents the data type of the user
     * @param userData Represents the data of the user
     * @param password Represents the password of the user
     * @return The user object
     */
    @Override
    public Optional<User> getUser(final Optional<UserData> userDataType, final String userData, final String password) {
        final String hashPassword = PasswordHashGenerator.getInstance().hashPassword(password);

        return userDAO.getUser(userDataType.get().name(), userData, hashPassword);
    }

    /**
     * {@inheritDoc}
     *
     * @param userId Represents the password of the user
     * @return The user object
     */
    @Override
    public Optional<User> getUserById(final long userId) {
        return userDAO.getUserById(userId);
    }

    /**
     * {@inheritDoc}
     *
     * @param userId Represents the id of {@link User}
     * @param userData Represents the data to be updated
     * @param type Represents the type of data to be updated
     * @return True if user data is updated, false otherwise
     */
    @Override
    public boolean updateUserData(final long userId, final Optional<UserData> type, final String userData){
        return userDAO.updateUserProfile(userId, type.get().name(), userData);
    }
}