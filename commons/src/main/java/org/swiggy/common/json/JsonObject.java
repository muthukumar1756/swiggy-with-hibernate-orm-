package org.swiggy.common.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * <p>
 * Handles the methods to work with json object.
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public final class JsonObject {

    private static final JsonFactory JSON_FACTORY = JsonFactory.getInstance();
    private ObjectNode objectNode;

    public JsonObject(final JsonNode jsonNode) {
        this.objectNode = (ObjectNode) jsonNode;
    }

    /**
     * <p>
     * Creates the json object by wrapping the json node object.
     * </p>
     *
     * @param jsonNode the json node object
     * @return The json object
     */
    private JsonObject create(final JsonNode jsonNode) {
        return new JsonObject(jsonNode);
    }

    /**
     * <p>
     * Builds the json object with the given object.
     * </p>
     *
     * @param object the object to be converted into json object
     * @return The json object
     */
    public JsonObject build(final Object object) {
        return create(JSON_FACTORY.toJson(object));
    }

    /**
     * <p>
     * converts the string into json object.
     * </p>
     *
     * @return The json object
     */
    public JsonObject toJsonNode(final String json) {
        return create(JSON_FACTORY.toJsonNode(json));
    }

    /**
     * <p>
     * converts the object node into byte array.
     * </p>
     *
     * @return The byte array of array node
     */
    public byte[] asBytes() {
        return JSON_FACTORY.asByteArray(objectNode);
    }

    /**
     * <p>
     * Gets the object node as text.
     * </p>
     *
     * @param type the value to get from the object node
     */
    public String get(final String type) {
        return objectNode.get(type).asText();
    }

    /**
     * <p>
     *  Puts the key and value in the object node.
     * </p>
     *
     * @param key The key string
     * @param value The Value string
     * @return The Json object
     */
    public JsonObject put(final String key, final String value) {
        objectNode.put(key, value);

        return this;
    }

    /**
     * <p>
     * Gets the object node.
     * </p>
     *
     * @return The object node
     */
    public ObjectNode getObjectNode() {
        return objectNode;
    }
}