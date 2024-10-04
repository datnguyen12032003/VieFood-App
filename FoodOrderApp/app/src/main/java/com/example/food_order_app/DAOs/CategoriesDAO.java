package com.example.food_order_app.DAOs;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.food_order_app.Models.Categories;

import java.util.List;

@Dao
public interface CategoriesDAO {

    @Query("SELECT * FROM categories")
    List<Categories> getAllCategories();

    @Query("SELECT * FROM categories WHERE category_id = :categoryId")
    Categories getCategoryById(int categoryId);

}
