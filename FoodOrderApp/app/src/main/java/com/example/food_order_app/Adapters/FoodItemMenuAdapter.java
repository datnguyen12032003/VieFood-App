package com.example.food_order_app.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.food_order_app.Models.FoodItem;
import com.example.food_order_app.R;

import java.util.List;

public class FoodItemMenuAdapter extends RecyclerView.Adapter<FoodItemMenuAdapter.FoodItemViewHolder> {

    private List<FoodItem> foodItemList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onAddClick(FoodItem foodItem);
    }

    public FoodItemMenuAdapter(List<FoodItem> foodItemList, OnItemClickListener onItemClickListener) {
        this.foodItemList = foodItemList;
        this.onItemClickListener = onItemClickListener;
    }

    public void setData(List<FoodItem> foodItemList) {
        this.foodItemList = foodItemList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FoodItemMenuAdapter.FoodItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_food_item, parent, false);
        return new FoodItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodItemMenuAdapter.FoodItemViewHolder holder, int position) {
        FoodItem foodItem = foodItemList.get(position);
        if (foodItem == null) {
            return;
        }
        Glide.with(holder.itemView.getContext()).load(foodItem.getImage()).error(R.drawable.ic_image_placeholder).into(holder.foodImage);
        holder.foodName.setText(foodItem.getName());
        holder.foodDescription.setText(foodItem.getDescription());
        holder.foodPrice.setText(String.format("$%.2f", foodItem.getPrice()));
        holder.foodRating.setText(String.format("★ %.1f", foodItem.getRating()));
        //Xử lý sự kiện nút "Add"
        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onAddClick(foodItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (foodItemList != null) {
            return foodItemList.size();
        } else {
            return 0;
        }
    }

    public class FoodItemViewHolder extends RecyclerView.ViewHolder {
        private TextView foodName, foodDescription, foodPrice, foodRating;
        private ImageView foodImage;
        private ImageButton btnAdd;

        public FoodItemViewHolder(@NonNull View itemView) {
            super(itemView);

            foodName = itemView.findViewById(R.id.menu_foodName);
            foodDescription = itemView.findViewById(R.id.menu_foodDescription);
            foodPrice = itemView.findViewById(R.id.menu_foodPrice);
            foodRating = itemView.findViewById(R.id.menu_foodRating);
            foodImage = itemView.findViewById(R.id.menu_foodImage);
            btnAdd = itemView.findViewById(R.id.btn_add);
        }
    }
}
