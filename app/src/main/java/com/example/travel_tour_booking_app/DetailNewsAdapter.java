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

public class DetailNewsAdapter extends RecyclerView.Adapter<DetailNewsAdapter.MyViewHolder> {
    Context context;
    List<DetailNews> detailNews;

    public DetailNewsAdapter (Context context, List<DetailNews> detailNews){
        this.context = context;
        this.detailNews = detailNews;
    }
    @Override
    public DetailNewsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.item_detailnews, parent, false);
        DetailNewsAdapter.MyViewHolder myViewHolder = new DetailNewsAdapter.MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DetailNewsAdapter.MyViewHolder holder, int position) {
        DetailNews tempDetailNews = detailNews.get(position);
        if(tempDetailNews.isImage())
        {
            //Thiê lập Visible
            holder.ivPlace.setVisibility(View.VISIBLE);
            holder.tvSubtitlePlace.setVisibility(View.VISIBLE);

            holder.tvDetail.setVisibility(View.GONE);

            //Thiết lập nội dung
            holder.ivPlace.setImageResource(tempDetailNews.getImage());
            holder.tvSubtitlePlace.setText(tempDetailNews.getSubtitleImage());
        } else{
            //Thiê lập Visible
            holder.ivPlace.setVisibility(View.GONE);
            holder.tvSubtitlePlace.setVisibility(View.GONE);

            holder.tvDetail.setVisibility(View.VISIBLE);

            //Thiết lập nội dung
            holder.tvDetail.setText(tempDetailNews.getDetailText());
        }
    }

    @Override
    public int getItemCount() {
        return detailNews.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tvDetail;
        private ImageView ivPlace;
        private TextView tvSubtitlePlace;
        public MyViewHolder(View itemView){
            super(itemView);
            tvDetail = itemView.findViewById(R.id.tv_details);
            ivPlace = itemView.findViewById(R.id.iv_detail_place);
            tvSubtitlePlace = itemView.findViewById(R.id.tv_subtitle_detail_place);
        }
    }
}
