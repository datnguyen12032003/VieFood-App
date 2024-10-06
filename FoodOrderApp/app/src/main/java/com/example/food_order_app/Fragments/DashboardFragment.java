package com.example.food_order_app.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.food_order_app.Activities.AddNewFoodActivity;
import com.example.food_order_app.Adapters.FoodItemDashBoardAdapter;
import com.example.food_order_app.Database.AppDatabase;
import com.example.food_order_app.Models.FoodItem;
import com.example.food_order_app.R;

import java.util.ArrayList;
import java.util.List;

// DashboardFragment.java
public class DashboardFragment extends Fragment {

    private RecyclerView rcv;
    private FoodItemDashBoardAdapter adapter;
    private List<FoodItem> foodItemList;
    private Button btnAddNewFood;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        foodItemList = new ArrayList<>(); // Khởi tạo danh sách món ăn
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);
        initializeViews(v);
        setupRecyclerView();
        setupButtonListeners();
        loadFoodItems();
        return v;
    }

    private void initializeViews(View v) {
        rcv = v.findViewById(R.id.recyclerView);
        btnAddNewFood = v.findViewById(R.id.btnAddNewFood);
    }

    private void setupRecyclerView() {
        rcv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new FoodItemDashBoardAdapter(foodItemList, new FoodItemDashBoardAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(FoodItem foodItem) {
                Toast.makeText(getContext(), "Edit", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDeleteClick(FoodItem foodItem) {
                showDeleteConfirmationDialog(foodItem);
            }
        });
        rcv.setAdapter(adapter);
    }

    private void setupButtonListeners() {
        btnAddNewFood.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddNewFoodActivity.class);
            startActivityForResult(intent, 1);
        });
    }

    private void showDeleteConfirmationDialog(FoodItem foodItem) {
        new AlertDialog.Builder(getContext())
                .setTitle("Delete Food Item")
                .setMessage("Are you sure you want to delete this food item?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    deleteFoodItem(foodItem);
                    Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == AddNewFoodActivity.RESULT_OK) {
            loadFoodItems(); // Gọi load lại dữ liệu khi trở về từ AddNewFoodActivity
        }
    }

    public void loadFoodItems() {
        foodItemList = AppDatabase.getInstance(getContext()).foodItemDao().getAllFoodItems();
        adapter.setData(foodItemList);
    }

    public void deleteFoodItem(FoodItem foodItem) {
        AppDatabase.getInstance(getContext()).foodItemDao().deleteFoodItem(foodItem);
        loadFoodItems();
    }
}
