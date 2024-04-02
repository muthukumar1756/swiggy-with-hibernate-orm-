package org.swiggy.user.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.GenerationType;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import org.swiggy.validator.validatorgroup.cart.PostCartValidator;
import org.swiggy.validator.validatorgroup.order.GetOrderValidator;
import org.swiggy.validator.validatorgroup.order.PostOrderValdiator;

import java.util.Objects;

/**
 * <p>
 * Represents order entity with properties and methods.
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "user_id")
    @NotNull(message = "UserId can't be null", groups = {PostOrderValdiator.class, GetOrderValidator.class})
    @Positive(message = "User id can't be negative", groups = {PostOrderValdiator.class, GetOrderValidator.class})
    private long userId;
    @Column(name = "cart_id")
    @NotNull(message = "CartId can't be null", groups = {PostOrderValdiator.class})
    @Positive(message = "Cart id can't be negative", groups = {PostOrderValdiator.class})
    private long cartId;
    @Column(name = "food_id")
    @NotNull(message = "FoodId can't be null", groups = {PostOrderValdiator.class})
    @Positive(message = "Restaurant id can't be negative", groups = {PostOrderValdiator.class})
    private long foodId;
    @NotNull(message = "FoodName can't be null", groups = {PostOrderValdiator.class})
    @Pattern(message = "Enter a valid food name", regexp = "^[A-Za-z][A-Za-z\\s]{2,20}$", groups = {PostCartValidator.class})
    private String foodName;
    @Column(name = "restaurant_id")
    @NotNull(message = "RestaurantId can't be null", groups = {PostOrderValdiator.class})
    @Positive(message = "Restaurant Id Can't Be Negative", groups = {PostOrderValdiator.class})
    private long restaurantId;
    @NotNull(message = "RestaurantName can't be null", groups = {PostOrderValdiator.class})
    @Pattern(message = "Enter a valid restaurant name", regexp = "^[A-Za-z][A-Za-z\\s]{2,20}$", groups = {PostCartValidator.class})
    private String restaurantName;
    @Column(name = "quantity")
    @NotNull(message = "Quantity can't be null", groups = {PostOrderValdiator.class})
    @Positive(message = "Quantity can't be negative", groups = {PostOrderValdiator.class})
    private int quantity;
    @Column(name = "amount")
    @NotNull(message = "Amount can't be null", groups = {PostOrderValdiator.class})
    @Positive(message = "Amount can't be negative", groups = {PostOrderValdiator.class})
    private float amount;
    @Column(name = "address_id")
    @NotNull(message = "AddressId can't be null", groups = {PostOrderValdiator.class})
    @Positive(message = "Address id can't be negative", groups = {PostOrderValdiator.class})
    private long addressId;

    public Order() {
    }

    public void setId(final int id) {
        this.id = id;
    }

    public void setRestaurantName(final String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public void setFoodName(final String foodName) {
        this.foodName = foodName;
    }

    public void setQuantity(final int quantity) {
        this.quantity = quantity;
    }

    public void setAmount(final float amount) {
        this.amount = amount;
    }

    public long getId() {
        return id;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public String getFoodName() {
        return foodName;
    }

    public int getQuantity() {
        return quantity;
    }

    public float getAmount() {
        return amount;
    }

    public long getCartId() {
        return cartId;
    }

    public void setCartId(final long cartId) {
        this.cartId = cartId;
    }

    public long getAddressId() {
        return addressId;
    }

    public void setAddressId(final long addressId) {
        this.addressId = addressId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(final long userId) {
        this.userId = userId;
    }

    public long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(final long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public long getFoodId() {
        return foodId;
    }

    public void setFoodId(final long foodId) {
        this.foodId = foodId;
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