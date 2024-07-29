
package com.example.practice1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {
    private EditText nameEditText, emailEditText, passwordEditText, confirmPasswordEditText, phoneEditText;
    private Button signUpButton, loginButton;
    private ProgressDialog progressDialog;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;


  protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_reg);
      nameEditText = findViewById(R.id.name);
      emailEditText = findViewById(R.id.email);
      passwordEditText = findViewById(R.id.password);
      confirmPasswordEditText = findViewById(R.id.confirm_password);
      phoneEditText = findViewById(R.id.phone);
      signUpButton = findViewById(R.id.sign_up_button);
      loginButton = findViewById(R.id.login_button);
      progressDialog = new ProgressDialog(this);

      mAuth = FirebaseAuth.getInstance();
      databaseReference = FirebaseDatabase.getInstance().getReference("Users");

      signUpButton.setOnClickListener(v -> signUpUser());

      loginButton.setOnClickListener(v -> {
          Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
          startActivity(intent);
          finish();
      });
  }

    private void signUpUser() {
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();
        String phoneNumber = phoneEditText.getText().toString().trim();

        if (TextUtils.isEmpty(phoneNumber)) {
            Toast.makeText(this, "Phone number is required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Signing Up...");
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    saveUserToDatabase(user.getUid(), name, email, phoneNumber);
                }
            } else {
                progressDialog.dismiss();
                Toast.makeText(RegistrationActivity.this, "Sign Up Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUserToDatabase(String userId, String name, String email, String phoneNumber) {
        User user = new User(name, email, phoneNumber);
        databaseReference.child(userId).setValue(user).addOnCompleteListener(task -> {
            progressDialog.dismiss();
            if (task.isSuccessful()) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
                Toast.makeText(RegistrationActivity.this, "Sign Up Successful", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(RegistrationActivity.this, "Failed to Save User Info: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}

