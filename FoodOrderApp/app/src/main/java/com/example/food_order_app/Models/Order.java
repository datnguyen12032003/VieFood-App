package com.example.food_order_app.Models;

public class Order {

    private int orderId;

    private int userId;

    private String orderDate;

    private double orderTotal;

    private String orderStatus;

    private String orderAddress;

    private String orderPhone;

    private String orderNote;

    private double totalPrice;


    public Order() {
    }

    public Order(String orderAddress, String orderDate, String orderNote, String orderPhone, String orderStatus, double orderTotal, double totalPrice, int userId) {
        this.orderAddress = orderAddress;
        this.orderDate = orderDate;
        this.orderNote = orderNote;
        this.orderPhone = orderPhone;
        this.orderStatus = orderStatus;
        this.orderTotal = orderTotal;
        this.totalPrice = totalPrice;
        this.userId = userId;
    }

    public String getOrderAddress() {
        return orderAddress;
    }

    public void setOrderAddress(String orderAddress) {
        this.orderAddress = orderAddress;
    }

    public String getOrderNote() {
        return orderNote;
    }

    public void setOrderNote(String orderNote) {
        this.orderNote = orderNote;
    }

    public String getOrderPhone() {
        return orderPhone;
    }

    public void setOrderPhone(String orderPhone) {
        this.orderPhone = orderPhone;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public double getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(double orderTotal) {
        this.orderTotal = orderTotal;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
