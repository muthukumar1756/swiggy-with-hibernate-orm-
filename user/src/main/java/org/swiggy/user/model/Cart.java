package org.swiggy.user.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import javax.persistence.GenerationType;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import org.hibernate.annotations.ColumnDefault;
import org.swiggy.validator.validatorgroup.cart.ClearCartValidator;
import org.swiggy.validator.validatorgroup.cart.DeleteCartValidator;
import org.swiggy.validator.validatorgroup.cart.PostCartValidator;
import org.swiggy.validator.validatorgroup.cart.GetCartValidator;

import java.util.Objects;

/**
 * <p>
 * Represents cart entity with properties and methods.
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
@Entity
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Positive(message = "Cart id can't be negative", groups = {DeleteCartValidator.class})
    private long id;
    @Column(name = "user_id")
    @NotNull(message = "UserId can't be null", groups = {PostCartValidator.class, GetCartValidator.class, ClearCartValidator.class})
    @Positive(message = "User id can't be negative", groups = {PostCartValidator.class, GetCartValidator.class, ClearCartValidator.class})
    private long userId;
    @Column(name = "food_id")
    @NotNull(message = "FoodId can't be null", groups = {PostCartValidator.class})
    @Positive(message = "Food id can't be negative", groups = {PostCartValidator.class})
    private long foodId;
    @NotNull(message = "FoodName can't be null", groups = {PostCartValidator.class})
    @Pattern(message = "Enter a valid food name", regexp = "^[A-Za-z][A-Za-z\\s]{2,20}$", groups = {PostCartValidator.class})
    private String foodName;
    @Column(name = "restaurant_id")
    @NotNull(message = "RestaurantId can't be null", groups = {PostCartValidator.class})
    @Positive(message = "Restaurant id can't be negative", groups = {PostCartValidator.class})
    private long restaurantId;
    @NotNull(message = "RestaurantName can't be null", groups = {PostCartValidator.class})
    @Pattern(message = "Enter a valid restaurant name", regexp = "^[A-Za-z][A-Za-z\\s]{2,20}$", groups = {PostCartValidator.class})
    private String restaurantName;
    @Column(name = "quantity")
    @NotNull(message = "Quantity can't be null", groups = {PostCartValidator.class})
    @Positive(message = "Quantity can't be negative", groups = {PostCartValidator.class})
    private int quantity;
    @Column(name = "amount")
    @NotNull(message = "Amount can't be null", groups = {PostCartValidator.class})
    @Positive(message = "Amount can't be negative", groups = {PostCartValidator.class})
    private float amount;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @ColumnDefault("1")
    private CartStatus cartStatus;

    public Cart() {
    }

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(final int quantity) {
        this.quantity = quantity;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(final float amount) {
        this.amount = amount;
    }

    public CartStatus getCartStatus() {
        return cartStatus;
    }

    public void setCartStatus(final CartStatus cartStatus) {
        this.cartStatus = cartStatus;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(final String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(final String foodName) {
        this.foodName = foodName;
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
