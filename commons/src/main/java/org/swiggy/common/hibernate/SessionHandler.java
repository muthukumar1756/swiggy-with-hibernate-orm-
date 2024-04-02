package org.swiggy.common.hibernate;

import org.hibernate.Session;

import java.io.Serializable;

/**
 * <p>
 * Methods to handle the database session
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public final class SessionHandler implements AutoCloseable {

    private Session session;

    SessionHandler(final Session session) {
        this.session = session;
    }

    /**
     * <p>
     *  Gets the transaction instance associated with the session.
     * </p>
     *
     * @return The transaction object
     */
    public TransactionHandler getTransaction() {
        return new TransactionHandler(session.getTransaction());
    }

    /**
     * <p>
     * To save the entity in the database
     * </p>
     */
    public void save(final Object object) {
        session.persist(object);
    }

    /**
     * <p>
     * To get the entity from the database
     * </p>
     */
    public <T> T get(final Class<T> entityType, final Object id) {
        return session.get(entityType, (Serializable) id);
    }

    /**
     * <p>
     * To create the query
     * </p>
     */
    public <R> QueryBuilder createQuery(final String query, final Class<R> resultClass) {
        return new QueryBuilder(session.createQuery(query, resultClass));
    }

    /**
     * <p>
     * To create the query
     * </p>
     */
    public QueryBuilder createQuery(final String query) {
        return new QueryBuilder(session.createQuery(query));
    }

    /**
     * <p>
     * To close the session
     * </p>
     */
    @Override
    public void close() {
        session.close();
    }
}