package com.example.rentalvilla;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class UserDashboardActivity extends AppCompatActivity {

    Button btnBooking, btnBookingHistory, btnFeedback, btnLogout;
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        btnBooking = findViewById(R.id.btnBooking);
        btnBookingHistory = findViewById(R.id.btnBookingHistory); // NEW
        btnFeedback = findViewById(R.id.btnFeedback);
        btnLogout = findViewById(R.id.btnUserLogout);

        userEmail = getIntent().getStringExtra("email");

        // Book a Car
        btnBooking.setOnClickListener(v -> {
            Intent intent = new Intent(this, DateSelectionActivity.class);
            intent.putExtra("email", userEmail);
            startActivity(intent);
        });

        // View Booking History
        btnBookingHistory.setOnClickListener(v -> {
            Intent intent = new Intent(this, UserBookingHistoryActivity.class);
            intent.putExtra("email", userEmail);
            startActivity(intent);
        });

        // Feedback
        btnFeedback.setOnClickListener(v -> {
            Intent intent = new Intent(this, FeedbackActivity.class);
            intent.putExtra("email", userEmail);
            startActivity(intent);
        });

        // Logout
        btnLogout.setOnClickListener(v -> {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        });
    }
}
