package org.swiggy.common.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;

/**
 * <p>
 * Gets the session factory instance to work with session objects
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public final class SessionBuilder {

    private static SessionBuilder sessionBuilder;
    private SessionFactory sessionFactory;

    private SessionBuilder() {
    }

    private SessionBuilder(final SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * <p>
     *  Get the session builder instance.
     * </p>
     *
     * @return The session builder instance
     */
    public static SessionBuilder getSessionBuilder() {
        if (null == sessionBuilder) {
            sessionBuilder = new SessionBuilder();
        }

        return sessionBuilder;
    }

    /**
     * <p>
     *  Gets the session factory instance.
     * </p>
     *
     * @return The session factory instance
     */
    public SessionBuilder getSessionFactory() {
        if (null == sessionFactory) {
            final Configuration configuration = new Configuration()
                    .setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect")
                    .setProperty("hibernate.connection.driver_class", "org.postgresql.Driver")
                    .setProperty("hibernate.connection.url", "jdbc:postgresql://localhost/com.swiggy")
                    .setProperty("hibernate.connection.username", "postgres")
                    .setProperty("hibernate.connection.password", "123")
                    .setProperty("hibernate.show_sql", "true");
            sessionFactory = configuration.buildSessionFactory();
        }

        return new SessionBuilder(sessionFactory);
    }

    /**
     * <p>
     * Opens the session
     * </p>
     *
     * @return The Session object
     */
    public SessionHandler buildSession() {
        final Session session = sessionFactory.openSession();

        return new SessionHandler(session);
    }
}