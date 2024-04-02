package org.swiggy.user.internal.restcontroller;

import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.PathParam;

import java.util.List;
import java.util.Optional;

import org.swiggy.common.json.JsonFactory;
import org.swiggy.common.json.JsonArray;
import org.swiggy.common.json.JsonObject;
import org.swiggy.user.model.Address;
import org.swiggy.user.model.Order;
import org.swiggy.user.model.User;
import org.swiggy.user.internal.service.OrderService;
import org.swiggy.user.internal.service.impl.OrderServiceImpl;
import org.swiggy.validator.hibernatevalidator.ValidatorFactory;
import org.swiggy.validator.validatorgroup.address.PostAddressValidator;
import org.swiggy.validator.validatorgroup.address.GetAddressValidator;
import org.swiggy.validator.validatorgroup.order.GetOrderValidator;
import org.swiggy.validator.validatorgroup.order.PostOrderValdiator;

/**
 * <p>
 * Handles the order related operation and responsible for receiving user input through rest api and processing it.
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
@Path("/order")
public final class OrderController {

    private static OrderController orderController;
    private final OrderService orderService;
    private final JsonFactory jsonFactory;
    private final ValidatorFactory validatorFactory;

    private OrderController() {
        orderService = OrderServiceImpl.getInstance();
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
    public static OrderController getInstance() {
        if (null == orderController) {
            orderController = new OrderController();
        }

        return orderController;
    }

    /**
     * <p>
     * places the user orders.
     * </p>
     *
     * @param orderList Represents the list of order items
     * @return byte array of json object
     */
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public byte[] placeOrder(final List<Order> orderList) {
        final JsonArray jsonViolations = validatorFactory.validate(orderList, PostOrderValdiator.class);

        if (!jsonViolations.isEmpty()) {
            return jsonViolations.asBytes();
        }
        final JsonObject jsonObject = jsonFactory.createObjectNode();

        if (orderService.placeOrder(orderList)) {
            return jsonObject.put("Status", "Successful order was placed").asBytes();
        }

        return jsonObject.put("Status", "Unsuccessful order placing failed").asBytes();
    }

    /**
     * <p>
     * Stores the address of the user.
     * </p>
     *
     * @param address Represents the address of the user
     * @return byte array of json object
     */
    @Path("/address")
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public byte[] addAddress(final Address address) {
        final JsonArray jsonViolations = validatorFactory.validate(address, PostAddressValidator.class);

        if (!jsonViolations.isEmpty()) {
            return jsonViolations.asBytes();
        }
        final JsonObject jsonObject = jsonFactory.createObjectNode();

        if (orderService.addAddress(address)) {
            return jsonObject.put("Status", "Successful address was added").asBytes();
        }

        return jsonObject.put("Status", "Unsuccessful adding address was failed").asBytes();
    }

    /**
     * <p>
     * Displays all the addresses of the user.
     * </p>
     *
     * @param userId Represents the id of the {@link User}
     * @return byte array of json object
     */
    @Path("/address/{userId}")
    @GET
    @Produces("application/json")
    public byte[] getAddress(@PathParam("userId") final long userId) {
        final Address address = new Address();

        address.setUserId(userId);
        final JsonArray jsonViolations = validatorFactory.validate(address, GetAddressValidator.class);

        if (!jsonViolations.isEmpty()) {
            return jsonViolations.asBytes();
        }
        final Optional<List<Address>> addressList = orderService.getAddress(userId);

        if (addressList.isPresent()) {
            return jsonFactory.createObjectNode()
                    .put("Status", "Unsuccessful address list is empty or user id is invalid").asBytes();
        }

        return jsonFactory.createArrayNode().build(addressList).asBytes();
    }

    /**
     * <p>
     * Gets the orders placed by the user.
     * </p>
     *
     * @param userId Represents the id of the {@link User}
     * @return byte array of json object
     */
    @Path("/{userId}")
    @GET
    @Produces("application/json")
    public byte[] getOrders(@PathParam("userId") final long userId) {
        final Order order = new Order();

        order.setUserId(userId);
        final JsonArray jsonViolations = validatorFactory.validate(order, GetOrderValidator.class);

        if (!jsonViolations.isEmpty()) {
            return jsonViolations.asBytes();
        }
        final Optional<List<Order>> orderList = orderService.getOrders(userId);

        if (orderList.isPresent()) {
            return jsonFactory.createObjectNode()
                    .put("Status", "Unsuccessful order list is empty or user id is invalid").asBytes();
        }

        return jsonFactory.createArrayNode().build(orderList).asBytes();
    }
}