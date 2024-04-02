package org.swiggy.restaurant.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.GenerationType;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import org.swiggy.validator.validatorgroup.Restaurant.LoginRestaurantValidation;
import org.swiggy.validator.validatorgroup.Restaurant.PostRestaurantValidator;
import org.swiggy.validator.validatorgroup.Restaurant.GetRestaurantValidator;
import org.swiggy.validator.validatorgroup.Restaurant.PutRestaurantValidator;

import java.util.Objects;

/**
 * <p>
 * Represents restaurant entity with properties and methods.
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
@Entity
@Table(name = "restaurant")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Positive(message = "Restaurant id can't be negative", groups = {GetRestaurantValidator.class, PutRestaurantValidator.class})
    private long id;
    @Column(name = "name")
    @NotNull(message = "Name can't be null", groups = {PostRestaurantValidator.class})
    @Pattern(message = "Enter a valid name", regexp = "^[A-Za-z][A-Za-z\\s]{2,20}$", groups = {PostRestaurantValidator.class, PutRestaurantValidator.class})
    private String name;
    @Column(name = "phone_number")
    @NotNull(message = "PhoneNumber can't be null", groups = {PostRestaurantValidator.class})
    @Pattern(message = "Enter a valid phone number", regexp = "^(0/91)?[6789]\\d{9}$", groups = {PostRestaurantValidator.class, PutRestaurantValidator.class, LoginRestaurantValidation.class})
    private String phoneNumber;
    @Column(name = "password")
    @NotNull(message = "Password can't be null", groups = {PostRestaurantValidator.class})
    @Pattern(message = "Enter a valid password", regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,15}$", groups = {PostRestaurantValidator.class, PutRestaurantValidator.class, LoginRestaurantValidation.class})
    private String password;
    @Column(name = "email_id")
    @NotNull(message = "EmailId can't be null", groups = {PostRestaurantValidator.class})
    @Pattern(message = "Enter a valid email id", regexp = "^[a-z][a-z\\d._]+@[a-z]{5,20}.[a-z]{2,3}$", groups = {PostRestaurantValidator.class, PutRestaurantValidator.class, LoginRestaurantValidation.class})
    private String emailId;

    public Restaurant() {
    }

    public Restaurant(final String name, final String phoneNumber, final String emailId, final String password) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.emailId = emailId;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(final String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(final String emailId) {
        this.emailId = emailId;
    }

    @Override
    public boolean equals(final Object object) {
        return ! Objects.isNull(object) && getClass() == object.getClass() && this.hashCode() == object.hashCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(emailId, phoneNumber);
    }
}