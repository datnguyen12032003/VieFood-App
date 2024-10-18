package com.example.food_order_app.Activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_order_app.Adapters.OrderAdapter;
import com.example.food_order_app.Models.Cart;
import com.example.food_order_app.Models.Order;
import com.example.food_order_app.Models.User;
import com.example.food_order_app.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class OrderActivity extends AppCompatActivity {

    private RecyclerView recyclerViewOrderProducts;
    private OrderAdapter orderAdapter;
    private EditText orderAddress, orderPhone, orderNoteInput;
    private TextView orderTotal;
    private Button buttonConfirm, buttonCancel;
    private Order order;
    private User currentUser;
    private DatabaseReference dbOrders;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        initViews();
        retrieveUserData();
        setupRecyclerView();

        buttonConfirm.setOnClickListener(v -> showConfirmationDialog((List<Cart>) getIntent().getSerializableExtra("cartItems")));
        buttonCancel.setOnClickListener(v -> finish());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data != null) {
                String selectedAddress = data.getStringExtra("selectedAddress");
                orderAddress.setText(selectedAddress);
            }
        }
    }

    private void initViews() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        userId = sharedPreferences.getString("current_user_id", null);
        dbOrders = FirebaseDatabase.getInstance().getReference("Orders");
        ImageView icLocationMap = findViewById(R.id.ic_location_map);
        icLocationMap.setOnClickListener(v -> {
            Intent mapIntent = new Intent(OrderActivity.this, MapsActivity.class);
            startActivityForResult(mapIntent, 1);
        });

        recyclerViewOrderProducts = findViewById(R.id.recyclerView_order_products);
        orderAddress = findViewById(R.id.order_address);
        orderPhone = findViewById(R.id.order_phone);
        orderNoteInput = findViewById(R.id.order_note_input);
        orderTotal = findViewById(R.id.order_total);
        buttonConfirm = findViewById(R.id.button_confirm);
        buttonCancel = findViewById(R.id.button_cancel);

        currentUser = (User) getIntent().getSerializableExtra("currentUser");
    }

    private void retrieveUserData() {
        if (currentUser != null) {
            orderPhone.setText(currentUser.getUserPhone());
            orderAddress.setText(currentUser.getUserAddress());
        }

        List<Cart> cartItems = (List<Cart>) getIntent().getSerializableExtra("cartItems");
        double totalAmount = getIntent().getDoubleExtra("totalAmount", 0.0);
        createOrder(cartItems, totalAmount);
        populateOrderDetails();
    }

    private void showConfirmationDialog(List<Cart> cartItems) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure you want to place the order?");
        builder.setPositiveButton("OK", (dialog, which) -> {
            placeOrder(cartItems);
            clearCart();
            finish();
        });
        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        builder.show();
    }


    private void placeOrder(List<Cart> cartItems) {
        String orderId = dbOrders.push().getKey();
        if (orderId != null) {
            order.setOrderId(orderId);
            order.setOrderPhone(orderPhone.getText().toString());
            order.setOrderAddress(orderAddress.getText().toString());
            order.setOrderNote(orderNoteInput.getText().toString());

            Map<String, Object> orderDetails = new HashMap<>();
            orderDetails.put("orderId", orderId);
            orderDetails.put("orderPhone", orderPhone.getText().toString());
            orderDetails.put("orderAddress", orderAddress.getText().toString());
            orderDetails.put("orderNote", orderNoteInput.getText().toString());
            orderDetails.put("orderTotal", order.getOrderTotal());
            orderDetails.put("orderedItems", cartItems);
            orderDetails.put("userId", userId);
            orderDetails.put("orderDate", String.valueOf(System.currentTimeMillis()));
            orderDetails.put("orderStatus", "waiting");

            dbOrders.child(orderId).setValue(orderDetails).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Order placed successfully!", Toast.LENGTH_SHORT).show();
                    sendNotification("Order Confirmation", "Your order has been placed successfully!");
                    Intent intent = new Intent(OrderActivity.this, NavigationActivity.class);
                    intent.putExtra("openCartFragment", true);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Failed to place order. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(e -> {
                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        } else {
            Toast.makeText(this, "Failed to generate order ID.", Toast.LENGTH_SHORT).show();
        }
    }


    private void clearCart() {
        DatabaseReference dbCartItems = FirebaseDatabase.getInstance().getReference("Cart").child(userId);
        dbCartItems.removeValue();
    }

    private void createOrder(List<Cart> cartItems, double totalAmount) {
        order = new Order();
        order.setOrderAddress(orderAddress.getText().toString());
        order.setOrderPhone(orderPhone.getText().toString());
        order.setOrderNote(orderNoteInput.getText().toString());
        order.setOrderTotal(totalAmount);
        order.setOrderedItems(cartItems);
        if (currentUser != null) {
            order.setUserId(userId);
        } else {
            Toast.makeText(this, "User is not logged in.", Toast.LENGTH_SHORT).show();
        }
        order.setOrderDate(String.valueOf(System.currentTimeMillis()));
        order.setOrderStatus("waiting");
    }

    private void populateOrderDetails() {
        orderTotal.setText("Total: " + formatNumber(order.getOrderTotal()));
    }

    private String formatNumber(double amount) {
        return NumberFormat.getInstance(Locale.getDefault()).format(amount);
    }

    private void sendNotification(String title, String message) {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String channelId = "order_channel";
            CharSequence name = "Order Notifications";
            String description = "Channel for order notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "order_channel")
                .setSmallIcon(R.drawable.food_icon)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }


    private void setupRecyclerView() {
        recyclerViewOrderProducts.setLayoutManager(new LinearLayoutManager(this));
        orderAdapter = new OrderAdapter(order.getOrderedItems());
        recyclerViewOrderProducts.setAdapter(orderAdapter);
    }
}
