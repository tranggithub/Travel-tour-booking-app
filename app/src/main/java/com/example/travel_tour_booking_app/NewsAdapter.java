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

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {
    Context context;
    List<News> newss;

    public NewsAdapter (Context context, List<News> newss){
        this.context = context;
        this.newss = newss;
    }
    @Override
    public NewsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.item_news, parent, false);
        NewsAdapter.MyViewHolder myViewHolder = new NewsAdapter.MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.MyViewHolder holder, int position) {
        Glide.with(context).load(newss.get(position).getThumbnail()).into(holder.ivThumbnail);
        holder.tvTitle.setText(newss.get(position).getTitile());
        holder.tvDate.setText(newss.get(position).getUploadDate());
        holder.tvText.setText(newss.get(position).getText());

        holder.ll_item_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailNewsActivity.class);
                intent.putExtra("Title", newss.get(holder.getAdapterPosition()).getTitile());
                intent.putExtra("Date", newss.get(holder.getAdapterPosition()).getUploadDate());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return newss.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tvTitle;
        private TextView tvDate;
        private TextView tvText;
        private ImageView ivThumbnail;
        private LinearLayout ll_item_news;
        public MyViewHolder(View itemView){
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title_news);
            tvDate = itemView.findViewById(R.id.tv_date_news);
            tvText = itemView.findViewById(R.id.tv_text_news);
            ivThumbnail = itemView.findViewById(R.id.iv_news_thumbnail);
            ll_item_news = itemView.findViewById(R.id.ll_item_news);
        }
    }
}