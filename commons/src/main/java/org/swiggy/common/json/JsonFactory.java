package org.swiggy.common.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import org.swiggy.common.exception.JacksonDataConversionException;

/**
 * <p>
 * Wraps the jackson library and the methods of jackson can be used from this class.
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public final class JsonFactory {

    private static JsonFactory jsonFactory;
    private static ObjectMapper objectMapper = getObjectMapper();

    private JsonFactory() {
    }

    /**
     * <p>
     * Gets the jackson wrapper object.
     * </p>
     *
     * @return The object of jackson wrapper class
     */
    public static JsonFactory getInstance() {
        if (null == jsonFactory) {
            jsonFactory = new JsonFactory();
        }

        return jsonFactory;
    }

    /**
     * <p>
     * Gets the object mapper object.
     * </p>
     *
     * @return The object of object mapper
     */
    private static ObjectMapper getObjectMapper() {
        if (null == objectMapper) {
            objectMapper = new ObjectMapper();

            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        }

        return objectMapper;
    }

    /**
     * <p>
     * Gets the object and converts into json node.
     * </p>
     *
     * @return The json node
     */
    public JsonNode toJson(final Object object) {
        return objectMapper.valueToTree(object);
    }

    /**
     * <p>
     * Gets the json string and converts into json node.
     * </p>
     *
     * @return The json node
     */
    public JsonNode toJsonNode(final String json) {
        try {
            return objectMapper.readTree(json);
        } catch (JsonProcessingException exception) {
            throw new JacksonDataConversionException(exception.getMessage());
        }
    }

    /**
     * <p>
     * converts the json node into byte array.
     * </p>
     *
     * @return The byte array of json node
     */
    public byte[] asByteArray(final JsonNode jsonNode) {
        try {
            return objectMapper.writeValueAsBytes(jsonNode);
        } catch (JsonProcessingException exception) {
            throw new JacksonDataConversionException(exception.getMessage());
        }
    }

    /**
     * <p>
     * creates the json object.
     * </p>
     *
     * @return The json object
     */
    public JsonObject createObjectNode() {
        return new JsonObject(objectMapper.createObjectNode());
    }

    /**
     * <p>
     * creates the json array.
     * </p>
     *
     * @return The json array
     */
    public JsonArray createArrayNode() {
        return new JsonArray(objectMapper.createArrayNode());
    }

    /**
     * <p>
     * Gets the jackson json provider.
     * </p>
     *
     * @return Jackson json provider
     */
    public JacksonJsonProvider getJsonProvider() {
        return new JacksonJsonProvider();
    }
}