package com.example.rentalvilla;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    Context context;
    Cursor cursor;
    DatabaseHelper db;

    public BookingAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
        this.db = new DatabaseHelper(context);
    }

    @Override
    public BookingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BookingViewHolder holder, int position) {
        if (!cursor.moveToPosition(position)) return;

        int bookingId = cursor.getInt(cursor.getColumnIndexOrThrow("ID"));
        String userName = cursor.getString(cursor.getColumnIndexOrThrow("UserName"));
        String carName = cursor.getString(cursor.getColumnIndexOrThrow("CarName"));
        String carNumber = cursor.getString(cursor.getColumnIndexOrThrow("CarNumber"));
        double pricePerDay = cursor.getDouble(cursor.getColumnIndexOrThrow("PricePerDay"));
        String startDate = cursor.getString(cursor.getColumnIndexOrThrow("StartDate"));
        String endDate = cursor.getString(cursor.getColumnIndexOrThrow("EndDate"));
        String paymentStatus = cursor.getString(cursor.getColumnIndexOrThrow("PaymentStatus"));

        long diff = Math.abs(getDateDiff(startDate, endDate));
        double total = diff * pricePerDay;

        holder.tvUserName.setText("User: " + userName);
        holder.tvCarName.setText("Car: " + carName);
        holder.tvCarNumber.setText("Car Number: " + carNumber);
        holder.tvStartDate.setText("Start: " + startDate);
        holder.tvEndDate.setText("End: " + endDate);
        holder.tvPaymentStatus.setText("Payment: " + paymentStatus);
        holder.tvTotalPrice.setText("Total: " + total);

        holder.btnClearPayment.setOnClickListener(v -> {
            if (db.updatePaymentStatus(bookingId, "Paid")) {
                Toast.makeText(context, "Payment cleared!", Toast.LENGTH_SHORT).show();
                cursor = db.getAllBookings();
                notifyDataSetChanged();
            }
        });

        holder.btnClearPayment.setEnabled(!paymentStatus.equalsIgnoreCase("Paid"));
    }

    private long getDateDiff(String start, String end) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date s = sdf.parse(start);
            Date e = sdf.parse(end);
            long diff = Math.abs(e.getTime() - s.getTime()) / (1000 * 60 * 60 * 24) + 1;
            return diff;
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public int getItemCount() {
        return (cursor != null) ? cursor.getCount() : 0;
    }

    static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName, tvCarName, tvCarNumber, tvStartDate, tvEndDate, tvPaymentStatus, tvTotalPrice;
        Button btnClearPayment;

        BookingViewHolder(View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvBookingUser);
            tvCarName = itemView.findViewById(R.id.tvBookingCar);
            tvCarNumber = itemView.findViewById(R.id.tvBookingCarNumber);
            tvStartDate = itemView.findViewById(R.id.tvBookingStart);
            tvEndDate = itemView.findViewById(R.id.tvBookingEnd);
            tvPaymentStatus = itemView.findViewById(R.id.tvPaymentStatus);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            btnClearPayment = itemView.findViewById(R.id.btnClearPayment);
        }
    }
}