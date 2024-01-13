package com.example.travel_tour_booking_app;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class PromotionAdapter extends RecyclerView.Adapter<PromotionAdapter.MyViewHolder> {
    Context context;
    List<Promotion> promotions;
    private int maxItemsToShow = -1; // Mặc định không giới hạn

    public PromotionAdapter (Context context, List<Promotion> promotions){
        this.context = context;
        this.promotions = promotions;
    }
    public PromotionAdapter (Context context, List<Promotion> promotions, int maxItemsToShow){
        this.context = context;
        this.promotions = promotions;
        this.maxItemsToShow = maxItemsToShow;
    }
    @Override
    public PromotionAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.item_promotion, parent, false);
        PromotionAdapter.MyViewHolder myViewHolder = new PromotionAdapter.MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PromotionAdapter.MyViewHolder holder, int position) {
        Glide.with(context).load(promotions.get(position).getThumbnail()).into(holder.ivThumbnail);

        holder.tvTitle.setText(promotions.get(position).getTitle());
        holder.tvDate.setText(promotions.get(position).getStartDateString() + " - " + promotions.get(position).getEndDateString());
        holder.tvText.setText(promotions.get(position).getText());

        holder.ll_item_promotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailPromotionActivity.class);
                intent.putExtra("Title", promotions.get(holder.getAdapterPosition()).getTitle());
                intent.putExtra("Date", promotions.get(holder.getAdapterPosition()).getStartDateString()+" - "+promotions.get(holder.getAdapterPosition()).getEndDateString());
                intent.putExtra("DetailPromotionList", (Promotion) promotions.get(holder.getAdapterPosition()));
                intent.putExtra("Key",promotions.get(holder.getAdapterPosition()).getKey());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (maxItemsToShow == -1) {
            // Trường hợp không giới hạn số lượng item
            return promotions.size();
        } else {
            // Trường hợp giới hạn số lượng item
            return Math.min(promotions.size(), maxItemsToShow);
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tvTitle;
        private TextView tvDate;
        private TextView tvText;
        private ImageView ivThumbnail;
        LinearLayout ll_item_promotion;
        public MyViewHolder(View itemView){
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title_promotion);
            tvDate = itemView.findViewById(R.id.tv_date_promotion);
            tvText = itemView.findViewById(R.id.tv_text_promotion);
            ivThumbnail = itemView.findViewById(R.id.iv_thumbnail_promotion);
            ll_item_promotion = itemView.findViewById(R.id.ll_item_promotion);
        }
    }

    public void searchPromotion (ArrayList<Promotion> promotions){
        this.promotions = promotions;
        notifyDataSetChanged();
    }
}