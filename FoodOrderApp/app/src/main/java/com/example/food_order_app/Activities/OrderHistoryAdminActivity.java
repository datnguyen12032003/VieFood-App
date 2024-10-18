package com.example.food_order_app.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.food_order_app.Adapters.OrderHistoryAdminAdapter;
import com.example.food_order_app.Models.Order;
import com.example.food_order_app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class OrderHistoryAdminActivity extends AppCompatActivity {

    private RecyclerView recyclerViewOrders;
    private OrderHistoryAdminAdapter orderHistoryAdminAdapter;
    private List<Order> orderList;
    private DatabaseReference dbOrders;
    private ImageView icBack;
    private Spinner statusSpinner;
    private String selectedStatus = "All";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history_admin);

        recyclerViewOrders = findViewById(R.id.recyclerView_orders);
        orderList = new ArrayList<>();
        dbOrders = FirebaseDatabase.getInstance().getReference("Orders");
        icBack = findViewById(R.id.ic_back);
        statusSpinner = findViewById(R.id.status_spinner);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.order_status_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(adapter);


        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedStatus = parentView.getItemAtPosition(position).toString();
                loadFilteredOrders(selectedStatus);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        setupRecyclerView();
        loadAllOrders();
    }

    private void setupRecyclerView() {
        recyclerViewOrders.setLayoutManager(new LinearLayoutManager(this));
        orderHistoryAdminAdapter = new OrderHistoryAdminAdapter(orderList);
        recyclerViewOrders.setAdapter(orderHistoryAdminAdapter);
    }

    private void loadAllOrders() {
        dbOrders.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                orderList.clear();
                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                    Order order = orderSnapshot.getValue(Order.class);
                    orderList.add(order);
                }
                orderHistoryAdminAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    private void loadFilteredOrders(String status) {
        dbOrders.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                orderList.clear();
                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                    Order order = orderSnapshot.getValue(Order.class);
                    if (status.equals("All") || order.getOrderStatus().equals(status)) {
                        orderList.add(order);
                    }
                }
                orderHistoryAdminAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAllOrders();
    }
}
