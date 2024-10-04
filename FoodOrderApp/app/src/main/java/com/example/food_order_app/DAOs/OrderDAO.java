package com.example.food_order_app.DAOs;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.food_order_app.Models.Order;

import java.util.List;

@Dao
public interface OrderDAO {

    @Query("SELECT * FROM orders")
    List<Order> getAllOrders();

    @Query("SELECT * FROM orders WHERE order_id = :orderId")
    Order getOrderById(int orderId);

    @Insert
    void insertOrder(Order order);

    @Update
    void updateOrder(Order order);

    @Delete
    void deleteOrder(Order order);
}
