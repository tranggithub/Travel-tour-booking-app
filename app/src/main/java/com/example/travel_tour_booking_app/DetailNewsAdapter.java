package com.example.travel_tour_booking_app;

import android.content.Context;
import android.content.Intent;
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

public class DetailNewsAdapter extends RecyclerView.Adapter<DetailNewsAdapter.MyViewHolder> {
    Context context;
    List<DetailNews> detailNews;
    Float textSize = null;

    public DetailNewsAdapter (Context context, List<DetailNews> detailNews, float textSize){
        this.context = context;
        this.detailNews = detailNews;
        this.textSize = textSize;
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
            //Thiêt lập Visible
            holder.ivPlace.setVisibility(View.VISIBLE);
            holder.tvSubtitlePlace.setVisibility(View.VISIBLE);

            holder.tvDetail.setVisibility(View.GONE);

            //Thiết lập nội dung
            //holder.ivPlace.setImageResource(tempDetailNews.getImage());
            //holder.ivPlace.setImageResource(0);
            Glide.with(context).load(detailNews.get(position).getPicture()).into(holder.ivPlace);
            holder.tvSubtitlePlace.setText(tempDetailNews.getSubtitleImage());
        } else{
            //Thiết lập Visible
            holder.ivPlace.setVisibility(View.GONE);
            holder.tvSubtitlePlace.setVisibility(View.GONE);

            holder.tvDetail.setVisibility(View.VISIBLE);

            //Thiết lập nội dung
            holder.tvDetail.setText(tempDetailNews.getDetailText());

            if(textSize != null){
                holder.tvDetail.setTextSize(textSize);
            }
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

    public void setTextSize(float size){
        textSize = convertDpToPixels(size);
    }
    private float convertDpToPixels(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
}
