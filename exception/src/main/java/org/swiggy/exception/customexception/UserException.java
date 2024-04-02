package org.swiggy.exception.customexception;

public class UserException extends RuntimeException {
    public UserException(final String message) {
        super(message);
    }
}
