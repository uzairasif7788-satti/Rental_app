package com.example.rentalvilla;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class OwnerRegisterActivity extends AppCompatActivity {

    EditText etUsername, etPassword;
    Button btnRegister;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_register);

        etUsername = findViewById(R.id.etOwnerUsername);
        etPassword = findViewById(R.id.etOwnerPassword);
        btnRegister = findViewById(R.id.btnOwnerRegister);
        db = new DatabaseHelper(this);

        btnRegister.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            } else {
                if (db.isOwnerExists()) {
                    Toast.makeText(this, "Owner already registered!", Toast.LENGTH_SHORT).show();
                } else {
                    boolean success = db.registerOwner(username, password);
                    if (success) {
                        Toast.makeText(this, "Owner registered successfully!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, OwnerLoginActivity.class));
                        finish();
                    } else {
                        Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
