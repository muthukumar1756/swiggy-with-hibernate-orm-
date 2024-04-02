package org.swiggy.user.internal.exception;

import org.swiggy.exception.customexception.UserException;

/**
 * <p>
 * Handles the exception when the user data is not found.
 * </p>
 */
public class UserDataNotFoundException extends UserException {
    public UserDataNotFoundException(final String message) {
        super(message);
    }
}