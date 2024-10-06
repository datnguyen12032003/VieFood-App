package com.example.food_order_app.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.food_order_app.Database.AppDatabase;
import com.example.food_order_app.MainActivity;
import com.example.food_order_app.Models.User;
import com.example.food_order_app.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private Button btnRegister;
    private ImageButton btnBack, btnTogglePassword;
    private EditText etUsername, etEmail, etPhone, etAddress, etPassword;
    private boolean isPasswordVisible = false; // Biến toàn cục cho trạng thái hiển thị mật khẩu
    private DatabaseReference dbUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        dbUsers = FirebaseDatabase.getInstance().getReference("Users");

        // Khởi tạo các thành phần UI
        btnBack = findViewById(R.id.btn_back);
        btnRegister = findViewById(R.id.btn_create_account);
        etUsername = findViewById(R.id.txt_username);
        etEmail = findViewById(R.id.txt_email);
        etPhone = findViewById(R.id.txt_phone);
        etAddress = findViewById(R.id.txt_address);
        etPassword = findViewById(R.id.txt_password);
        btnTogglePassword = findViewById(R.id.btn_toggle_password);

        // Thiết lập sự kiện nhấn nút quay lại
        btnBack.setOnClickListener(view -> {
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
        });

        // Thiết lập sự kiện nhấn nút ẩn hiện mật khẩu
        btnTogglePassword.setOnClickListener(view -> togglePasswordVisibility());

        // Thiết lập sự kiện nhấn nút đăng ký
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User newUser = new User();
                newUser.setUserName(etUsername.getText().toString());
                newUser.setUserEmail(etEmail.getText().toString());
                newUser.setUserPhone(etPhone.getText().toString());
                newUser.setUserAddress(etAddress.getText().toString());
                newUser.setUserPassword(etPassword.getText().toString());
                newUser.setAdmin(false);
                createNewUser(newUser);
                dbUsers.push().setValue(newUser);
            }
        });
    }

    private void createNewUser(User newUser) {
        try {
            AppDatabase.getInstance(this).userDao().insertUser(newUser);
            // Xử lý khi tạo mới thành công
            Toast.makeText(this, "Create new user successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            // Xử lý khi tạo mới thất bại
            Toast.makeText(this, "Create new user failed", Toast.LENGTH_SHORT).show();
        }
    }

    // Hàm để ẩn hiện mật khẩu
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
