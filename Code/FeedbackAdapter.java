package com.example.rentalvilla;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.FeedbackViewHolder> {

    Context context;
    Cursor cursor;

    public FeedbackAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    @Override
    public FeedbackViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_feedback, parent, false);
        return new FeedbackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FeedbackViewHolder holder, int position) {
        if (!cursor.moveToPosition(position)) return;

        String userName = cursor.getString(cursor.getColumnIndexOrThrow("UserName"));
        String message = cursor.getString(cursor.getColumnIndexOrThrow("Message"));

        holder.tvFeedbackUser.setText("User: " + userName);
        holder.tvFeedbackMessage.setText("Feedback: " + message);
    }

    @Override
    public int getItemCount() {
        return (cursor != null) ? cursor.getCount() : 0;
    }

    static class FeedbackViewHolder extends RecyclerView.ViewHolder {
        TextView tvFeedbackUser, tvFeedbackMessage;

        FeedbackViewHolder(View itemView) {
            super(itemView);
            tvFeedbackUser = itemView.findViewById(R.id.tvFeedbackUser);
            tvFeedbackMessage = itemView.findViewById(R.id.tvFeedbackMessage);
        }
    }
}
