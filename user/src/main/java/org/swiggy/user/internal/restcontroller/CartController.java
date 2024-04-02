package org.swiggy.user.internal.restcontroller;

import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.GET;
import javax.ws.rs.DELETE;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.PathParam;

import java.util.List;
import java.util.Optional;

import org.swiggy.common.json.JsonFactory;
import org.swiggy.common.json.JsonArray;
import org.swiggy.common.json.JsonObject;

import org.swiggy.user.model.Cart;
import org.swiggy.user.model.User;
import org.swiggy.user.internal.service.CartService;
import org.swiggy.user.internal.service.impl.CartServiceImpl;

import org.swiggy.validator.hibernatevalidator.ValidatorFactory;
import org.swiggy.validator.validatorgroup.cart.ClearCartValidator;
import org.swiggy.validator.validatorgroup.cart.DeleteCartValidator;
import org.swiggy.validator.validatorgroup.cart.PostCartValidator;
import org.swiggy.validator.validatorgroup.cart.GetCartValidator;

/**
 * <p>
 * Handles the users cart related operation and responsible for receiving user input through rest api and processing it.
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
@Path("/cart")
public final class CartController {

    private static CartController cartController;
    private final CartService cartService;
    private final JsonFactory jsonFactory;
    private final ValidatorFactory validatorFactory;

    private CartController() {
        cartService = CartServiceImpl.getInstance();
        jsonFactory = JsonFactory.getInstance();
        validatorFactory = ValidatorFactory.getInstance();
    }

    /**
     * <p>
     * Gets the object of the cart controller class.
     * </p>
     *
     * @return The cart controller object
     */
    public static CartController getInstance() {
        if (null == cartController) {
            cartController = new CartController();
        }

        return cartController;
    }

    /**
     * <p>
     * Adds the selected food to the user cart.
     * </p>
     *
     * @param cart Represents the cart of the user
     * @return byte array of json object
     */
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public byte[] addFoodToCart(final Cart cart) {
        final JsonArray jsonViolations = validatorFactory.validate(cart, PostCartValidator.class);

        if (!jsonViolations.isEmpty()) {
            return jsonViolations.asBytes();
        }
        final JsonObject jsonObject = jsonFactory.createObjectNode();

        if (cartService.addFoodToCart(cart)) {
            return jsonObject.put("Status", "Successful cart item added").asBytes();
        }

        return jsonObject.put("Status", "Unsuccessful adding cart item failed enter a valid id").asBytes();
    }

    /**
     * <p>
     * Gets the cart of the user.
     * </p>
     *
     * @param userId Represents the id of the {@link User}
     * @return byte array of json object
     */
    @Path("/{userId}")
    @GET
    @Produces("application/json")
    public byte[] getCart(@PathParam("userId") final long userId) {
        final Cart cart = new Cart();

        cart.setUserId(userId);
        final JsonArray jsonViolations = validatorFactory.validate(cart, GetCartValidator.class);

        if (!jsonViolations.isEmpty()) {
            return jsonViolations.asBytes();
        }
        final Optional<List<Cart>> cartList = cartService.getCart(userId);

        if (cartList.isPresent()) {
            return jsonFactory.createObjectNode().put("Status", "Your cart is empty or user id is invalid").asBytes();
        }

        return jsonFactory.createArrayNode().build(cartList).asBytes();
    }

    /**
     * <p>
     * Removes the food selected by the user.
     * </p>
     *
     * @param cartId Represents the id of the user cart
     * @return byte array of json object
     */
    @Path("/{cartId}")
    @DELETE
    @Produces("application/json")
    public byte[] removeFood(@PathParam("cartId") final long cartId) {
        final Cart cart = new Cart();

        cart.setId(cartId);
        final JsonArray jsonViolations = validatorFactory.validate(cart, DeleteCartValidator.class);

        if (!jsonViolations.isEmpty()) {
            return jsonViolations.asBytes();
        }
        final JsonObject jsonObject = jsonFactory.createObjectNode();

        if (cartService.removeFood(cartId)) {
            return jsonObject.put("Status", "Successful food was removed").asBytes();
        }

        return jsonObject.put("Status", "Unsuccessful removing food was failed enter a valid id").asBytes();
    }

    /**
     * <p>
     * Remove all the foods from the user cart.
     * </p>
     *
     * @param userId Represents the id of the {@link User}
     * @return byte array of json object
     */
    @Path("clear/{userId}")
    @DELETE
    @Produces("application/json")
    public byte[] clearCart(@PathParam("userId") final long userId) {
        final Cart cart = new Cart();

        cart.setUserId(userId);
        final JsonArray jsonViolations = validatorFactory.validate(cart, ClearCartValidator.class);

        if (!jsonViolations.isEmpty()) {
            return jsonViolations.asBytes();
        }
        final JsonObject jsonObject = jsonFactory.createObjectNode();

        if (cartService.clearCart(userId)) {
            return jsonObject.put("Status", "Successful cart was cleared").asBytes();
        }

        return jsonObject.put("Status", "Unsuccessful clearing cart was failed enter a valid id").asBytes();
    }
}