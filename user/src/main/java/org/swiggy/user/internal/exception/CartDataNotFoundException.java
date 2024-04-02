package org.swiggy.user.internal.exception;

import org.swiggy.exception.customexception.CartException;

/**
 * <p>
 * Handles the exception when the user cart is not found.
 * </p>
 */
public final class CartDataNotFoundException extends CartException {
    public CartDataNotFoundException(final String message) {
        super(message);
    }
}
