package org.swiggy.user.internal.dao;

import org.swiggy.user.model.Cart;
import org.swiggy.user.model.User;

import java.util.List;
import java.util.Optional;

/**
 * <p>
 * Provides data base service for the user cart.
 * </p>
 *
 * @author Muthu kumar v
 * @version 1.1
 */
public interface CartDAO {

    /**
     * <p>
     * Adds the selected food to the user cart.
     * </p>
     *
     * @param cart Represents the cart of the user
     * @return True if the food is added to the user cart, false otherwise
     */
    boolean addFoodToCart(final Cart cart);

    /**
     * <p>
     * Gets the cart of the current user.
     * </p>
     *
     * @param userId Represents the id of the {@link User}
     * @return The list of all foods from the user cart
     */
    Optional<List<Cart>> getCart(final long userId);

    /**
     * <p>
     * Removes the selected food from the user cart.
     * </p>
     *
     * @param cartId Represents the id of the user cart
     * @return True if the food is removed, false otherwise
     */
    boolean removeFood(final long cartId);

    /**
     * <p>
     * Remove all the foods from the user cart.
     * </p>
     *
     * @param userId Represents the id of the {@link User}
     * @return The true if the cart is cleared, false otherwise
     */
    boolean clearCart(final long userId);
}
