package org.swiggy.common.exception;

import org.swiggy.exception.customexception.UserException;

/**
 * <p>
 * Handles the exception of algorithm not found while hashing the user password.
 * </p>
 */
public final class HashAlgorithmNotFoundException extends UserException {
    public HashAlgorithmNotFoundException(final String message) {
        super(message);
    }
}
