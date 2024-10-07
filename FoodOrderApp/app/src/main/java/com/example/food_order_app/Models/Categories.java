package com.example.food_order_app.Models;

public class Categories {

    private int categoryId;

    private String name;

    private int foodId;

    public Categories() {
    }

    public Categories(String name, int foodId) {
        this.name = name;
        this.foodId = foodId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
