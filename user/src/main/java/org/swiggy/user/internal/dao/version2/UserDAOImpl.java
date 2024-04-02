package org.swiggy.user.internal.dao.version2;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.swiggy.common.hibernate.QueryBuilder;
import org.swiggy.common.hibernate.SessionBuilder;
import org.swiggy.common.hibernate.SessionHandler;
import org.swiggy.common.hibernate.TransactionHandler;
import org.swiggy.user.internal.dao.UserDAO;
import org.swiggy.user.model.User;

import java.util.Optional;

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
    private SessionBuilder sessionBuilder;

    private UserDAOImpl() {
        sessionBuilder = SessionBuilder.getSessionBuilder().getSessionFactory();
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
            userDAO = new UserDAOImpl();
        }

        return userDAO;
    }

    /**
     * {@inheritDoc}
     *
     * @param user Represents the {@link User}
     * @return True if user is created, false otherwise
     */
    @Override
    public boolean createUserProfile(final User user) {
        Optional<TransactionHandler> transaction = Optional.empty();

        try (final SessionHandler session = sessionBuilder.buildSession()) {
            transaction = Optional.of(session.getTransaction());

            transaction.get().begin();
            session.save(user);
            transaction.get().commit();

            return true;
        } catch (Exception message) {
            if (transaction.isPresent()) {
                transaction.get().rollBack();
            }
            LOGGER.warn(message.getMessage());

            return false;
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
    @Override
    public Optional<User> getUser(final String userDataType, final String userData, final String password) {
        final String query = """
        select id, name, phone_number, email_id, password from users where :type = :userData and password = :password""";
        Optional<TransactionHandler> transaction = Optional.empty();

        try (final SessionHandler session = sessionBuilder.buildSession()) {
            transaction = Optional.of(session.getTransaction());

            transaction.get().begin();
            final QueryBuilder queryBuilder = session.createQuery(query, User.class);

            queryBuilder.setParameter("type", userDataType);
            queryBuilder.setParameter("userData", userData);
            queryBuilder.setParameter("password", password);
            final User user = (User) queryBuilder.getSingleResult();

            transaction.get().commit();

            if (null != user) {
                return Optional.of(user);
            }
        } catch (Exception message) {
            if (transaction.isPresent()) {
                transaction.get().rollBack();
            }
            LOGGER.warn(message.getMessage());
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
        Optional<TransactionHandler> transaction = Optional.empty();

        try (final SessionHandler session = sessionBuilder.buildSession()) {
            transaction = Optional.of(session.getTransaction());

            transaction.get().begin();
            final User user = session.get(User.class, userId);
            transaction.get().commit();

            if (null != user) {
                return Optional.of(user);
            }
        } catch (Exception message) {
            if (transaction.isPresent()) {
                transaction.get().rollBack();
            }
            LOGGER.warn(message.getMessage());
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
        final String query = "update users set :type = :userData where id = :userId";
        Optional<TransactionHandler> transaction = Optional.empty();

        try (final SessionHandler session = sessionBuilder.buildSession()) {
            transaction = Optional.of(session.getTransaction());

            transaction.get().begin();
            final QueryBuilder queryBuilder = session.createQuery(query, User.class);

            queryBuilder.setParameter("type", type);
            queryBuilder.setParameter("userData", userData);
            queryBuilder.setParameter("userId", userId);
            final long result = queryBuilder.executeUpdate();

            transaction.get().commit();

            return 0 < result;
        } catch (Exception message) {
            if (transaction.isPresent()) {
                transaction.get().rollBack();
            }
            LOGGER.warn(message.getMessage());
        }

        return false;
    }
}