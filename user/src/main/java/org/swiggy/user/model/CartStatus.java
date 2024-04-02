package org.swiggy.user.model;

/**
 * <p>
 * Defines the status of the item in cart.
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public enum CartStatus {
    IN_CART(1),
    ORDER_PLACED(2);

    private final int id;

    CartStatus(final int id) {
        this.id = id;
    }

    /**
     *
     * @param cartStatus Represents the category of the food
     * @return The id of the food category
     */
    public static int getId(CartStatus cartStatus) {
        return cartStatus.id;
    }

    public static CartStatus getTypeById(final int id) {
        for (CartStatus type : values()) {

            if (type.id == id) {
                return type;
            }
        }
        return null;
    }
}
