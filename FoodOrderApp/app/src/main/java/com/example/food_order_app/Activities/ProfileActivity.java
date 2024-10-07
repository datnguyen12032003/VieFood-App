package com.example.food_order_app.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.food_order_app.Models.User;
import com.example.food_order_app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private static final int EDIT_PROFILE_REQUEST_CODE = 1;
    private TextView userNameTextView;
    private TextView userPhoneTextView;
    private TextView userAddressTextView;
    private DatabaseReference dbUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        userNameTextView = findViewById(R.id.userName);
        userPhoneTextView = findViewById(R.id.userPhone);
        userAddressTextView = findViewById(R.id.userAddress);
        ImageButton backButton = findViewById(R.id.backButton);
        Button editButton = findViewById(R.id.editButton);
        dbUsers = FirebaseDatabase.getInstance().getReference("Users");
        fetchUserData();
        backButton.setOnClickListener(v -> finish());
        editButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            startActivityForResult(intent, EDIT_PROFILE_REQUEST_CODE);
        });
    }

    private void fetchUserData() {
        String userId = getSharedPreferences("user_prefs", MODE_PRIVATE)
                .getString("current_user_id", null);

        if (userId != null) {
            dbUsers.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        User currentUser = dataSnapshot.getValue(User.class);
                        if (currentUser != null) {
                            userNameTextView.setText(currentUser.getUserName());
                            userPhoneTextView.setText(currentUser.getUserPhone());
                            userAddressTextView.setText(currentUser.getUserAddress());
                        } else {
                            showToast("User not found");
                        }
                    } else {
                        showToast("User not found");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    showToast("Failed to fetch user data");
                }
            });
        } else {
            showToast("No user found");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_PROFILE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            String updatedName = data.getStringExtra("updatedName");
            String updatedPhone = data.getStringExtra("updatedPhone");
            String updatedAddress = data.getStringExtra("updatedAddress");
            userNameTextView.setText(updatedName);
            userPhoneTextView.setText(updatedPhone);
            userAddressTextView.setText(updatedAddress);
        }
    }


    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
