package org.swiggy.database.exception;

import org.swiggy.exception.customexception.DataBaseConnectionException;

/**
 * <p>
 * Handles the exception when unable to get database connection.
 * </p>
 */
public final class DatabaseConnectionFailureException extends DataBaseConnectionException {
    public DatabaseConnectionFailureException(final String message) {
        super(message);
    }
}

