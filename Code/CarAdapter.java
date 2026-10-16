package com.example.rentalvilla;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder> {

    Context context;
    Cursor cursor;
    OnCarClickListener listener;

    // Updated: also pass carNumber + pricePerDay
    public interface OnCarClickListener {
        void onCarClick(int carId, String carName, String carNumber, double pricePerDay);
    }

    public CarAdapter(Context context, Cursor cursor, OnCarClickListener listener) {
        this.context = context;
        this.cursor = cursor;
        this.listener = listener;
    }

    @Override
    public CarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_car, parent, false);
        return new CarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CarViewHolder holder, int position) {
        if (!cursor.moveToPosition(position)) return;

        int id = cursor.getInt(cursor.getColumnIndexOrThrow("ID"));
        String name = cursor.getString(cursor.getColumnIndexOrThrow("CarName"));
        String number = cursor.getString(cursor.getColumnIndexOrThrow("CarNumber"));
        double price = cursor.getDouble(cursor.getColumnIndexOrThrow("PricePerDay"));

        holder.tvCarName.setText("Name: " + name);
        holder.tvCarNumber.setText("Number: " + number);
        holder.tvCarPrice.setText("Price/Day: Rs. " + price);

        holder.itemView.setOnClickListener(v -> listener.onCarClick(id, name, number, price));
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    static class CarViewHolder extends RecyclerView.ViewHolder {
        TextView tvCarName, tvCarNumber, tvCarPrice;

        CarViewHolder(View itemView) {
            super(itemView);
            tvCarName = itemView.findViewById(R.id.tvCarName);
            tvCarNumber = itemView.findViewById(R.id.tvCarNumber);
            tvCarPrice = itemView.findViewById(R.id.tvCarPrice);
        }
    }
}
