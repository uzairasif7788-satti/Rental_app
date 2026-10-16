package com.example.rentalvilla;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class BookingActivity extends AppCompatActivity {

    TextView tvUser, tvCar, tvCarNumber, tvStart, tvEnd, tvTotal;
    Button btnConfirm;
    DatabaseHelper db;

    String email, carName, carNumber, startDate, endDate;
    int carId;
    double pricePerDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        // ✅ Find all views
        tvUser = findViewById(R.id.tvBookingUser);
        tvCar = findViewById(R.id.tvBookingCar);
        tvCarNumber = findViewById(R.id.tvBookingCarNumber);
        tvStart = findViewById(R.id.tvBookingStart);
        tvEnd = findViewById(R.id.tvBookingEnd);
        tvTotal = findViewById(R.id.tvBookingTotal);
        btnConfirm = findViewById(R.id.btnConfirmBooking);

        db = new DatabaseHelper(this);

        // ✅ Get all data from Intent
        email = getIntent().getStringExtra("email");
        carName = getIntent().getStringExtra("carName");
        carNumber = getIntent().getStringExtra("carNumber");
        startDate = getIntent().getStringExtra("startDate");
        endDate = getIntent().getStringExtra("endDate");
        carId = getIntent().getIntExtra("carId", -1);
        pricePerDay = getIntent().getDoubleExtra("pricePerDay", 0);

        // ✅ Calculate total amount
        long days = getDateDiff(startDate, endDate);
        double totalAmount = days * pricePerDay;

        // ✅ Display all info
        tvUser.setText("User: " + email);
        tvCar.setText("Car: " + carName);
        tvCarNumber.setText("Car Number: " + carNumber);
        tvStart.setText("Start Date: " + startDate);
        tvEnd.setText("End Date: " + endDate);
        tvTotal.setText("Total Amount: Rs. " + totalAmount);

        btnConfirm.setOnClickListener(v -> {
            boolean success = db.bookCar(email, carId, startDate, endDate);
            if (success) {
                Toast.makeText(this, "Booking Confirmed!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Booking Failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private long getDateDiff(String start, String end) {
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            java.util.Date s = sdf.parse(start);
            java.util.Date e = sdf.parse(end);
            return Math.abs(e.getTime() - s.getTime()) / (1000 * 60 * 60 * 24) + 1;
        } catch (Exception ex) {
            return 0;
        }
    }
}
