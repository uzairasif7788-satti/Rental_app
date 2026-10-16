package com.example.rentalvilla;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    Context context;
    Cursor cursor;

    public UserAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        if (!cursor.moveToPosition(position)) return;

        String name = cursor.getString(cursor.getColumnIndexOrThrow("Name"));
        String email = cursor.getString(cursor.getColumnIndexOrThrow("Email"));
        String phone = cursor.getString(cursor.getColumnIndexOrThrow("Phone"));

        holder.tvName.setText("Name: " + name);
        holder.tvEmail.setText("Email: " + email);
        holder.tvPhone.setText("Number: " + phone);
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvEmail, tvPhone;

        UserViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvUserName);
            tvEmail = itemView.findViewById(R.id.tvUserEmail);
            tvPhone = itemView.findViewById(R.id.tvUserPhone);
        }
    }
}
