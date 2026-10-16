package com.example.rentalvilla;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class OwnerLoginActivity extends AppCompatActivity {

    EditText etUsername, etPassword;
    Button btnLogin, btnRegister;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_login);

        etUsername = findViewById(R.id.etOwnerLoginUsername);
        etPassword = findViewById(R.id.etOwnerLoginPassword);
        btnLogin = findViewById(R.id.btnOwnerLogin);
        btnRegister = findViewById(R.id.btnOwnerGoToRegister);
        db = new DatabaseHelper(this);

        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            } else {
                boolean success = db.loginOwner(username, password);
                if (success) {
                    Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, OwnerDashboardActivity.class));
                    finish();
                } else {
                    Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, OwnerRegisterActivity.class));
        });
    }
}
