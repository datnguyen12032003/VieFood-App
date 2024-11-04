package com.example.food_order_app.Fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.food_order_app.Activities.NavigationActivity;
import com.example.food_order_app.Adapters.FoodHomeAdapter;
import com.example.food_order_app.Models.FoodItem;
import com.example.food_order_app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private FoodHomeAdapter foodHomeAdapter;
    private List<FoodItem> foodItems;
    private ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        foodItems = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_popular);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        TextView seeAll = view.findViewById(R.id.see_all);
        progressBar = view.findViewById(R.id.progress_bar);
        view.findViewById(R.id.dishes).setOnClickListener(v -> openMenuFragment("Dishes"));
        view.findViewById(R.id.pizza).setOnClickListener(v -> openMenuFragment("Pizza"));
        view.findViewById(R.id.burger).setOnClickListener(v -> openMenuFragment("Burger"));
        view.findViewById(R.id.drinks).setOnClickListener(v -> openMenuFragment("Drinks"));
        view.findViewById(R.id.dessert).setOnClickListener(v -> openMenuFragment("Dessert"));

        seeAll.setOnClickListener(v -> openMenuFragment(null));

        fetchFoodItems();
        return view;
    }

    private void openMenuFragment(String category) {
        MenuFragment menuFragment = new MenuFragment();
        if (category != null) {
            Bundle args = new Bundle();
            args.putString("selected_category", category);
            menuFragment.setArguments(args);
            Log.d("MenuFragment", "Selected Category: " + category);
        }
        Log.d("HomeFragment", "Attempting to open MenuFragment 111");
        ((NavigationActivity) getActivity()).openFragment(menuFragment);
//        ((NavigationActivity) getActivity()).binding.bottomNavigationView.setSelectedItemId(R.id.menu);
    }


    private void fetchFoodItems() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        DatabaseReference dbFoodItems = FirebaseDatabase.getInstance().getReference("FoodItems");
        dbFoodItems.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                foodItems.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    FoodItem foodItem = snapshot.getValue(FoodItem.class);
                    foodItems.add(foodItem);
                }
                Collections.sort(foodItems, new Comparator<FoodItem>() {
                    @Override
                    public int compare(FoodItem o1, FoodItem o2) {
                        return Integer.compare(o2.getQuantity(), o1.getQuantity());
                    }
                });

                if (foodItems.size() > 8) {
                    foodItems = foodItems.subList(0, 8);
                }

                foodHomeAdapter = new FoodHomeAdapter(foodItems);
                recyclerView.setAdapter(foodHomeAdapter);
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("HomeFragment", "Database error: " + databaseError.getMessage());
            }
        });
    }
}
