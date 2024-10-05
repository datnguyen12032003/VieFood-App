package com.example.food_order_app.DAOs;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.food_order_app.Models.User;

import java.util.List;

@Dao
public interface UserDAO {

    @Query("SELECT * FROM user")
    List<User> getAllUsers();

    @Query("SELECT * FROM user WHERE user_id = :userId")
    User getUserById(int userId);

    @Query("SELECT * FROM user WHERE user_email = :userEmail")
    User getUserByEmail(String userEmail);

    @Query("SELECT * FROM user WHERE user_phone = :userPhone")
    User getUserByPhone(String userPhone);


    @Query("SELECT * FROM user WHERE user_email = :userEmail AND user_password = :userPassword")
    User getUserByEmailAndPassword(String userEmail, String userPassword);

    @Insert
    void insertUser(User user);

    @Update
    void updateUser(User user);

    @Delete
    void deleteUser(User user);

}
