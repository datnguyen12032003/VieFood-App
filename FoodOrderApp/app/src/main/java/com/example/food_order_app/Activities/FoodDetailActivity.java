package com.example.food_order_app.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.food_order_app.Adapters.CommentsAdapter;
import com.example.food_order_app.Models.Cart;
import com.example.food_order_app.Models.Review;
import com.example.food_order_app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FoodDetailActivity extends AppCompatActivity {

    private TextView foodName, foodDescription, foodPrice, foodRating, foodCategory, txt_no_reviews;
    private ImageView foodImage1;
    private DatabaseReference dbReview, dbFoodItem, dbCart;
    private RecyclerView recyclerView;
    private String strFoodId;
    private Double strFoodPrice;
    private AlertDialog dialog;
    private CommentsAdapter adapter;
    private List<Review> reviewList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_food_detail);

        initUI();
        strFoodId = getIntent().getStringExtra("foodId");
        strFoodPrice = getIntent().getDoubleExtra("foodPrice", 0.0);
        if (strFoodId == null) {
            showToast("Invalid food ID");
            finish();
            return;
        }
        setupRecyclerView();
        loadContent();

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        findViewById(R.id.btn_add_comment).setOnClickListener(v -> addComment());
        findViewById(R.id.btn_add_to_cart).setOnClickListener(v -> addToCart());
    }

    private void addToCart() {
        try{
            String userId = getSharedPreferences("user_prefs", MODE_PRIVATE).getString("current_user_id", null);
            if(userId == null){
                showToast("Please login to add to cart");
                return;
            }
            dbCart = FirebaseDatabase.getInstance().getReference().child("Cart").child(userId);
            if(dbCart == null){
                showToast("Failed to add to cart");
                return;
            }
            Cart cart = new Cart(userId, strFoodPrice, 1 , strFoodId);
            dbCart.push().setValue(cart).onSuccessTask(task -> {
                showToast("Added to cart");
                return null;
            });
        } catch(Exception e){
            showToast(e.getMessage());
        }

    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CommentsAdapter(reviewList);
        recyclerView.setAdapter(adapter);
    }

    private void initUI() {
        foodName = findViewById(R.id.txt_foodName);
        foodDescription = findViewById(R.id.txt_foodDescription);
        foodPrice = findViewById(R.id.txt_foodPrice);
        foodRating = findViewById(R.id.txt_foodRating);
        foodCategory = findViewById(R.id.txt_foodCategory);
        foodImage1 = findViewById(R.id.img_foodImage);
        recyclerView = findViewById(R.id.rcv_reviews);
        txt_no_reviews = findViewById(R.id.txt_no_reviews);

        reviewList = new ArrayList<>();
    }

    private void loadContent() {
        dbFoodItem = FirebaseDatabase.getInstance().getReference().child("FoodItems").child(strFoodId);
        dbFoodItem.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    displayFoodDetails(snapshot);
                    loadReview();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showToast("Failed to load food details");
            }
        });
    }

    private void loadReview() {
        dbReview = FirebaseDatabase.getInstance().getReference().child("Reviews").child(strFoodId);
        dbReview.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reviewList.clear();
                for (DataSnapshot reviewSnapshot : snapshot.getChildren()) {
                    Review review = reviewSnapshot.getValue(Review.class);
                    if (review != null) {
                        reviewList.add(review);
                    }
                }
                adapter.setData(reviewList);
                txt_no_reviews.setVisibility(reviewList.isEmpty() ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showToast("Failed to load reviews");
            }
        });

    }

    private void displayFoodDetails(DataSnapshot snapshot) {
        foodName.setText(snapshot.child("name").getValue(String.class));
        foodDescription.setText(snapshot.child("description").getValue(String.class));
        foodPrice.setText(String.format("$%.2f", snapshot.child("price").getValue(Double.class)));
        foodRating.setText(String.format("★ %.1f", snapshot.child("rating").getValue(Double.class)));
        foodCategory.setText(snapshot.child("category").getValue(String.class));
        Glide.with(this).load(snapshot.child("image").getValue(String.class)).error(R.drawable.ic_image_placeholder).into(foodImage1);
    }

    private void addComment() {
        dbReview = FirebaseDatabase.getInstance().getReference().child("Reviews").child(strFoodId);
        dialog = createCommentDialog();
        dialog.show();
    }

    private AlertDialog createCommentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.comment_dialog, null);
        builder.setView(view);

        EditText etComment = view.findViewById(R.id.edtComment);
        RatingBar ratingBar = view.findViewById(R.id.foodRating);
        Button btnSubmit = view.findViewById(R.id.btnSubmitRating);

        String currentUserId = getSharedPreferences("user_prefs", MODE_PRIVATE).getString("current_user_id", null);

        btnSubmit.setOnClickListener(v -> submitComment(etComment, ratingBar, currentUserId));
        return builder.create();
    }

    private void submitComment(EditText etComment, RatingBar ratingBar, String userId) {
        String comment = etComment.getText().toString();
        float rating = ratingBar.getRating();

        if (comment.isEmpty() || rating == 0.0f) {
            etComment.setError("Please provide a comment");
            return;
        }

        Review newReview = new Review(comment, rating, strFoodId, userId);
        dbReview.push().setValue(newReview).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                updateTotalRating(rating);
                dialog.dismiss();
            } else {
                showToast("Failed to add review");
            }
        });
    }

    private void updateTotalRating(float newRating) {
        dbReview.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                float totalRating = 0.0f;
                int count = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    totalRating += dataSnapshot.getValue(Review.class).getRating();
                    count++;
                }
                float averageRating = (count > 0) ? totalRating / count : newRating;
                dbFoodItem.child("rating").setValue(averageRating);
                loadContent();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showToast("Failed to update rating");
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
