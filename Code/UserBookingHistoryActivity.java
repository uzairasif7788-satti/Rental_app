package com.example.rentalvilla;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class UserBookingHistoryActivity extends AppCompatActivity {

    RecyclerView rvUserBookings;
    DatabaseHelper db;
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_booking_history);

        rvUserBookings = findViewById(R.id.rvUserBookings);
        rvUserBookings.setLayoutManager(new LinearLayoutManager(this));

        db = new DatabaseHelper(this);

        userEmail = getIntent().getStringExtra("email");

        Cursor cursor = db.getUserBookings(userEmail);

        if (cursor == null || cursor.getCount() == 0) {
            Toast.makeText(this, "No bookings found!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            UserBookingAdapter adapter = new UserBookingAdapter(this, cursor);
            rvUserBookings.setAdapter(adapter);
        }
    }
}
