package com.example.food_order_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_order_app.Models.FoodItem;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    private List<FoodItem> foodItemList;

    public MenuAdapter(List<FoodItem> foodItemList) {
        this.foodItemList = foodItemList;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        FoodItem foodItem = foodItemList.get(position);
        holder.foodName.setText(foodItem.getName());
        holder.foodPrice.setText(String.valueOf(foodItem.getPrice()));
        // Bạn có thể thêm hình ảnh và các thông tin khác nếu muốn
    }

    @Override
    public int getItemCount() {
        return foodItemList.size();
    }

    // Phương thức cập nhật danh sách món ăn
    public void updateFoodItems(List<FoodItem> newFoodItems) {
        if (newFoodItems != null) {
            this.foodItemList.clear();
            this.foodItemList.addAll(newFoodItems);
            notifyDataSetChanged();
        }
    }

    public class MenuViewHolder extends RecyclerView.ViewHolder {
        TextView foodName, foodPrice;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.food_name);
            foodPrice = itemView.findViewById(R.id.food_price);
        }
    }
}
