package com.example.food_order_app.DAOs;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.food_order_app.Models.FoodItem;

import java.util.List;

@Dao
public interface FoodItemDAO {

    @Query("SELECT * FROM food_items")
    List<FoodItem> getAllFoodItems();

    @Query("SELECT * FROM food_items WHERE food_id = :foodId")
    FoodItem getFoodItemById(int foodId);

    @Insert
    void insertFoodItem(FoodItem foodItem);

    @Update
    void updateFoodItem(FoodItem foodItem);

    @Delete
    void deleteFoodItem(FoodItem foodItem);
}
