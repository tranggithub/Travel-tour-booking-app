package com.example.travel_tour_booking_app;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {
    Context context;
    List<Notification> notifications;

    public NotificationAdapter (Context context, List<Notification> notifications){
        this.context = context;
        this.notifications = notifications;
    }
    @Override
    public NotificationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false);
        NotificationAdapter.MyViewHolder myViewHolder = new NotificationAdapter.MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.MyViewHolder holder, int position) {
        Glide.with(context).load(notifications.get(position).getThumbnailURL()).into(holder.ivThumbnail);
        holder.tvTitle.setText(notifications.get(position).getTitle());
        holder.tvDate.setText(notifications.get(position).getDate());
        holder.tvText.setText(notifications.get(position).getText());

        //ử lý sự kin nhấp vào
        holder.ll_item_notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tvTitle;
        private TextView tvDate;
        private TextView tvText;
        private ImageView ivThumbnail;
        private LinearLayout ll_item_notifications;
        public MyViewHolder(View itemView){
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title_notifications);
            tvDate = itemView.findViewById(R.id.tv_date_notifications);
            tvText = itemView.findViewById(R.id.tv_text_notifications);
            ivThumbnail = itemView.findViewById(R.id.iv_notifications_thumbnail);
            ll_item_notifications = itemView.findViewById(R.id.ll_item_notifications);
        }
    }

    public void searchNotification(ArrayList<Notification> notifications){
        this.notifications = notifications;
        notifyDataSetChanged();
    }
}