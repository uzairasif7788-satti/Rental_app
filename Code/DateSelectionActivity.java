package com.example.rentalvilla;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class DateSelectionActivity extends AppCompatActivity {

    EditText etStartDate, etEndDate;
    Button btnNext;
    String userEmail;

    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_selection);

        etStartDate = findViewById(R.id.etStartDate);
        etEndDate = findViewById(R.id.etEndDate);
        btnNext = findViewById(R.id.btnNextCarSelection);

        userEmail = getIntent().getStringExtra("email");

        calendar = Calendar.getInstance();

        etStartDate.setOnClickListener(v -> showDatePicker(etStartDate));
        etEndDate.setOnClickListener(v -> showDatePicker(etEndDate));

        btnNext.setOnClickListener(v -> {
            String start = etStartDate.getText().toString();
            String end = etEndDate.getText().toString();

            // ✅ Pass dates directly by Intent
            Intent intent = new Intent(this, CarSelectionActivity.class);
            intent.putExtra("email", userEmail);
            intent.putExtra("startDate", start);
            intent.putExtra("endDate", end);
            startActivity(intent);
            finish();
        });
    }

    private void showDatePicker(EditText target) {
        int y = calendar.get(Calendar.YEAR);
        int m = calendar.get(Calendar.MONTH);
        int d = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(this, (view, year, month, day) -> {
            String mm = String.format("%02d", month + 1);
            String dd = String.format("%02d", day);
            String date = year + "-" + mm + "-" + dd;
            target.setText(date);
        }, y, m, d);
        dialog.show();
    }


}
