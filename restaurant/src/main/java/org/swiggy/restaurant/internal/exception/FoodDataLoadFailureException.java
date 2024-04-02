package org.swiggy.restaurant.internal.exception;

import org.swiggy.exception.customexception.RestaurantException;

/**
 * <p>
 * Handles the exception when unable to load the food.
 * </p>
 */
public class FoodDataLoadFailureException extends RestaurantException {
    public FoodDataLoadFailureException(final String message) {
        super(message);
    }
}
