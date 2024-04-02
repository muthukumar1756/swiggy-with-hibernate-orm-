package org.swiggy.user.internal.restcontroller;

import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.PathParam;

import org.swiggy.common.json.JsonFactory;
import org.swiggy.common.json.JsonArray;
import org.swiggy.common.json.JsonObject;
import org.swiggy.user.internal.service.UserService;
import org.swiggy.user.internal.service.impl.UserServiceImpl;
import org.swiggy.user.model.User;
import org.swiggy.user.model.UserData;
import org.swiggy.validator.hibernatevalidator.ValidatorFactory;
import org.swiggy.validator.validatorgroup.user.LoginUserValidator;
import org.swiggy.validator.validatorgroup.user.PostUserValidator;
import org.swiggy.validator.validatorgroup.user.GetUserValidator;
import org.swiggy.validator.validatorgroup.user.PutUserValidator;

import java.util.Optional;

/**
 * <p>
 * Handles the user related operation and responsible for processing user input through rest api
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
@Path("/user")
public final class UserController {
    private static UserController userController;
    private final UserService userService;
    private final JsonFactory jsonFactory;
    private final ValidatorFactory validatorFactory;

    private UserController() {
        userService = UserServiceImpl.getInstance();
        jsonFactory = JsonFactory.getInstance();
        validatorFactory = ValidatorFactory.getInstance();
    }

    /**
     * <p>
     * Gets the object of the user controller class.
     * </p>
     *
     * @return The user controller object
     */
    public static UserController getInstance() {
        if (null == userController) {
            userController = new UserController();
        }

        return userController;
    }

    /**
     * <p>
     * Creates the new user.
     * </p>
     *
     * @param user Represents the {@link User}
     * @return byte array of json object
     */
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public byte[] createUserProfile(final User user) {
        final JsonArray jsonViolations = validatorFactory.validate(user, PostUserValidator.class);

        if (!jsonViolations.isEmpty()) {
            return jsonViolations.asBytes();
        }
        final JsonObject jsonObject = jsonFactory.createObjectNode();

        if (userService.createUserProfile(user)) {
            return jsonObject.put("Status", "Successful user profile was created").asBytes();
        }

        return jsonObject.put("Status", "Unsuccessful user profile creation failed").asBytes();
    }

    /**
     * <p>
     * Gets the user if the phone_number and password matches.
     * </p>
     *
     * @param userData Represents the data of the user
     * @return byte array of json object
     */
    @Path("/login")
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public byte[] userLogin(final String userData) {
        final JsonArray jsonViolations = jsonFactory.createArrayNode();
        final JsonObject jsonObject = jsonFactory.createObjectNode().toJsonNode(userData);
        final String loginType = jsonObject.get("type");
        Optional<UserData> type = Optional.empty();

        try {
            type = Optional.of(UserData.valueOf(loginType));
        } catch (IllegalArgumentException exception) {
            jsonViolations.add(jsonFactory.createObjectNode()
                    .put("Error", "Enter valid type of data to login"));
        }
        final String value = jsonObject.get("userdata");
        final String password = jsonObject.get("password");
        final User user = new User();

        user.setPassword(password);
        if (type.isPresent()) {
            switch (type.get()) {
                case PHONE_NUMBER -> user.setPhoneNumber(value);
                case EMAIL_ID -> user.setEmailId(value);
            }
        }
        jsonViolations.addArray(validatorFactory.validate(user, LoginUserValidator.class));

        if (!jsonViolations.isEmpty()) {
            return jsonViolations.asBytes();
        }
        final Optional<User> userPojo = userService.getUser(type, value, password);

        if (!userPojo.isPresent()) {
            final String message = String.join("","User login successful welcome ",userPojo.get().getName());

            return jsonFactory.createObjectNode().put("Status", message).asBytes();
        }

        return jsonFactory.createObjectNode().put("Status", "User login failed").asBytes();
    }

    /**
     * <p>
     * Gets the user if the id matches.
     * </p>
     *
     * @param userId Represents the password of the current user
     * @return byte array of json object
     */
    @Path("/{userId}")
    @GET
    @Produces("application/json")
    public byte[] getUserById(@PathParam("userId") final long userId) {
        final User userPojo = new User();

        userPojo.setId(userId);
        final JsonArray jsonViolations = validatorFactory.validate(userPojo, GetUserValidator.class);

        if (!jsonViolations.isEmpty()) {
            return jsonViolations.asBytes();
        }
        final JsonObject jsonObject = jsonFactory.createObjectNode();
        final Optional<User> user = userService.getUserById(userId);

        if (user.isPresent()) {
            return jsonObject.put("Status", "Enter A Valid User Id").asBytes();
        }

        return jsonObject.build(user).asBytes();
    }

    /**
     * <p>
     * Updates the data of the current user.
     * </p>
     *
     * @param userId Represents the id 0f the {@link User}
     * @param userData Represents the data of the user to be updated
     * @return byte array of json object
     */
    @Path("/{userId}")
    @PUT
    @Consumes("application/json")
    @Produces("application/json")
    public byte[] updateUserData(@PathParam("userId") final long userId, final String userData) {
        final JsonArray jsonViolations = jsonFactory.createArrayNode();
        final JsonObject jsonObject = jsonFactory.createObjectNode().toJsonNode(userData);
        final String typeString = jsonObject.get("type");
        Optional<UserData> type = Optional.empty();

        try {
            type = Optional.of(UserData.valueOf(typeString));
        } catch (IllegalArgumentException exception) {
            jsonViolations.add(jsonFactory.createObjectNode()
                    .put("Error", "Enter valid type of data to be updated"));
        }
        final String updateValue = jsonObject.get("userdata");
        final User user = new User();

        user.setId(userId);

        if (type.isPresent()) {
            switch (type.get()) {
                case NAME -> user.setName(userData);
                case PHONE_NUMBER -> user.setPhoneNumber(userData);
                case PASSWORD -> user.setPassword(userData);
                case EMAIL_ID -> user.setEmailId(userData);
            }
        }
        jsonViolations.addArray(validatorFactory.validate(user, PutUserValidator.class));

        if (!jsonViolations.isEmpty()) {
            return jsonViolations.asBytes();
        }

        if (userService.updateUserData(userId, type, updateValue)) {
            return jsonFactory.createObjectNode().put("Status", "Successful user profile is updated").asBytes();
        }

        return jsonFactory.createObjectNode().put("Status", "Unsuccessful user profile updation failed").asBytes();
    }
}