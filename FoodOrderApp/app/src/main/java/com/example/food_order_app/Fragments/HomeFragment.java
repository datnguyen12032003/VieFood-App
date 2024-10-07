package com.example.food_order_app.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.food_order_app.R;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Tìm các button và thiết lập sự kiện bấm
        Button btnDishes = view.findViewById(R.id.btnDishes);
        Button btnPizza = view.findViewById(R.id.btnPizza);
        Button btnBurger = view.findViewById(R.id.btnBurger);
        Button btnDrink = view.findViewById(R.id.btnDrink);
        Button btnDessert = view.findViewById(R.id.btnDessert);

        // Thiết lập sự kiện bấm cho từng button để mở MenuFragment với danh mục tương ứng
        btnDishes.setOnClickListener(v -> openMenuFragment("Dishes"));
        btnPizza.setOnClickListener(v -> openMenuFragment("Pizza"));
        btnBurger.setOnClickListener(v -> openMenuFragment("Burger"));
        btnDrink.setOnClickListener(v -> openMenuFragment("Drinks"));
        btnDessert.setOnClickListener(v -> openMenuFragment("Desserts"));

        return view;
    }

    // Phương thức để mở MenuFragment và truyền danh mục món ăn
    private void openMenuFragment(String category) {
        // Tạo đối tượng MenuFragment và truyền tham số
        MenuFragment menuFragment = new MenuFragment();
        Bundle args = new Bundle();
        args.putString("category", category);  // Truyền danh mục món ăn
        menuFragment.setArguments(args);

        // Thay thế fragment hiện tại bằng MenuFragment
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, menuFragment);
        transaction.addToBackStack(null);  // Để có thể quay lại fragment trước đó
        transaction.commit();
    }
}
