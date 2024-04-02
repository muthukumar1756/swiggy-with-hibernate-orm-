package org.swiggy.user.internal.exception;

import org.swiggy.exception.customexception.UserException;

/**
 * <p>
 * Handles the exception when the user data cant be updated.
 * </p>
 */
public class UserDataUpdateFailureException extends UserException {
    public UserDataUpdateFailureException(final String message) {
        super(message);
    }
}
