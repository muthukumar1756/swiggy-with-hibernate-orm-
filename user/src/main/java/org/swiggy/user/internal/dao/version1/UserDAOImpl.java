package org.swiggy.user.internal.dao.version1;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import org.swiggy.database.connection.DataBaseConnection;

import org.swiggy.user.internal.exception.UserDataLoadFailureException;
import org.swiggy.user.internal.exception.UserDataNotFoundException;
import org.swiggy.user.internal.exception.UserDataUpdateFailureException;
import org.swiggy.user.internal.dao.UserDAO;
import org.swiggy.user.model.User;

/**
 * <p>
 * Implements the data base service of the user related operation.
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public final class UserDAOImpl implements UserDAO {

    private static final Logger LOGGER = LogManager.getLogger(UserDAOImpl.class);
    private static UserDAO userDAO;
    private final Connection connection;

    private UserDAOImpl() {
        connection = DataBaseConnection.getConnection();
    }

    /**
     * <p>
     * Gets the object of the user database implementation class.
     * </p>
     *
     * @return The user database service implementation object
     */
    public static UserDAO getInstance() {
        if (null == userDAO) {
            return userDAO = new UserDAOImpl();
        }

        return userDAO;
    }

    /**
     * {@inheritDoc}
     *
     * @param user Represents the {@link User}
     * @return True if user is created, false otherwise
     */
    public boolean createUserProfile(final User user) {
        final String query = """
                insert into users (name, phone_number, email_id, password) values (?, ?, ?, ?) returning id""";

        try (final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getPhoneNumber());
            preparedStatement.setString(3, user.getEmailId());
            preparedStatement.setString(4, user.getPassword());
            final ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();
            user.setId(resultSet.getInt(1));

            return true;
        } catch (SQLException message) {
            LOGGER.error(message.getMessage());
            throw new UserDataLoadFailureException(message.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param userDataType Represents the data type of the user
     * @param userData Represents the data of the user
     * @param password Represents the password of the user
     * @return The user object
     */
    public Optional<User> getUser(final String userDataType, final String userData, final String password) {
        final String query = String.join("",
                "select id, name, phone_number, email_id, password from users where ",
                userDataType, " = ? and password = ?");

        try (final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, userData);
            preparedStatement.setString(2, password);
            final ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                final User user = new User();

                user.setId(resultSet.getInt(1));
                user.setName(resultSet.getString(2));
                user.setPhoneNumber(resultSet.getString(3));
                user.setEmailId(resultSet.getString(4));
                user.setPassword(resultSet.getString(5));

                return Optional.of(user);
            }
        } catch (SQLException message) {
            LOGGER.error(message.getMessage());
            throw new UserDataNotFoundException(message.getMessage());
        }

        return Optional.empty();
    }

    /**
     * {@inheritDoc}
     *
     * @param userId Represents the id of the user
     * @return The user object
     */
    @Override
    public Optional<User> getUserById(final long userId) {
        final String query = " select name, phone_number, email_id, password from users where id = ?";

        try (final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, userId);
            final ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                final User user = new User();

                user.setId(userId);
                user.setName(resultSet.getString(1));
                user.setPhoneNumber(resultSet.getString(2));
                user.setEmailId(resultSet.getString(3));
                user.setPassword(resultSet.getString(4));

                return Optional.of(user);
            }
        } catch (SQLException message) {
            LOGGER.error(message.getMessage());
            throw new UserDataNotFoundException(message.getMessage());
        }

        return Optional.empty();
    }

    /**
     * {@inheritDoc}
     *
     * @param userId Represents the id of {@link User}
     * @param type Represents the type of data to be updated
     * @param userData Represents the value of data to be updated
     * @return True if user data is updated, false otherwise
     */
    @Override
    public boolean updateUserProfile(final long userId, final String type, final String userData) {
        final String query = String.join("", "update users set ", type, " = ? where id = ?");

        try (final PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, userData);
            preparedStatement.setLong(2, userId);

            return 0 < preparedStatement.executeUpdate();
        } catch (SQLException message) {
            LOGGER.error(message.getMessage());
            throw new UserDataUpdateFailureException(message.getMessage());
        }
    }
}