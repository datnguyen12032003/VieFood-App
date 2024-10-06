package com.example.food_order_app.Activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.food_order_app.Fragments.DashboardFragment;
import com.example.food_order_app.R;
import com.example.food_order_app.databinding.ActivityAdminNavigationBinding;

public class AdminNavigationActivity extends AppCompatActivity {

    ActivityAdminNavigationBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Mặc định mở DashboardFragment khi Activity khởi động
        openFragment(new DashboardFragment());
        // Đặt item mặc định là Dashboard
        binding.adminBottomNavigationView.setSelectedItemId(R.id.dashboard);

        // Thiết lập sự kiện chọn item trong Bottom Navigation
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
                .replace(R.id.frame_layout_bottom_navigation, fragment)
                .commit();
    }




}
