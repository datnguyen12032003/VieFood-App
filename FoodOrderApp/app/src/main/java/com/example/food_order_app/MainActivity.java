package com.example.food_order_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.food_order_app.Activities.AdminNavigationActivity;
import com.example.food_order_app.Activities.LoginActivity;
import com.example.food_order_app.Activities.NavigationActivity;
import com.example.food_order_app.Activities.RegisterActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnCreateAccount;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Kiểm tra trạng thái đăng nhập
        String currentUserId = getSharedPreferences("user_prefs", MODE_PRIVATE)
                .getString("current_user_id", null);
        boolean isAdmin = getSharedPreferences("user_prefs", MODE_PRIVATE)
                .getBoolean("admin", false);

        if (currentUserId != null) {
            redirectToAppropriateActivity(isAdmin);
            return;
        }

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        btnCreateAccount = findViewById(R.id.btn_create_account);
        btnLogin = findViewById(R.id.btn_login);

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }


    private void redirectToAppropriateActivity(boolean isAdmin) {
        Intent intent;
        if (isAdmin) {
            intent = new Intent(MainActivity.this, AdminNavigationActivity.class);
        } else {
            intent = new Intent(MainActivity.this, NavigationActivity.class);
        }
        startActivity(intent);
        finish();
    }
}