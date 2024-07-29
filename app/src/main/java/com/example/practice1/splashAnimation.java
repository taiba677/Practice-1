package com.example.practice1;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class splashAnimation extends AppCompatActivity {

    private static final int SPLASH_DISPLAY_LENGTH = 2000;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_animation);
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        new Handler(Looper.getMainLooper()).postDelayed(this::checkUserStatus, SPLASH_DISPLAY_LENGTH);
    }

    private void checkUserStatus() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            loadUserData(currentUser.getUid());
        } else {
            navigateToLogin();
        }
    }

    private void loadUserData(String userId) {
        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User.user = dataSnapshot.getValue(User.class);
                  navigateToDashboard();

                } else {
                    Toast.makeText(splashAnimation.this, "User data not found", Toast.LENGTH_SHORT).show();
                    navigateToLogin();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(splashAnimation.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                navigateToLogin();
            }
        });
    }

    private void navigateToDashboard() {
        Intent intent = new Intent(splashAnimation.this, home.class);
        startActivity(intent);
        finish();
    }


    private void navigateToLogin() {
        Intent intent = new Intent(splashAnimation.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
