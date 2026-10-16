package com.example.rentalvilla;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CarSelectionActivity extends AppCompatActivity implements CarAdapter.OnCarClickListener {

    RecyclerView rvCars;
    DatabaseHelper db;
    String email, startDate, endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_selection);

        rvCars = findViewById(R.id.rvCars);
        rvCars.setLayoutManager(new LinearLayoutManager(this));
        db = new DatabaseHelper(this);

        email = getIntent().getStringExtra("email");
        startDate = getIntent().getStringExtra("startDate");
        endDate = getIntent().getStringExtra("endDate");

        if (startDate == null || endDate == null) {
            Toast.makeText(this, "Please select rental dates first.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Cursor cursor = db.getAvailableCars(startDate, endDate);

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No cars available for selected dates.", Toast.LENGTH_LONG).show();
            finish();
        } else {
            CarAdapter carAdapter = new CarAdapter(this, cursor, this);
            rvCars.setAdapter(carAdapter);
        }
    }

    @Override
    public void onCarClick(int carId, String carName, String carNumber, double pricePerDay) {
        Intent intent = new Intent(this, BookingActivity.class);
        intent.putExtra("email", email);
        intent.putExtra("carId", carId);
        intent.putExtra("carName", carName);
        intent.putExtra("carNumber", carNumber);
        intent.putExtra("pricePerDay", pricePerDay); // ✅ NEW
        intent.putExtra("startDate", startDate);
        intent.putExtra("endDate", endDate);
        startActivity(intent);
    }


}