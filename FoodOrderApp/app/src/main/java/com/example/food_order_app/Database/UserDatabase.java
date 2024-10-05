package com.example.food_order_app.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.food_order_app.DAOs.CategoriesDAO;
import com.example.food_order_app.DAOs.FoodItemDAO;
import com.example.food_order_app.DAOs.OrderDAO;
import com.example.food_order_app.DAOs.OrderDetailDAO;
import com.example.food_order_app.DAOs.ReviewDAO;
import com.example.food_order_app.DAOs.UserDAO;
import com.example.food_order_app.Models.Categories;
import com.example.food_order_app.Models.FoodItem;
import com.example.food_order_app.Models.Order;
import com.example.food_order_app.Models.OrderDetail;
import com.example.food_order_app.Models.Review;
import com.example.food_order_app.Models.User;

@Database(entities = {User.class, FoodItem.class, Order.class, OrderDetail.class, Categories.class, Review.class}, version = 1)
public abstract class UserDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "OrderFood.db";
    private static UserDatabase instance;

    public static synchronized UserDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), UserDatabase.class, DATABASE_NAME).allowMainThreadQueries().build();
        }
        return instance;
    }

    public abstract UserDAO userDao();

    public abstract FoodItemDAO foodItemDao();

    public abstract OrderDAO orderDao();

    public abstract OrderDetailDAO orderDetailDao();

    public abstract CategoriesDAO categoriesDao();

    public abstract ReviewDAO reviewDao();


}

