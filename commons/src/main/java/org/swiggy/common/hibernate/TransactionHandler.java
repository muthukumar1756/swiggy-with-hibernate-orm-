package org.swiggy.common.hibernate;

import org.hibernate.Transaction;

/**
 * <p>
 * Methods to handle the database transactions
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public final class TransactionHandler {

    private Transaction transaction;

    TransactionHandler(final Transaction transaction) {
        this.transaction = transaction;
    }

    /**
     * <p>
     * To begin the transaction
     * </p>
     */
    public void begin() {
        transaction.begin();
    }

    /**
     * <p>
     * To commit the transaction
     * </p>
     */
    public void commit() {
        transaction.commit();
    }

    /**
     * <p>
     * To rollback the transaction
     * </p>
     */
    public void rollBack() {
        transaction.rollback();
    }
}