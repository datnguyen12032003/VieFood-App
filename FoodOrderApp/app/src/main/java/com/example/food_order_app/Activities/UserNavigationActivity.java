package com.example.food_order_app.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment; // Thêm dòng này


import com.example.food_order_app.UserFragments.CartFragment;
import com.example.food_order_app.UserFragments.HomeFragment;
import com.example.food_order_app.UserFragments.MenuFragment;
import com.example.food_order_app.UserFragments.ProfileFragment;
import com.example.food_order_app.R;
import com.example.food_order_app.databinding.UserActivityNavigationBinding;

public class UserNavigationActivity extends AppCompatActivity {

    UserActivityNavigationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = UserActivityNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());//dùng để set layout trong activity bằng binding.getRoot()
        // Mặc định mở HomeFragment khi Activity khởi động
        openFragment(new HomeFragment());
        // Đặt item mặc định là Home
        binding.bottomNavigationView.setSelectedItemId(R.id.home);
        
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    openFragment(new HomeFragment());
                    return true;
                case R.id.menu:
                    openFragment(new MenuFragment());
                    return true;
                case R.id.cart:
                    openFragment(new CartFragment());
                    return true;
                case R.id.profile:
                    openFragment(new ProfileFragment());
                    return true;
                case R.id.logout:
                    return true;
                default:
                    return false;
            }
        });
    }

    private void openFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout_bottom_navigation, fragment) // Thay R.id.fragment_container bằng ID của container trong layout
                .commit();
    }

}
