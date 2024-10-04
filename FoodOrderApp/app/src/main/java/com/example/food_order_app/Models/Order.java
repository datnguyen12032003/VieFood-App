package com.example.food_order_app.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "orders")
public class Order {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "order_id")
    private int orderId;

    @ColumnInfo(name = "user_id")
    private int userId;

    @ColumnInfo(name = "order_date")
    private String orderDate;

    @ColumnInfo(name = "order_total")
    private double orderTotal;

    @ColumnInfo(name = "order_status")
    private String orderStatus;

    @ColumnInfo(name = "order_address")
    private String orderAddress;

    @ColumnInfo(name = "order_phone")
    private String orderPhone;

    @ColumnInfo(name = "order_note")
    private String orderNote;

    @ColumnInfo(name = "total_price")
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
