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

import org.swiggy.validator.validatorgroup.food.PostFoodValidator;
import org.swiggy.validator.validatorgroup.food.DeleteFoodValidator;
import org.swiggy.validator.validatorgroup.food.GetFoodValidator;

import java.util.Objects;

/**
 * <p>
 * Represents food entity with properties and methods.
 * </p>
 *
 * @author Muthu kumar V
 * @version 1.0
 */
@Entity
@Table(name = "food")
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Positive(message = "Food id can't be negative", groups = {GetFoodValidator.class, DeleteFoodValidator.class})
    private long id;
    @Column(name = "name")
    @NotNull(message = "Name can't be null", groups = {PostFoodValidator.class, GetFoodValidator.class})
    @Pattern(message = "Enter a valid name", regexp = "^[A-Za-z][A-Za-z\\s]{2,20}$", groups = {PostFoodValidator.class})
    private String name;
    @Column(name = "rate")
    @NotNull(message = "Rate can't be null", groups = {PostFoodValidator.class, GetFoodValidator.class})
    @Positive(message = "Rate can't be negative", groups = {PostFoodValidator.class})
    private float rate;
    @Column(name = "type")
    @NotNull(message = "Type can't be null", groups = {PostFoodValidator.class, GetFoodValidator.class})
    private FoodType type;
    @Column(name = "quantity")
    @NotNull(message = "Quantity can't be null", groups = {PostFoodValidator.class})
    @Positive(message = "Quantity can't be negative", groups = {PostFoodValidator.class})
    private int quantity;

    public Food() {
    }

    public Food(final String foodName, final float rate, final FoodType type, final int quantity) {
        this.name = foodName;
        this.rate = rate;
        this.type = type;
        this.quantity = quantity;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(final float rate) {
        this.rate = rate;
    }

    public FoodType getType() {
        return type;
    }

    public void setType(final FoodType foodType) {
        this.type = foodType;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(final int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {

        return null;
    }

    @Override
    public boolean equals(final Object object) {
        return ! Objects.isNull(object) && getClass() == object.getClass() && this.hashCode() == object.hashCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}