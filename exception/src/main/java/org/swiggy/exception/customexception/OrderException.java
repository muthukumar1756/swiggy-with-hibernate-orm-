package org.swiggy.exception.customexception;

public class OrderException extends RuntimeException {
    public OrderException(final String message) {
        super(message);
    }
}
