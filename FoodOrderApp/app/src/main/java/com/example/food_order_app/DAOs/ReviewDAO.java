package com.example.food_order_app.DAOs;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.food_order_app.Models.Review;

import java.util.List;

@Dao
public interface ReviewDAO {

    @Query("SELECT * FROM reviews")
    List<Review> getAllReviews();

    @Query("SELECT * FROM reviews WHERE review_id = :reviewId")
    Review getReviewById(int reviewId);

    @Insert
    void insertReview(Review review);

    @Update
    void updateReview(Review review);

    @Delete
    void deleteReview(Review review);

}
