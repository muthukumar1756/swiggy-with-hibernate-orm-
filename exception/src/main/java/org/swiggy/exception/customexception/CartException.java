package org.swiggy.exception.customexception;

public class CartException extends RuntimeException {
    public CartException(final String message) {
        super(message);
    }
}