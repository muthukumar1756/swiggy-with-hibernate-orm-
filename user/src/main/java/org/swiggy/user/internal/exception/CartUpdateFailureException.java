package org.swiggy.user.internal.exception;

import org.swiggy.exception.customexception.CartException;

/**
 * <p>
 * Handles the exception when unable to update the user cart.
 * </p>
 */
public final class CartUpdateFailureException extends CartException {
    public CartUpdateFailureException(final String message) {
        super(message);
    }
}
