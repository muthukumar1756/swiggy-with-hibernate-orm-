package org.swiggy.validator.regexvalidator;

/**
 * <p>
 * Validates the input given by the users.
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public final class DataValidator {

    private static final String NAME_PATTERN = "^[A-Za-z][A-Za-z\\s]{2,20}$";
    private static final String PASSWORD_PATTERN = "^(?=.*[a-zA-Z\\d])(?=.*[@#$%^&+=]).{8,15}$";
    private static final String PHONE_NUMBER_PATTERN = "^(0/91)?[6789]\\d{9}$";
    private static final String EMAIL_PATTERN = "^[a-z][a-z\\d._]+@[a-z]{5,}.[a-z]{2,3}$";
    private static DataValidator dataValidator;

    private DataValidator() {
    }

    /**
     * <p>
     * Gets the object of the data validator class.
     * </p>
     *
     * @return The validation object
     */
    public static DataValidator getInstance() {
        if (null == dataValidator) {
            dataValidator = new DataValidator();
        }

        return dataValidator;
    }

    /**
     * <p>
     * Validates the username of the user.
     * </p>
     *
     * @param userName The username of the user
     * @return True if username is valid, false otherwise
     */
    public boolean validateUserName(final String userName) {
       return userName.matches(NAME_PATTERN);
    }

    /**
     * <p>
     * Validates the mobile number of the user.
     * </p>
     *
     * @param phoneNumber The mobile number of the user
     * @return True if mobile number is valid, false otherwise
     */
    public boolean validatePhoneNumber(final String phoneNumber) {
       return phoneNumber.matches(PHONE_NUMBER_PATTERN);
    }

    /**
     * <p>
     * Validates the email of the user.
     * </p>
     *
     * @param emailId The email of the user
     * @return True if email is valid, false otherwise
     */
    public boolean validateEmailId(final String emailId) {
        return emailId.matches(EMAIL_PATTERN);
    }

    /**
     * <p>
     * Validates the password of the user.
     * </p>
     *
     * @param password The password of the user
     * @return True if password is valid, false otherwise
     */
    public boolean validatePassword(final String password) {
        return password.matches(PASSWORD_PATTERN);
    }
}