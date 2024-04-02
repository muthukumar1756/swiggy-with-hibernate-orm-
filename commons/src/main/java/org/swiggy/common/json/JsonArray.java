package org.swiggy.common.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

/**
 * <p>
 * Handles the methods to work with json array.
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public final class JsonArray {

    private static final JsonFactory JSON_FACTORY = JsonFactory.getInstance();
    private ArrayNode arrayNode;

    JsonArray(final JsonNode jsonNode) {
        this.arrayNode = (ArrayNode) jsonNode;
    }

    /**
     * <p>
     * Creates the json array object by wrapping the json node object.
     * </p>
     *
     * @param jsonNode the json node object
     * @return json array object
     */
    private JsonArray create(final JsonNode jsonNode) {
        return new JsonArray(jsonNode);
    }

    /**
     * <p>
     * Builds the json array with the given object.
     * </p>
     *
     * @param object the object to be converted into json array
     * @return JsonArray object
     */
    public JsonArray build(final Object object) {
        return create(JSON_FACTORY.toJson(object));
    }

    /**
     * <p>
     * converts the array node into byte array.
     * </p>
     *
     * @return The byte array of array node
     */
    public byte[] asBytes() {
        return JSON_FACTORY.asByteArray(arrayNode);
    }

    /**
     * <p>
     * Adds the json object to the array node.
     * </p>
     *
     * @param jsonObject The json object
     */
    public void add(final JsonObject jsonObject) {
        arrayNode.add(jsonObject.getObjectNode());
    }

    /**
     * <p>
     * Adds the json object to the array node.
     * </p>
     *
     * @param jsonArray The json object
     */
    public void addArray(final JsonArray jsonArray) {
        arrayNode.addAll(jsonArray.getArrayNode());
    }

    /**
     * <p>
     * Gets the array node.
     * </p>
     *
     * @return The array node
     */
    private ArrayNode getArrayNode() {
        return arrayNode;
    }

    /**
     * <p>
     *  checks the array node is empty or not
     * </p>
     *
     * @return true if the array node is empty, false otherwise
     */
    public boolean isEmpty() {
        return arrayNode.isEmpty();
    }
}
