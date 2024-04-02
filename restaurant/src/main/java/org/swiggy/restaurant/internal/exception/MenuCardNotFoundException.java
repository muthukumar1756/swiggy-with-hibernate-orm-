package org.swiggy.restaurant.internal.exception;

import org.swiggy.exception.customexception.RestaurantException;

/**
 * <p>
 * Handles the exception when unable to found the menucard of the selected restaurant.
 * </p>
 */
public class MenuCardNotFoundException extends RestaurantException {
    public MenuCardNotFoundException(final String message) {
        super(message);
    }
}
