package org.swiggy.restaurant.internal.exception;

import org.swiggy.exception.customexception.RestaurantException;

/**
 * <p>
 * Handles the exception when unable to load the restaurant.
 * </p>
 */
public class RestaurantDataLoadFailureException extends RestaurantException {
    public RestaurantDataLoadFailureException(final String message) {
        super(message);
    }
}