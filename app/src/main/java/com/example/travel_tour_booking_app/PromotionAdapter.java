package com.example.travel_tour_booking_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PromotionAdapter extends RecyclerView.Adapter<PromotionAdapter.MyViewHolder> {
    Context context;
    List<Promotion> promotions;

    public PromotionAdapter (Context context, List<Promotion> promotions){
        this.context = context;
        this.promotions = promotions;
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
        holder.tvTitle.setText(promotions.get(position).getTitle());
        holder.tvDate.setText(promotions.get(position).getCurrentDate(promotions.get(position).begin) + " - " + promotions.get(position).getCurrentDate(promotions.get(position).end));
        holder.tvText.setText(promotions.get(position).getText());

    }

    @Override
    public int getItemCount() {
        return promotions.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tvTitle;
        private TextView tvDate;
        private TextView tvText;
        public MyViewHolder(View itemView){
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title_promotion);
            tvDate = itemView.findViewById(R.id.tv_date_promotion);
            tvText = itemView.findViewById(R.id.tv_text_promotion);

        }
    }
}