package com.example.food_order_app.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.food_order_app.Models.FoodItem;
import com.example.food_order_app.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddNewFoodActivity extends AppCompatActivity {

    private EditText etFoodName, etFoodPrice, etFoodQuantity, etFoodDescription;
    private Spinner spinnerCategory;
    private Button btnAddFood, btnChooseImage;
    private Uri imageUri; // Biến lưu URI của hình ảnh được chọn
    private String imageUrl;
    private ImageView imageViewFood;
    private DatabaseReference foodItems = FirebaseDatabase.getInstance().getReferenceFromUrl("https://viefood-da6a0-default-rtdb.firebaseio.com/").child("FoodItems");
    private StorageReference storageReference;
    private ProgressDialog progressDialog;
    private String name, price, quantity, description, category;
    private ImageView icBack;

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
        spinnerCategory = findViewById(R.id.spinnerCategory);
        imageViewFood = findViewById(R.id.imgPreview);
        btnAddFood = findViewById(R.id.btnAddFood);
        btnChooseImage = findViewById(R.id.btnChooseImage);
        icBack = findViewById(R.id.icBack);


        icBack.setOnClickListener(v -> {
            finish();
        });

        // Tạo danh sách danh mục cho Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.food_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        // Xử lý sự kiện chọn danh mục
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                category = ""; // Hoặc thiết lập giá trị mặc định
            }
        });

        // Xử lý sự kiện khi chọn ảnh
        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        btnAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkFoodExist();
            }
        });
    }


    private void init() {
        name = etFoodName.getText().toString().trim();
        price = etFoodPrice.getText().toString().trim();
        quantity = etFoodQuantity.getText().toString().trim();
        description = etFoodDescription.getText().toString().trim();
        category = spinnerCategory.getSelectedItem().toString();
    }

    private void uploadImage() {
        try {
            progressDialog = new ProgressDialog(AddNewFoodActivity.this);
            progressDialog.setMessage("Uploading...");
            progressDialog.show();

            storageReference = FirebaseStorage.getInstance().getReference("foods");
            StorageReference fileReference = storageReference.child(System.currentTimeMillis() + ".jpg");
            fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //lấy url
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            //Url
                            imageUrl = uri.toString();
                            uploadFood();
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddNewFoodActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(AddNewFoodActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 100);
    }

    // Xử lý kết quả sau khi chọn ảnh
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageViewFood.setImageURI(imageUri);
        }
    }

    private void checkFoodExist() {
        if (!validateInput()) {
            return; // Dừng nếu không hợp lệ
        }

        String name = etFoodName.getText().toString();
        foodItems.orderByChild("name").equalTo(name).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult() != null && task.getResult().exists()) {
                    // Món ăn đã tồn tại
                    Toast.makeText(AddNewFoodActivity.this, "Food item already exists!", Toast.LENGTH_SHORT).show();
                } else {
                    // Món ăn chưa tồn tại, gọi hàm uploadFood để thêm món ăn mới
                    uploadImage();
                }
            } else {
                // Xử lý lỗi nếu truy vấn không thành công
                Toast.makeText(AddNewFoodActivity.this, "Error checking food existence: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void uploadFood() {
        try {
            init();
            String priceString = etFoodPrice.getText().toString();
            String quantityString = etFoodQuantity.getText().toString();

            // Chuyển đổi giá và số lượng
            Long price = Long.parseLong(priceString);
            int quantity = Integer.parseInt(quantityString);// Sửa lỗi ở đây
            if (imageUri == null) {
                Toast.makeText(AddNewFoodActivity.this, "Please choose an image", Toast.LENGTH_SHORT).show();
                return;
            }
            // Tạo đối tượng FoodItem và lưu vào database
            FoodItem foodItem = new FoodItem(category, description, imageUrl, name, price, quantity, 0, true);
            foodItems.push().setValue(foodItem);

            Toast.makeText(AddNewFoodActivity.this, "Add New Food Successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            setResult(Activity.RESULT_OK, intent);
            finish();
        } catch (Exception e) {
            Toast.makeText(AddNewFoodActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    private boolean validateInput() {
        init();
        String priceString = price;
        String quantityString = quantity;

        // Kiểm tra tên món ăn
        if (name.isEmpty()) {
            Toast.makeText(this, "Food name is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Kiểm tra giá
        if (priceString.isEmpty()) {
            Toast.makeText(this, "Price is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Kiểm tra định dạng giá (có thể là số nguyên hoặc số thập phân, có dấu phẩy hoặc dấu chấm)
        if (!priceString.matches("^[0-9]+(\\.[0-9]{1,2})?$")) {
            Toast.makeText(this, "Invalid price format. Use numbers only, e.g., 10000 or 10000.99", Toast.LENGTH_SHORT).show();
            return false;
        }

        double price;
        try {
            price = Double.parseDouble(priceString);
            if (price <= 0) {
                Toast.makeText(this, "Price must be greater than 0", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid price format", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Kiểm tra số lượng
        if (quantityString.isEmpty()) {
            Toast.makeText(this, "Quantity is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityString);
            if (quantity <= 0) {
                Toast.makeText(this, "Quantity must be greater than 0", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid quantity format", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Kiểm tra mô tả
        if (description.isEmpty()) {
            Toast.makeText(this, "Description is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Kiểm tra danh mục
        if (category.isEmpty()) {
            Toast.makeText(this, "Category is required", Toast.LENGTH_SHORT).show();
            return false;
        }


        // Kiểm tra hình ảnh
        if (imageUri == null) {
            Toast.makeText(this, "Please choose an image", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true; // Tất cả đều hợp lệ
    }


}
