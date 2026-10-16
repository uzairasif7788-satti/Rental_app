package com.example.rentalvilla;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class FeedbackActivity extends AppCompatActivity {

    EditText etFeedback;
    Button btnSubmit;
    DatabaseHelper db;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        etFeedback = findViewById(R.id.etFeedbackMessage);
        btnSubmit = findViewById(R.id.btnSubmitFeedback);
        db = new DatabaseHelper(this);

        email = getIntent().getStringExtra("email");

        btnSubmit.setOnClickListener(v -> {
            String message = etFeedback.getText().toString().trim();
            if (message.isEmpty()) {
                Toast.makeText(this, "Please enter feedback!", Toast.LENGTH_SHORT).show();
            } else {
                boolean success = db.addFeedback(email, message);
                if (success) {
                    Toast.makeText(this, "Thank you for your feedback!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "Failed to submit.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
