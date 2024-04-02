package org.swiggy.common.exception;

import org.swiggy.exception.customexception.DataConversionException;

/**
 * <p>
 * Handles the exception when unable to process the data with jackson json provider.
 * </p>
 */
public final class JacksonDataConversionException extends DataConversionException {
    public JacksonDataConversionException(final String message) {
        super(message);
    }
}
