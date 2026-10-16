package com.example.rentalvilla;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UserBookingAdapter extends RecyclerView.Adapter<UserBookingAdapter.ViewHolder> {

    Context context;
    Cursor cursor;

    public UserBookingAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user_booking, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (!cursor.moveToPosition(position)) return;

        String carName = cursor.getString(cursor.getColumnIndexOrThrow("CarName"));
        String carNumber = cursor.getString(cursor.getColumnIndexOrThrow("CarNumber"));
        double pricePerDay = cursor.getDouble(cursor.getColumnIndexOrThrow("PricePerDay"));
        String startDate = cursor.getString(cursor.getColumnIndexOrThrow("StartDate"));
        String endDate = cursor.getString(cursor.getColumnIndexOrThrow("EndDate"));
        String paymentStatus = cursor.getString(cursor.getColumnIndexOrThrow("PaymentStatus"));

        long days = getDateDiff(startDate, endDate);
        double total = days * pricePerDay;

        holder.tvCarName.setText("Car: " + carName);
        holder.tvCarNumber.setText("Number: " + carNumber);
        holder.tvStartDate.setText("Start: " + startDate);
        holder.tvEndDate.setText("End: " + endDate);
        holder.tvPaymentStatus.setText("Payment: " + paymentStatus);
        holder.tvTotal.setText("Total: Rs. " + total);
    }

    @Override
    public int getItemCount() {
        return (cursor != null) ? cursor.getCount() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCarName, tvCarNumber, tvStartDate, tvEndDate, tvPaymentStatus, tvTotal;

        ViewHolder(View itemView) {
            super(itemView);
            tvCarName = itemView.findViewById(R.id.tvUserCarName);
            tvCarNumber = itemView.findViewById(R.id.tvUserCarNumber);
            tvStartDate = itemView.findViewById(R.id.tvUserStartDate);
            tvEndDate = itemView.findViewById(R.id.tvUserEndDate);
            tvPaymentStatus = itemView.findViewById(R.id.tvUserPaymentStatus);
            tvTotal = itemView.findViewById(R.id.tvUserTotal);
        }
    }

    private long getDateDiff(String start, String end) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date s = sdf.parse(start);
            Date e = sdf.parse(end);
            return Math.abs(e.getTime() - s.getTime()) / (1000 * 60 * 60 * 24) + 1;
        } catch (Exception ex) {
            return 0;
        }
    }
}
