package com.example.food_order_app.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
    private RadioGroup radioGroupCategory;
    private Button btnAddFood, btnChooseImage;
    private Uri imageUri; // Biến lưu URI của hình ảnh được chọn
    private String imageUrl;
    private ImageView imageViewFood;
    private DatabaseReference foodItems = FirebaseDatabase.getInstance().getReferenceFromUrl("https://viefood-da6a0-default-rtdb.firebaseio.com/").child("FoodItems");
    private StorageReference storageReference;
    private ProgressDialog progressDialog;

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
        imageViewFood = findViewById(R.id.imgPreview);
        btnAddFood = findViewById(R.id.btnAddFood);
        btnChooseImage = findViewById(R.id.btnChooseImage);

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
                uploadImage();
            }
        });
    }

    private void uploadImage() {
        try {
            progressDialog = new ProgressDialog(AddNewFoodActivity.this);
            progressDialog.setMessage("Uploading...");
            progressDialog.show();

            storageReference = FirebaseStorage.getInstance().getReference();
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

    private void uploadFood() {
        try {
            String name = etFoodName.getText().toString();
            String priceString = etFoodPrice.getText().toString();
            String quantityString = etFoodQuantity.getText().toString();
            String description = etFoodDescription.getText().toString();
            int selectedId = radioGroupCategory.getCheckedRadioButtonId();
            RadioButton selectedRadioButton = findViewById(selectedId); // Sửa lỗi ở đây
            String category = selectedRadioButton != null ? selectedRadioButton.getText().toString() : "";

            // Chuyển đổi giá và số lượng
            double price = Double.parseDouble(priceString);
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


}
