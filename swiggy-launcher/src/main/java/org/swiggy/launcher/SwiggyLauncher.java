package org.swiggy.launcher;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.swiggy.common.inputhandler.InputHandler;
import org.swiggy.common.inputhandler.impl.InputHandlerImpl;

import org.swiggy.restaurant.initializer.RestaurantInitializer;
import org.swiggy.restaurant.view.RestaurantView;

import org.swiggy.user.view.UserView;

/**
 * <p>
 * Activates the functioning of the swiggy application
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public final class SwiggyLauncher {

    private static final Logger LOGGER = LogManager.getLogger(SwiggyLauncher.class);
    private static SwiggyLauncher swiggyLauncher;
    private final InputHandler inputHandler;
    private final RestaurantView restaurantView;
    private final UserView userView;

    private SwiggyLauncher() {
        inputHandler = InputHandlerImpl.getInstance();
        userView = UserView.getInstance();
        restaurantView = RestaurantView.getInstance();
    }

    public static SwiggyLauncher getInstance() {
        if (null == swiggyLauncher) {
            swiggyLauncher = new SwiggyLauncher();
        }

        return swiggyLauncher;
    }

    /**
     * <p>
     * selects the role of the person.
     * </p>
     */
    public void launch() {
        LOGGER.info("""
                Welcome To Swiggy
                1.Restaurant
                2.User""");

        switch (inputHandler.getValue()) {
            case 1 -> restaurantView.displayMainMenu();
            case 2 -> userView.displayMainMenu();
            default -> {
                LOGGER.warn("Enter A Valid Option");
                launch();
            }
        }
    }
}