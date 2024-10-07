package com.example.food_order_app.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import com.example.food_order_app.Fragments.HomeFragment;
import com.example.food_order_app.R;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home); // Đảm bảo bạn đã tạo layout cho activity này

        // Kiểm tra nếu savedInstanceState là null thì thêm HomeFragment
        if (savedInstanceState == null) {
            Fragment homeFragment = new HomeFragment(); // Tạo instance của HomeFragment
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, homeFragment) // Sử dụng ID từ layout
                    .commit();
        }
    }
}
