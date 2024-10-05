package com.example.food_order_app.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "food_items")
public class FoodItem {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "food_id")
    private int foodId;

    @ColumnInfo(name = "category_id")
    private int categoryId;

    @ColumnInfo(name = "food_name")
    private String name;

    @ColumnInfo(name = "food_price")
    private double price;

    @ColumnInfo(name = "food_description")
    private String description;

    @ColumnInfo(name = "food_image")
    private String image;

    @ColumnInfo(name = "food_status")
    private boolean status;

    @ColumnInfo(name = "food_quantity")
    private int quantity;

    @ColumnInfo(name = "food_rating")
    private double rating;

    public FoodItem() {
    }

    public FoodItem(boolean status, double rating, int quantity, double price, String name, String image, String description, int categoryId) {
        this.status = status;
        this.rating = rating;
        this.quantity = quantity;
        this.price = price;
        this.name = name;
        this.image = image;
        this.description = description;
        this.categoryId = categoryId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
