package org.swiggy.restaurant.internal.exception;

import org.swiggy.exception.customexception.RestaurantException;

/**
 * <p>
 * Handles the exception when unable to access the food count.
 * </p>
 */
public class FoodCountAccessException extends RestaurantException {
    public FoodCountAccessException(final String message) {
        super(message);
    }
}
