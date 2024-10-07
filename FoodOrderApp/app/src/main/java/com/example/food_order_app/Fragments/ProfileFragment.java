package com.example.food_order_app.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.food_order_app.Activities.ProfileActivity;
import com.example.food_order_app.Models.User;
import com.example.food_order_app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {

    private TextView userNameTextView;
    private TextView userPhoneTextView;
    private DatabaseReference dbUsers;

    public ProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbUsers = FirebaseDatabase.getInstance().getReference("Users");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        userNameTextView = view.findViewById(R.id.userName);
        userPhoneTextView = view.findViewById(R.id.userPhone);
        Button btnEditProfile = view.findViewById(R.id.btnEditProfile);
        fetchUserData();
        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ProfileActivity.class);
            startActivity(intent);
        });
        return view;
    }

    private void fetchUserData() {

        String userId = getActivity().getSharedPreferences("user_prefs", getActivity().MODE_PRIVATE)
                .getString("current_user_id", null);
        if (userId != null) {

            dbUsers.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        User currentUser = dataSnapshot.getValue(User.class);
                        if (currentUser != null) {
                            userNameTextView.setText(currentUser.getUserName());
                            userPhoneTextView.setText(currentUser.getUserPhone());
                        } else {
                            showToast("User not found");
                        }
                    } else {
                        showToast("User not found");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    showToast("Failed to fetch user data");
                }
            });
        } else {
            showToast("No user found");
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        fetchUserData();
    }

    private void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}
