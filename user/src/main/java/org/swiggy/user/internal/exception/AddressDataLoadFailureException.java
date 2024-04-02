package org.swiggy.user.internal.exception;

import org.swiggy.exception.customexception.UserException;

/**
 * <p>
 * Handles the exception when the address cant uploaded.
 * </p>
 */
public final class AddressDataLoadFailureException extends UserException {
    public AddressDataLoadFailureException(final String message) {
        super(message);
    }
}
