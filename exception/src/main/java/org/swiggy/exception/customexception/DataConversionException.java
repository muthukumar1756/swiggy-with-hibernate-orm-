package org.swiggy.exception.customexception;

public class DataConversionException extends RuntimeException {
    public DataConversionException(final String message) {
        super(message);
    }
}