package com.example.food_order_app.DAOs;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.food_order_app.Models.OrderDetail;

import java.util.List;

@Dao
public interface OrderDetailDAO {

    @Query("SELECT * FROM order_details")
    List<OrderDetail> getAllOrderDetails();

    @Query("SELECT * FROM order_details WHERE order_detail_id = :orderDetailId")
    OrderDetail getOrderDetailById(int orderDetailId);

}
