package com.example.food_order_app.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.food_order_app.Database.UserDatabase;
import com.example.food_order_app.Fragments.HomeFragment;
import com.example.food_order_app.MainActivity;
import com.example.food_order_app.Models.User;
import com.example.food_order_app.R;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin;
    private ImageButton btnBack, btnTogglePassword;
    private EditText etEmail, etPassword;
    private boolean isPasswordVisible = false; // Biến toàn cục cho trạng thái hiển thị mật khẩu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

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
            boolean login = login(user);
            if(login){
                Intent intent = new Intent(LoginActivity.this, NavigationActivity.class);
                startActivity(intent);
            }
        });

    }

    private boolean login(User user) {
        try {
            User getuser = UserDatabase.getInstance(this).userDao().getUserByEmailAndPassword(user.getUserEmail(), user.getUserPassword());
            if (getuser == null) {
                throw new Exception();
            }

            Toast.makeText(this, "Login successfully", Toast.LENGTH_SHORT).show();
            // Xử lý khi đăng nhập thành công
            return true;
        } catch (Exception ex) {
            // Xử lý khi đăng nhập thất bại

            Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show();
            return false;
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