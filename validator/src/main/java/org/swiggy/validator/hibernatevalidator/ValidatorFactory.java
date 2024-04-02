package org.swiggy.validator.hibernatevalidator;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import org.hibernate.validator.HibernateValidator;

import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
import org.swiggy.common.json.JsonFactory;
import org.swiggy.common.json.JsonArray;

import java.util.Set;

/**
 * <p>
 * Wraps the Validator and the methods of hibernate validator can be used from this class.
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public final class ValidatorFactory {

    private final JsonFactory jsonFactory;
    private static ValidatorFactory validatorFactory;
    private static Validator validator = getValidator();

    private ValidatorFactory() {
        jsonFactory = JsonFactory.getInstance();
    }

    /**
     * <p>
     * Gets the validator factory object.
     * </p>
     *
     * @return The object of validator factory class
     */
    public static ValidatorFactory getInstance() {
        if (null == validatorFactory) {
            validatorFactory = new ValidatorFactory();
        }

        return validatorFactory;
    }

    /**
     * <p>
     * Gets the validator object.
     * </p>
     *
     * @return The object of validator
     */
    private static Validator getValidator() {
        if (null == validator) {
            validator = Validation.byProvider(HibernateValidator.class).configure()
                    .messageInterpolator(new ParameterMessageInterpolator()).buildValidatorFactory().getValidator();
        }

        return validator;
    }

    /**
     * <p>
     * Validates the object and returns the violations if exits
     * </p>
     *
     * @return json array of violations if exists, null otherwise
     */
    public <T> JsonArray validate(final T object, final Class<?>... groups) {
        final Set<ConstraintViolation<T>> violationSet = validator.validate(object, groups);

        if (!violationSet.isEmpty()) {
            final JsonArray jsonViolations = jsonFactory.createArrayNode();

            for (final ConstraintViolation violation : violationSet) {
                jsonViolations.add(jsonFactory.createObjectNode().put(violation.getPropertyPath().toString(),
                        violation.getMessage()));
            }

            return jsonViolations;
        }

        return jsonFactory.createArrayNode();
    }
}