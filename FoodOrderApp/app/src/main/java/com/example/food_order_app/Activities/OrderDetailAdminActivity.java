package com.example.food_order_app.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.food_order_app.Adapters.OrderAdapter;
import com.example.food_order_app.Models.Cart;
import com.example.food_order_app.Models.User;
import com.example.food_order_app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class OrderDetailAdminActivity extends AppCompatActivity {

    private RecyclerView recyclerViewOrderDetails;
    private OrderAdapter orderAdapter;
    private TextView orderIdTextView, orderStatusTextView, orderTotalTextView, orderAddressTextView, orderPhoneTextview, orderNoteTextView, orderDateTextview;
    private TextView orderSubtotalTextView, orderDeliveryTextView, orderDiscountTextView;
    private DatabaseReference dbCart;
    private Button updateStatusButton, reorderButton;
    private static final int ORDER_DETAIL_REQUEST_CODE = 1;

    private ImageView icBack;
    private boolean isAdmin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail_admin);

        isAdmin = getSharedPreferences("user_prefs", MODE_PRIVATE).getBoolean("admin", false);
        Intent intent = new Intent(this, OrderDetailAdminActivity.class);
        startActivityForResult(intent, ORDER_DETAIL_REQUEST_CODE);

        String orderId = getIntent().getStringExtra("orderId");
        String orderStatus = getIntent().getStringExtra("orderStatus");
        double orderTotal = getIntent().getDoubleExtra("orderTotal", 0.0);
        String orderPhone = getIntent().getStringExtra("orderPhone");
        String orderAddress = getIntent().getStringExtra("orderAddress");
        String orderNote = getIntent().getStringExtra("orderNote");
        String orderDate = getIntent().getStringExtra("orderDate");
        List<Cart> orderedItems = (List<Cart>) getIntent().getSerializableExtra("orderedItems");

        icBack = findViewById(R.id.ic_back);

        reorderButton = findViewById(R.id.reorderButton);
        updateStatusButton = findViewById(R.id.updateStatusButton);
        if (isAdmin) {
            updateStatusButton.setVisibility(View.VISIBLE);
            reorderButton.setVisibility(View.GONE);
        } else {
            updateStatusButton.setVisibility(View.GONE);
            reorderButton.setVisibility(View.VISIBLE);
        }

        icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        orderIdTextView = findViewById(R.id.orderId);
        orderStatusTextView = findViewById(R.id.orderStatus);
        orderTotalTextView = findViewById(R.id.orderTotal);
        orderAddressTextView = findViewById(R.id.orderAddress);
        orderPhoneTextview = findViewById(R.id.orderPhone);
        orderNoteTextView = findViewById(R.id.orderNote);
        orderDateTextview = findViewById(R.id.orderDate);
        orderSubtotalTextView = findViewById(R.id.orderSubtotal);
        orderDeliveryTextView = findViewById(R.id.orderDelivery);
        orderDiscountTextView = findViewById(R.id.orderDiscount);

        orderIdTextView.setText("Order ID: " + orderId.replace("-", ""));
        orderStatusTextView.setText("Status: " + orderStatus);
        orderAddressTextView.setText("Address: " + orderAddress);
        orderPhoneTextview.setText("Phone: " + orderPhone);
        orderDateTextview.setText("Date: " + formatDate(orderDate));
        if (orderNote == null || orderNote.trim().isEmpty()) {
            orderNoteTextView.setText("Note: No Note");
        } else {
            orderNoteTextView.setText("Note: " + orderNote);
        }

        updateStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUpdateStatusDialog(orderId);
            }
        });
        double subtotal = 0;
        double discount = 0;
        double deliveryFee = 10000;
        int totalQuantity = 0;
        Set<String> uniqueItems = new HashSet<>();

        for (Cart cartItem : orderedItems) {
            subtotal += cartItem.getTotal_price();
            totalQuantity += cartItem.getQuantity();
            uniqueItems.add(cartItem.getFoodId());
        }

        if (totalQuantity >= 3 || uniqueItems.size() >= 3) {
            discount = subtotal * 0.1;
        }

        double totalAmount = subtotal - discount + deliveryFee;



        orderTotalTextView.setText("Total: " + formatCurrency(totalAmount));
        orderSubtotalTextView.setText("Subtotal: " + formatCurrency(subtotal));
        orderDeliveryTextView.setText("Delivery Fee: " + formatCurrency(deliveryFee));
        orderDiscountTextView.setText("Discount: -" + formatCurrency(discount));

        recyclerViewOrderDetails = findViewById(R.id.recyclerView_order_details);
        recyclerViewOrderDetails.setLayoutManager(new LinearLayoutManager(this));
        orderAdapter = new OrderAdapter(orderedItems);
        recyclerViewOrderDetails.setAdapter(orderAdapter);
    }



    private String formatDate(String orderDate) {
        long timestamp = Long.parseLong(orderDate);
        Date date = new Date(timestamp);
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        return outputFormat.format(date);
    }



    private String formatCurrency(double amount) {
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        String formattedAmount = formatter.format(amount);
        return formattedAmount + " VNĐ";
    }


    private void updateOrderStatus(String orderId, String status) {
        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference("Orders").child(orderId);
        orderRef.child("orderStatus").setValue(status)
                .addOnSuccessListener(aVoid -> {
                    orderStatusTextView.setText("Status: " + status);
                    showToast("Order status updated to: " + status);
                })
                .addOnFailureListener(e -> showToast("Failed to update order status: " + e.getMessage()));
    }


    private void showUpdateStatusDialog(String orderId) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_update_status);

        Button buttonCancel = dialog.findViewById(R.id.buttonCancel);
        Button buttonWaiting = dialog.findViewById(R.id.buttonWaiting);
        Button buttonSuccess = dialog.findViewById(R.id.buttonSuccess);

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateOrderStatus(orderId, "Cancelled");
                dialog.dismiss();
            }
        });



        buttonWaiting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateOrderStatus(orderId, "Waiting");
                dialog.dismiss();
            }
        });

        buttonSuccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateOrderStatus(orderId, "Success");
                dialog.dismiss();
            }
        });

        dialog.show();
    }



    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
