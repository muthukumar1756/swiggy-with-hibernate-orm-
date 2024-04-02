package org.swiggy.common.hibernate;

import org.hibernate.query.Query;

import java.util.List;

/**
 * <p>
 * Methods to handle the query
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public final class QueryBuilder {

    private Query query;

    QueryBuilder(final Query query) {
        this.query = query;
    }

    /**
     * <p>
     * Executes the query
     * </p>
     */
    public <R> List<R> executeQuery() {
        return query.getResultList();
    }

    /**
     * <p>
     * Executes the update query
     * </p>
     */
    public int executeUpdate() {
        return query.executeUpdate();
    }

    /**
     * <p>
     * To set the query parameter
     * </p>
     */
    public void setParameter(final String name, final Object value) {
        query.setParameter(name, value);
    }

    /**
     * <p>
     * To get the single result from the query execution
     * </p>
     */
    public Object getSingleResult() {
        return query.getSingleResult();
    }
}
