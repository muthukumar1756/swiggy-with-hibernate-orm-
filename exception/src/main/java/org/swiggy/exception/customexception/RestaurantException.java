package org.swiggy.exception.customexception;

public class RestaurantException extends RuntimeException {
    public RestaurantException(final String message) {
        super(message);
    }
}
