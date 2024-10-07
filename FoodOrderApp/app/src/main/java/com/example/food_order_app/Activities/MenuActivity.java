package com.example.food_order_app.Activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_order_app.Fragments.MenuFragment;
import com.example.food_order_app.MenuAdapter;
import com.example.food_order_app.Models.FoodItem;
import com.example.food_order_app.R;

import java.util.ArrayList;
import java.util.List;
public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Nhận loại món ăn từ Intent
        String category = getIntent().getStringExtra("category");

        // Truyền loại món ăn cho MenuFragment
        Bundle bundle = new Bundle();
        bundle.putString("category", category);
        MenuFragment menuFragment = new MenuFragment();
        menuFragment.setArguments(bundle);

        // Hiển thị MenuFragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, menuFragment)
                .commit();
    }
}
