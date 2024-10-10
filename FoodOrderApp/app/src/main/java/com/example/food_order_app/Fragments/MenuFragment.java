package com.example.food_order_app.Fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.food_order_app.Adapters.FoodItemMenuAdapter;
import com.example.food_order_app.Models.Cart;
import com.example.food_order_app.Models.FoodItem;
import com.example.food_order_app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MenuFragment extends Fragment {

    private RecyclerView rcv;
    private FoodItemMenuAdapter adapter;
    private List<FoodItem> foodItemList;
    private DatabaseReference dbFoodItems;
    private Button btn_dishes, btn_pizza, btn_burger, btn_drinks, btn_dessert;
    private TextView txt_title;
    private ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbFoodItems = FirebaseDatabase.getInstance().getReferenceFromUrl("https://viefood-da6a0-default-rtdb.firebaseio.com/").child("FoodItems");
        foodItemList = new ArrayList<>(); // Khởi tạo danh sách món ăn
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_menu, container, false);
        initializeViews(v);
        setupRecyclerView();
        loadFoodItems();
        resetButtonColors();
        return v;
    }

    private void loadFoodItems() {
        progressBar.setVisibility(View.VISIBLE);
        rcv.setVisibility(View.GONE); // Ẩn RecyclerView

        dbFoodItems.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                foodItemList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    FoodItem foodItem = dataSnapshot.getValue(FoodItem.class);
                    if (foodItem != null) {
                        foodItem.setFoodId(dataSnapshot.getKey());
                        foodItemList.add(foodItem);
                    }
                }
                adapter.setData(foodItemList);  // Ẩn ProgressBar và hiển thị RecyclerView
                progressBar.setVisibility(View.GONE);
                rcv.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load food items", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE); // Ẩn ProgressBar nếu có lỗi
            }
        });
    }

    private void setupRecyclerView() {
        rcv.setLayoutManager(new LinearLayoutManager(getContext()));
        String userId = getActivity().getSharedPreferences("user_prefs", getActivity().MODE_PRIVATE)
                .getString("current_user_id", null);
        adapter = new FoodItemMenuAdapter(foodItemList, new FoodItemMenuAdapter.OnItemClickListener() {
            @Override
            public void onAddClick(FoodItem foodItem) {
                String foodItemId = foodItem.getFoodId();
                Cart newCart = new Cart(userId, foodItem.getPrice(), 1, foodItemId);
                DatabaseReference dbCart = FirebaseDatabase.getInstance().getReference("Cart");
                dbCart.push().setValue(newCart);
                Toast.makeText(getContext(), "Added to cart", Toast.LENGTH_SHORT).show();
            }
        });
        rcv.setAdapter(adapter);
    }

    private void initializeViews(View v) {
        rcv = v.findViewById(R.id.rcv_menu);
        btn_dishes = v.findViewById(R.id.btn_dishes);
        btn_pizza = v.findViewById(R.id.btn_pizza);
        btn_burger = v.findViewById(R.id.btn_burger);
        btn_drinks = v.findViewById(R.id.btn_drinks);
        btn_dessert = v.findViewById(R.id.btn_dessert);
        txt_title = v.findViewById(R.id.txt_title);
        progressBar = v.findViewById(R.id.progress_bar);

        btn_dishes.setOnClickListener(v1 -> selectCategory("Dishes"));
        btn_pizza.setOnClickListener(v1 -> selectCategory("Pizza"));
        btn_burger.setOnClickListener(v1 -> selectCategory("Burger"));
        btn_drinks.setOnClickListener(v1 -> selectCategory("Drinks"));
        btn_dessert.setOnClickListener(v1 -> selectCategory("Dessert"));
    }

    private void selectCategory(String category) {
        resetButtonColors(); // Đặt lại màu sắc của tất cả các nút
        setSelectedButtonColor(category); // Đổi màu cho nút được chọn
        filterFoodItemsByCategory(category);
        // Lọc danh sách món ăn theo category
    }

    private void setSelectedButtonColor(String category) {
        Button selectedButton;
        switch (category) {
            case "Dishes":
                selectedButton = btn_dishes;
                break;
            case "Pizza":
                selectedButton = btn_pizza;
                break;
            case "Burger":
                selectedButton = btn_burger;
                break;
            case "Drinks":
                selectedButton = btn_drinks;
                break;
            case "Dessert":
                selectedButton = btn_dessert;
                break;
            default:
                return;
        }
        selectedButton.setBackgroundResource(R.drawable.button_state);
        selectedButton.setTextColor(getResources().getColor(R.color.white));
        txt_title.setText(category);
    }

    private void resetButtonColors() {
        int defaultColor = getResources().getColor(R.color.white);
        btn_dishes.setBackgroundColor(defaultColor);
        btn_dishes.setTextColor(getContext().getColor(R.color.black));
        btn_dishes.setBackgroundResource(R.drawable.button_category_selector);

        btn_pizza.setBackgroundColor(defaultColor);
        btn_pizza.setTextColor(getContext().getColor(R.color.black));
        btn_pizza.setBackgroundResource(R.drawable.button_category_selector);

        btn_burger.setBackgroundColor(defaultColor);
        btn_burger.setTextColor(getContext().getColor(R.color.black));
        btn_burger.setBackgroundResource(R.drawable.button_category_selector);

        btn_drinks.setBackgroundColor(defaultColor);
        btn_drinks.setTextColor(getContext().getColor(R.color.black));
        btn_drinks.setBackgroundResource(R.drawable.button_category_selector);

        btn_dessert.setBackgroundColor(defaultColor);
        btn_dessert.setTextColor(getContext().getColor(R.color.black));
        btn_dessert.setBackgroundResource(R.drawable.button_category_selector);
    }

    // Hàm lọc danh sách món ăn theo category
    private void filterFoodItemsByCategory(String category) {
        List<FoodItem> filteredList = new ArrayList<>();
        txt_title.setText(category);
        for (FoodItem foodItem : foodItemList) {
            if (foodItem.getCategory().equalsIgnoreCase(category)) {
                filteredList.add(foodItem);
            }
        }
        adapter.setData(filteredList);
    }
}
