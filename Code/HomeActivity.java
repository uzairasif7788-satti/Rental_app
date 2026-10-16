package com.example.rentalvilla;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    Button userButton, ownerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        userButton = findViewById(R.id.btnUser);
        ownerButton = findViewById(R.id.btnOwner);

        userButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, UserLoginActivity.class);
            startActivity(intent);
        });

        ownerButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, OwnerLoginActivity.class);
            startActivity(intent);
        });
    }
}
