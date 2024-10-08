package com.example.food_order_app.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.food_order_app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText currentPasswordEditText, newPasswordEditText, confirmNewPasswordEditText;
    private Button changePasswordButton;
    private DatabaseReference dbUsers;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        currentPasswordEditText = findViewById(R.id.currentPasswordEditText);
        newPasswordEditText = findViewById(R.id.newPasswordEditText);
        confirmNewPasswordEditText = findViewById(R.id.confirmNewPasswordEditText);
        changePasswordButton = findViewById(R.id.changePasswordButton);
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        currentUserId = prefs.getString("current_user_id", null);
        dbUsers = FirebaseDatabase.getInstance().getReference("Users");

        changePasswordButton.setOnClickListener(v -> {
            String currentPassword = currentPasswordEditText.getText().toString();
            String newPassword = newPasswordEditText.getText().toString();
            String confirmNewPassword = confirmNewPasswordEditText.getText().toString();

            if (validateInputs(currentPassword, newPassword, confirmNewPassword)) {
                updatePassword(currentPassword, newPassword);
            }
        });
    }

    private boolean validateInputs(String currentPassword, String newPassword, String confirmNewPassword) {
        if (newPassword.equals(confirmNewPassword)) {
            return true;
        } else {
            Toast.makeText(this, "New password and confirm password do not match", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void updatePassword(String currentPassword, String newPassword) {
        dbUsers.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String storedPassword = snapshot.child("userPassword").getValue(String.class);
                    if (storedPassword != null && storedPassword.equals(currentPassword)) {
                        dbUsers.child(currentUserId).child("userPassword").setValue(newPassword)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(ChangePasswordActivity.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                                    logoutAndRedirect();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(ChangePasswordActivity.this, "Failed to change password: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        Toast.makeText(ChangePasswordActivity.this, "Current password is incorrect", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ChangePasswordActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ChangePasswordActivity.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void logoutAndRedirect() {
        getSharedPreferences("user_prefs", MODE_PRIVATE).edit().remove("current_user_id").apply();
        Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
