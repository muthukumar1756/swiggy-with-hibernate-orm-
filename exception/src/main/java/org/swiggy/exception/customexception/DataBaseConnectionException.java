package org.swiggy.exception.customexception;

public class DataBaseConnectionException extends RuntimeException {
    public DataBaseConnectionException(final String message) {
        super(message);
    }
}
