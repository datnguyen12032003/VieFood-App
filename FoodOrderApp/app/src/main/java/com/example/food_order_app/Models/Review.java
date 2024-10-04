package com.example.food_order_app.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "reviews")
public class Review {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "review_id")
    private int reviewId;

    @ColumnInfo(name = "user_id")
    private int userId;

    @ColumnInfo(name = "food_id")
    private int foodId;

    @ColumnInfo(name = "rating")
    private int rating;

    @ColumnInfo(name = "comment")
    private String comment;

    public Review() {
    }

    public Review(int userId, int foodId, int rating, String comment) {
        this.userId = userId;
        this.foodId = foodId;
        this.rating = rating;
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
