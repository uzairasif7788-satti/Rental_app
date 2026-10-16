package com.example.rentalvilla;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class OwnerDashboardActivity extends AppCompatActivity {

    RecyclerView rvUsers, rvBookings, rvFeedback;
    Button btnLogout;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_dashboard);

        rvUsers = findViewById(R.id.rvUsers);
        rvBookings = findViewById(R.id.rvBookings);
        rvFeedback = findViewById(R.id.rvFeedback);
        btnLogout = findViewById(R.id.btnOwnerLogout);

        db = new DatabaseHelper(this);

        // Setup RecyclerViews
        rvUsers.setLayoutManager(new LinearLayoutManager(this));
        rvBookings.setLayoutManager(new LinearLayoutManager(this));
        rvFeedback.setLayoutManager(new LinearLayoutManager(this));

        UserAdapter userAdapter = new UserAdapter(this, db.getAllUsers());
        BookingAdapter bookingAdapter = new BookingAdapter(this, db.getAllBookings());
        FeedbackAdapter feedbackAdapter = new FeedbackAdapter(this, db.getAllFeedback());

        rvUsers.setAdapter(userAdapter);
        rvBookings.setAdapter(bookingAdapter);
        rvFeedback.setAdapter(feedbackAdapter);

        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}
