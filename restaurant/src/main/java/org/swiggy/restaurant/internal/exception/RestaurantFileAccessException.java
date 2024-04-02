package org.swiggy.restaurant.internal.exception;

import org.swiggy.exception.customexception.RestaurantException;

/**
 * <p>
 * Handles the exception when unable to access restaurant file.
 * </p>
 */
public class RestaurantFileAccessException extends RestaurantException {
    public RestaurantFileAccessException(final String message) {
        super(message);
    }
}
