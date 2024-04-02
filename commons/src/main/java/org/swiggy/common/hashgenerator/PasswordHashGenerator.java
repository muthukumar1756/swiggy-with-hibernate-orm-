package org.swiggy.common.hashgenerator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.swiggy.common.exception.HashAlgorithmNotFoundException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * <p>
 * Provides hashed password for security purposes.
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
public final class PasswordHashGenerator {

    private static final Logger LOGGER = LogManager.getLogger(PasswordHashGenerator.class);
    private static PasswordHashGenerator passwordHashGenerator;

    private PasswordHashGenerator() {
    }

    /**
     * <p>
     * Gets the password generator class object.
     * </p>
     *
     * @return The password generator object
     */
    public static PasswordHashGenerator getInstance() {
        if (null == passwordHashGenerator) {
            passwordHashGenerator = new PasswordHashGenerator();
        }

        return passwordHashGenerator;
    }

    /**
     * <p>
     * Hashes and returns the password.
     * </p>
     *
     * @param password password of the current user
     * @return The hashed password
     */
    public String hashPassword(final String password) {
        try {
            final MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            final byte[] encodedHash = messageDigest.digest(password.getBytes(StandardCharsets.UTF_8));

            final StringBuilder hashString = new StringBuilder();

            for (final byte hashByte : encodedHash) {
                hashString.append(String.format("%02x", hashByte));
            }

            return hashString.substring(0, 25);
        } catch (NoSuchAlgorithmException message) {
            LOGGER.warn(message.getMessage());
            throw new HashAlgorithmNotFoundException(message.getMessage());
        }
    }
}