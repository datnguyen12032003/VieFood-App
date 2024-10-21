package com.example.food_order_app.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.food_order_app.Activities.FoodDetailActivity;
import com.example.food_order_app.Models.FoodItem;
import com.example.food_order_app.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FoodHomeAdapter extends RecyclerView.Adapter<FoodHomeAdapter.FoodViewHolder> {

    private List<FoodItem> foodItems;

    public FoodHomeAdapter(List<FoodItem> foodItems) {
        this.foodItems = foodItems;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item_home, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        FoodItem foodItem = foodItems.get(position);
        holder.foodRating.setText(String.valueOf(foodItem.getRating()));
        Glide.with(holder.itemView.getContext())
                .load(foodItem.getImage())
                .apply(new RequestOptions()
                        .override(80, 80)
                        .centerCrop()
                        .transform(new RoundedCorners(20)))
                .into(holder.foodImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(), FoodDetailActivity.class);
                String foodId = foodItem.getFoodId();
                intent.putExtra("foodId", foodId);
                intent.putExtra("foodPrice", foodItem.getPrice());

                holder.itemView.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return foodItems.size();
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        ImageView foodImage;
        TextView foodRating;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            foodImage = itemView.findViewById(R.id.image_food);
            foodRating = itemView.findViewById(R.id.text_food_rating);
        }
    }
}
