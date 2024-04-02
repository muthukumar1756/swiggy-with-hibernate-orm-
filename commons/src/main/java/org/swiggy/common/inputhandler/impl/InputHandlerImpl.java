package org.swiggy.common.inputhandler.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.swiggy.common.inputhandler.InputHandler;

import java.util.Scanner;

/**
 * <p>
 * Represents the commonly used input handling methods in the application.
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public final class InputHandlerImpl implements InputHandler {

    private static final Logger LOGGER = LogManager.getLogger(InputHandlerImpl.class);
    private static Scanner scanner;
    private static InputHandler inputHandler;

    private InputHandlerImpl() {
        scanner = getScannerInstance();
    }

    /**
     * <p>
     * Gets the object of the input handler implementation class.
     * </p>
     *
     * @return The input handler implementation object
     */
    public static InputHandler getInstance() {
        if (null == inputHandler) {
            inputHandler = new InputHandlerImpl();
        }

        return inputHandler;
    }

    /**
     * <p>
     * Gets the scanner object.
     * </p>
     *
     * @return The scanner object
     */
    private Scanner getScannerInstance() {
        if (null == scanner) {
            scanner = new Scanner(System.in);
        }

        return scanner;
    }

    /**
     * {@inheritDoc}
     *
     * @return The value give by the user
     */
    @Override
    public int getValue() {
        while (true) {
            final String value = scanner.nextLine().trim();

            if (isBackButton(value)) {
                return -1;
            } else {
                try {
                    final int intValue = Integer.parseInt(value);
                    if (0 <= intValue) {

                        return intValue;
                    } else {
                        LOGGER.warn("Enter a non-negative integer.");
                    }
                } catch (NumberFormatException e) {
                    LOGGER.warn("Enter a valid integer.");
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return The information given by the user
     */
    @Override
    public String getInfo() {
        final String info = scanner.nextLine().trim();

        if (isBackButton(info)) {
            return "back";
        } else {
            return info;
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param back The back choice of the user
     * @return True if back condition is satisfied, false otherwise
     */
    @Override
    public boolean isBackButton(final String back) {
        return "*".equals(back);
    }
}
