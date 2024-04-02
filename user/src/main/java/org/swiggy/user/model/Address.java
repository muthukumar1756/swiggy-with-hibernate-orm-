package org.swiggy.user.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.GenerationType;

import jakarta.validation.constraints.*;
import org.swiggy.validator.validatorgroup.address.PostAddressValidator;
import org.swiggy.validator.validatorgroup.address.GetAddressValidator;

import java.util.Objects;

@Entity
@Table(name = "Address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "user_id")
    @NotNull(message = "userId can't be null", groups = {PostAddressValidator.class, GetAddressValidator.class})
    @Positive(message = "user id can't be negative", groups = {PostAddressValidator.class, GetAddressValidator.class})
    private long userId;
    @Column(name = "house_number")
    @NotNull(message = "houseNumber can't be null", groups = {PostAddressValidator.class})
    @Size(max = 10)
    private String houseNumber;
    @Column(name = "street_name")
    @NotNull(message = "streetName can't be null", groups = {PostAddressValidator.class})
    @Pattern(message = "enter a valid street name", regexp = "^[A-Za-z][A-Za-z\\s]{3,20}$", groups = {PostAddressValidator.class})
    private String streetName;
    @Column(name = "area_name")
    @NotNull(message = "areaName can't be null", groups = {PostAddressValidator.class})
    @Pattern(message = "enter a valid area name", regexp = "^[A-Za-z][A-Za-z\\s]{3,20}$", groups = {PostAddressValidator.class})
    private String areaName;
    @Column(name = "city_name")
    @NotNull(message = "cityName can't be null", groups = {PostAddressValidator.class})
    @Pattern(message = "enter a valid city name", regexp = "^[A-Za-z][A-Za-z\\s]{3,20}$", groups = {PostAddressValidator.class})
    private String cityName;
    @Column(name = "pincode")
    @NotNull(message = "pincode can't be null", groups = {PostAddressValidator.class})
    @Pattern(message = "enter a valid pincode", regexp = "^[1-9][0-9]{5}$", groups = {PostAddressValidator.class})
    private String pincode;
    @Column(name = "address_type")
    @NotNull(message = "address type can't be null", groups = {PostAddressValidator.class})
    private AddressType addressType;

    public Address() {
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(final String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(final String streetName) {
        this.streetName = streetName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(final String areaName) {
        this.areaName = areaName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(final String cityName) {
        this.cityName = cityName;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(final String pincode) {
        this.pincode = pincode;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(final long userId) {
        this.userId = userId;
    }

    public AddressType getAddressType() {
        return addressType;
    }

    public void setAddressType(final AddressType addressType) {
        this.addressType = addressType;
    }

    @Override
    public boolean equals(final Object object) {
        return ! Objects.isNull(object) && getClass() == object.getClass() && this.hashCode() == object.hashCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId);
    }
}
