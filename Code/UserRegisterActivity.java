package com.example.rentalvilla;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UserRegisterActivity extends AppCompatActivity {

    EditText etName, etEmail, etPassword, etPhone;
    Button btnRegister;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        etName = findViewById(R.id.etUserName);
        etEmail = findViewById(R.id.etUserEmail);
        etPassword = findViewById(R.id.etUserPassword);
        etPhone = findViewById(R.id.etUserPhone);
        btnRegister = findViewById(R.id.btnUserRegister);
        db = new DatabaseHelper(this);

        btnRegister.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            } else {
                boolean success = db.registerUser(name, email, password, phone);
                if (success) {
                    Toast.makeText(this, "Registered successfully!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, UserLoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
