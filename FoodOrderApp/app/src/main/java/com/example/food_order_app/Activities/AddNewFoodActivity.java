package com.example.food_order_app.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.food_order_app.Fragments.DashboardFragment;
import com.example.food_order_app.Models.FoodItem;
import com.example.food_order_app.R;
import com.example.food_order_app.Database.AppDatabase;

public class AddNewFoodActivity extends AppCompatActivity {

    private EditText etFoodName, etFoodPrice, etFoodQuantity, etFoodDescription;
    private RadioGroup radioGroupCategory;
    private Button btnAddFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_new_food);

        // Khởi tạo các view
        etFoodName = findViewById(R.id.etFoodName);
        etFoodPrice = findViewById(R.id.etFoodPrice);
        etFoodQuantity = findViewById(R.id.etFoodQuantity);
        etFoodDescription = findViewById(R.id.etFoodDescription);
        radioGroupCategory = findViewById(R.id.radioGroupCategory);
        btnAddFood = findViewById(R.id.btnAddFood);

        btnAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String name = etFoodName.getText().toString();
                    String priceString = etFoodPrice.getText().toString();
                    String quantityString = etFoodQuantity.getText().toString();
                    String description = etFoodDescription.getText().toString();
                    int selectedId = radioGroupCategory.getCheckedRadioButtonId();
                    RadioButton selectedRadioButton = findViewById(selectedId); // Sửa lỗi ở đây
                    String category = selectedRadioButton != null ? selectedRadioButton.getText().toString() : "";

//                    // Kiểm tra xem tất cả các trường có được điền không
//                    if (name.isEmpty() || priceString.isEmpty() || quantityString.isEmpty() || description.isEmpty() || category.isEmpty()) {
//                        Toast.makeText(AddNewFoodActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
//                        return;
//                    }

                    // Chuyển đổi giá và số lượng
                    double price = Double.parseDouble(priceString);
                    int quantity = Integer.parseInt(quantityString);// Sửa lỗi ở đây


                    // Tạo đối tượng FoodItem và lưu vào database
                    FoodItem foodItem = new FoodItem(category, description, "", name, price, quantity, 0, true);
                    AppDatabase.getInstance(AddNewFoodActivity.this).foodItemDao().insertFoodItem(foodItem);
                    Toast.makeText(AddNewFoodActivity.this, "Add New Food Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    setResult(Activity.RESULT_OK, intent);
                    finish();

                } catch (Exception e) {
                    Toast.makeText(AddNewFoodActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }


}
