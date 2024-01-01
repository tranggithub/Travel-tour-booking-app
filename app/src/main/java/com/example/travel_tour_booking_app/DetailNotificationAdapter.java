package com.example.travel_tour_booking_app;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class DetailNotificationAdapter extends RecyclerView.Adapter<DetailNotificationAdapter.MyViewHolder> {
    Context context;
    List<DetailNews> detailNotification;
    Float textSize = null;

    public DetailNotificationAdapter (Context context, List<DetailNews> detailNotification, float textSize){
        this.context = context;
        this.detailNotification = detailNotification;
        this.textSize = textSize;
    }
    @Override
    public DetailNotificationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.item_detailnotification, parent, false);
        DetailNotificationAdapter.MyViewHolder myViewHolder = new DetailNotificationAdapter.MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DetailNotificationAdapter.MyViewHolder holder, int position) {
        DetailNews tempDetailNotification = detailNotification.get(position);
        if(tempDetailNotification.isImage())
        {
            //Thiêt lập Visible
            holder.ivPlace.setVisibility(View.VISIBLE);
            holder.tvSubtitlePlace.setVisibility(View.VISIBLE);

            holder.tvDetail.setVisibility(View.GONE);

            //Thiết lập nội dung

            Glide.with(context).load(detailNotification.get(position).getPicture()).into(holder.ivPlace);
            holder.tvSubtitlePlace.setText(tempDetailNotification.getSubtitleImage());
        } else{
            //Thiết lập Visible
            holder.ivPlace.setVisibility(View.GONE);
            holder.tvSubtitlePlace.setVisibility(View.GONE);

            holder.tvDetail.setVisibility(View.VISIBLE);

            //Thiết lập nội dung
            holder.tvDetail.setText(tempDetailNotification.getDetailText());

            if(textSize != null){
                holder.tvDetail.setTextSize(textSize);
            }
        }
    }

    @Override
    public int getItemCount() {
        return detailNotification.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tvDetail;
        private ImageView ivPlace;
        private TextView tvSubtitlePlace;
        public MyViewHolder(View itemView){
            super(itemView);
            tvDetail = itemView.findViewById(R.id.tv_details);
            ivPlace = itemView.findViewById(R.id.iv_detail_notification);
            tvSubtitlePlace = itemView.findViewById(R.id.tv_subtitle_detail_notification);
        }
    }

    public void setTextSize(float size){
        textSize = convertDpToPixels(size);
    }
    private float convertDpToPixels(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
}
