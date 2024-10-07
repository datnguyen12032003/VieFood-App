package com.example.food_order_app.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.food_order_app.MainActivity;
import com.example.food_order_app.Models.User;
import com.example.food_order_app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin;
    private ImageButton btnBack, btnTogglePassword;
    private EditText etEmail, etPassword;
    private boolean isPasswordVisible = false; // Biến toàn cục cho trạng thái hiển thị mật khẩu
    private DatabaseReference dbUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        dbUsers = FirebaseDatabase.getInstance().getReference("Users");

        btnLogin = findViewById(R.id.btn_login);
        btnBack = findViewById(R.id.btn_back);
        etEmail = findViewById(R.id.txt_email);
        etPassword = findViewById(R.id.txt_password);
        btnTogglePassword = findViewById(R.id.btn_toggle_password);

        // Thiết lập sự kiện nhấn nút quay lại
        btnBack.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        });

        // Thiết lập sự kiện nhấn nút ẩn hiện mật khẩu
        btnTogglePassword.setOnClickListener(view -> togglePasswordVisibility());

        // Thiết lập sự kiện nhấn nút đăng nhập
        btnLogin.setOnClickListener(view -> {
            // Xử lý đăng nhập ở đây
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();
            User user = new User(email, password);
            login(user);
        });

    }

    private void login(User user) {
        try {
            dbUsers.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (dataSnapshot.child("userEmail").getValue().equals(user.getUserEmail()) && dataSnapshot.child("userPassword").getValue().equals(user.getUserPassword())) {
                            if (dataSnapshot.child("admin").getValue().equals(true)) {
                                Intent intent = new Intent(LoginActivity.this, AdminNavigationActivity.class);
                                startActivity(intent);
                                Toast.makeText(LoginActivity.this, "Login successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Intent intent = new Intent(LoginActivity.this, NavigationActivity.class);
                                startActivity(intent);
                                Toast.makeText(LoginActivity.this, "Login successfully", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception ex) {
            // Xử lý khi đăng nhập thất bại
            Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            btnTogglePassword.setImageResource(R.drawable.ic_visibility_off);
        } else {
            etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            btnTogglePassword.setImageResource(R.drawable.ic_visibility); // Thay đổi icon
        }
        etPassword.setSelection(etPassword.length()); // Đưa con trỏ về cuối
        isPasswordVisible = !isPasswordVisible; // Đổi trạng thái
    }
}