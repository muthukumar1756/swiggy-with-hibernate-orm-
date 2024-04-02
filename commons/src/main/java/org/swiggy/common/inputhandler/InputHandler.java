package org.swiggy.common.inputhandler;

/**
 * <p>
 * Represents the commonly used input handling methods in the application.
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public interface InputHandler {

    /**
     * <p>
     * Gets and process the value .
     * </p>
     *
     * @return The given Value
     */
     int getValue();

    /**
     * <p>
     * Gets and process the information.
     * </p>
     *
     * @return The given information
     */
    String getInfo();

    /**
     * Validates the input for the back option.
     *
     * @param back The given back string
     * @return True if back condition is satisfied, false otherwise
     */
    boolean isBackButton(final String back);
}