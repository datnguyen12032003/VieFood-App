package com.example.food_order_app.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.food_order_app.Activities.AddNewFoodActivity;
import com.example.food_order_app.Activities.AdminNavigationActivity;
import com.example.food_order_app.Adapters.FoodItemDashBoardAdapter;
import com.example.food_order_app.Database.AppDatabase;
import com.example.food_order_app.Models.FoodItem;
import com.example.food_order_app.R;

import java.util.ArrayList; // Đảm bảo import đúng List
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);
        rcv = v.findViewById(R.id.recyclerView);
        btnAddNewFood = v.findViewById(R.id.btnAddNewFood);

        // Set RecyclerView layout manager and adapter
        rcv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new FoodItemDashBoardAdapter(foodItemList, new FoodItemDashBoardAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(FoodItem foodItem) {
                Toast.makeText(getContext(), "Edit", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDeleteClick(FoodItem foodItem) {
                Toast.makeText(getContext(), "Delete", Toast.LENGTH_SHORT).show();
            }
        });
        rcv.setAdapter(adapter); // Set adapter

        loadFoodItems();

        btnAddNewFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Khởi động AddNewFoodActivity và chờ kết quả
                if (getActivity() instanceof AdminNavigationActivity) {
                    Intent intent = new Intent(getContext(), AddNewFoodActivity.class);
                    startActivity(intent);
                }
            }
        });

        return v;
    }

    public void loadFoodItems() {
        // Lấy danh sách món ăn từ cơ sở dữ liệu
        foodItemList.clear(); // Xóa danh sách hiện tại nếu có
        foodItemList.addAll(AppDatabase.getInstance(getContext()).foodItemDao().getAllFoodItems()); // Giả định bạn có phương thức này
        adapter.notifyDataSetChanged(); // Cập nhật adapter
    }
}

