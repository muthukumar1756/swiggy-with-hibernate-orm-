package org.swiggy.user.internal.service.impl;

import org.swiggy.user.internal.dao.CartDAO;
import org.swiggy.user.internal.dao.version2.CartDAOImpl;
import org.swiggy.user.model.Cart;
import org.swiggy.user.model.User;
import org.swiggy.user.internal.service.CartService;

import java.util.List;
import java.util.Optional;

/**
 * <p>
 * Implements the service of the user cart related operation.
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public final class CartServiceImpl implements CartService {

    private static CartService cartService;
    private final CartDAO cartDAO;

    private CartServiceImpl() {
        cartDAO = CartDAOImpl.getInstance();
    }

    /**
     * <p>
     * Gets the cart service implementation class object.
     * </p>
     *
     * @return The cart service implementation object
     */
    public static CartService getInstance() {
        if (null == cartService) {
            cartService = new CartServiceImpl();
        }

        return cartService;
    }

    /**
     * {@inheritDoc}
     *
     * @param cart Represents the cart of the user
     * @return True if the food is added to the user cart, false otherwise
     */
    @Override
    public boolean addFoodToCart(final Cart cart) {
        return cartDAO.addFoodToCart(cart);
    }

    /**
     * {@inheritDoc}
     *
     * @param userId Represents the id 0f the {@link User}
     * @return The list of all foods from the user cart
     */
    @Override
    public Optional<List<Cart>> getCart(final long userId) {
        return cartDAO.getCart(userId);
    }

    /**
     * {@inheritDoc}
     *
     * @param cartId Represents the id of the user cart
     * @return True if the food is removed,false otherwise
     */
    @Override
    public boolean removeFood(final long cartId) {
        return cartDAO.removeFood(cartId);
    }

    /**
     * {@inheritDoc}
     *
     * @param userId Represents the id of the {@link User}
     * @return The true if the cart is cleared, false otherwise
     */
    @Override
    public boolean clearCart(final long userId) {
        return cartDAO.clearCart(userId);
    }
}