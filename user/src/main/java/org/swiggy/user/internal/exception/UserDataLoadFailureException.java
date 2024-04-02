package org.swiggy.user.internal.exception;

import org.swiggy.exception.customexception.UserException;

/**
 * <p>
 * Handles the exception when unable to load user data.
 * </p>
 */
public class UserDataLoadFailureException extends UserException {
    public UserDataLoadFailureException(final String message) {
        super(message);
    }
}