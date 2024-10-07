package com.example.food_order_app.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_order_app.MenuAdapter;
import com.example.food_order_app.Models.FoodItem;
import com.example.food_order_app.R;

import java.util.ArrayList;
import java.util.List;

public class MenuFragment extends Fragment {

    private RecyclerView recyclerView;
    private MenuAdapter menuAdapter;
    private FoodViewModel foodViewModel;
    private String category;  // Thêm biến category ở đây

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        // Lấy dữ liệu từ Bundle
        Bundle args = getArguments();
        if (args != null) {
            category = args.getString("category");
            TextView textView = view.findViewById(R.id.textViewCategory);
            textView.setText(category);  // Hiển thị danh mục
        }

        // Khởi tạo RecyclerView và Adapter
        recyclerView = view.findViewById(R.id.recycler_view_menu);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        menuAdapter = new MenuAdapter(new ArrayList<>());
        recyclerView.setAdapter(menuAdapter);

        // Khởi tạo FoodViewModel
        foodViewModel = new ViewModelProvider(this).get(FoodViewModel.class);

        // Lấy danh sách món ăn từ cơ sở dữ liệu dựa trên category
        if (category != null) {
            foodViewModel.getFoodItemsByCategory(category).observe(getViewLifecycleOwner(), new Observer<List<FoodItem>>() {
                @Override
                public void onChanged(List<FoodItem> foodItems) {
                    menuAdapter.updateFoodItems(foodItems); // Gọi phương thức để cập nhật dữ liệu trong adapter
                }
            });
        }

        return view;
    }
}

