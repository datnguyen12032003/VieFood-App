package com.example.food_order_app.Models;

public class Cart {
    String cartId;
    String userId;
    String foodId;
    int quantity;
    double total_price;

    public Cart() {
    }

    public Cart(String userId, double total_price, int quantity, String foodId) {
        this.userId = userId;
        this.total_price = total_price;
        this.quantity = quantity;
        this.foodId = foodId;
    }

    // Getter và setter cho các thuộc tính

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
