package org.swiggy.user.internal.exception;

import org.swiggy.exception.customexception.OrderException;

public final class OrderDataNotFoundException  extends OrderException {
    public OrderDataNotFoundException(final String message) {
        super(message);
    }
}
