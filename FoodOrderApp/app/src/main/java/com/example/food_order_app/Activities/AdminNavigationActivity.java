package com.example.food_order_app.Activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.food_order_app.DashboardFragment;
import com.example.food_order_app.Fragments.CartFragment;
import com.example.food_order_app.Fragments.HomeFragment;
import com.example.food_order_app.Fragments.MenuFragment;
import com.example.food_order_app.Fragments.ProfileFragment;
import com.example.food_order_app.R;
import com.example.food_order_app.databinding.ActivityAdminNavigationBinding;
import com.example.food_order_app.databinding.ActivityNavigationBinding;

public class AdminNavigationActivity extends AppCompatActivity {

    ActivityAdminNavigationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());//dùng để set layout trong activity bằng binding.getRoot()
        // Mặc định mở HomeFragment khi Activity khởi động
        openFragment(new DashboardFragment());
        // Đặt item mặc định là Home
        binding.adminBottomNavigationView.setSelectedItemId(R.id.dashboard);

        binding.adminBottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.dashboard:
                    openFragment(new DashboardFragment());
                    return true;
                case R.id.menu:
                    return true;
                case R.id.profile:
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