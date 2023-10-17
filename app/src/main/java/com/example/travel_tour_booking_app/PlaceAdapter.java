package com.example.travel_tour_booking_app;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.MyViewHolder> {
    Context context;
    List<Place> places;

    public PlaceAdapter (Context context, List<Place> places){
        this.context = context;
        this.places = places;
    }
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.item_place, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvTitle.setText(places.get(position).getTitle());
        holder.tvLocation.setText(places.get(position).getLocation() + " (" + places.get(position).getDuration()+" ngày)");
        holder.tvPrice.setText(places.get(position).getPrice());
        if (places.get(position).getStar() < 1){
            holder.ivStar1.setImageResource(R.drawable.ic_star_not_vote);
            holder.ivStar2.setImageResource(R.drawable.ic_star_not_vote);
            holder.ivStar3.setImageResource(R.drawable.ic_star_not_vote);
            holder.ivStar4.setImageResource(R.drawable.ic_star_not_vote);
            holder.ivStar5.setImageResource(R.drawable.ic_star_not_vote);
        } else if (places.get(position).getStar() < 2)
        {
            holder.ivStar1.setImageResource(R.drawable.ic_star_vote);
            holder.ivStar2.setImageResource(R.drawable.ic_star_not_vote);
            holder.ivStar3.setImageResource(R.drawable.ic_star_not_vote);
            holder.ivStar4.setImageResource(R.drawable.ic_star_not_vote);
            holder.ivStar5.setImageResource(R.drawable.ic_star_not_vote);
        } else if (places.get(position).getStar() < 3)
        {
            holder.ivStar1.setImageResource(R.drawable.ic_star_vote);
            holder.ivStar2.setImageResource(R.drawable.ic_star_vote);
            holder.ivStar3.setImageResource(R.drawable.ic_star_not_vote);
            holder.ivStar4.setImageResource(R.drawable.ic_star_not_vote);
            holder.ivStar5.setImageResource(R.drawable.ic_star_not_vote);
        }
        else if (places.get(position).getStar() < 4)
        {
            holder.ivStar1.setImageResource(R.drawable.ic_star_vote);
            holder.ivStar2.setImageResource(R.drawable.ic_star_vote);
            holder.ivStar3.setImageResource(R.drawable.ic_star_vote);
            holder.ivStar4.setImageResource(R.drawable.ic_star_not_vote);
            holder.ivStar5.setImageResource(R.drawable.ic_star_not_vote);
        }
        else if (places.get(position).getStar() < 5)
        {
            holder.ivStar1.setImageResource(R.drawable.ic_star_vote);
            holder.ivStar2.setImageResource(R.drawable.ic_star_vote);
            holder.ivStar3.setImageResource(R.drawable.ic_star_vote);
            holder.ivStar4.setImageResource(R.drawable.ic_star_vote);
            holder.ivStar5.setImageResource(R.drawable.ic_star_not_vote);
        }
        else
        {
            holder.ivStar1.setImageResource(R.drawable.ic_star_vote);
            holder.ivStar2.setImageResource(R.drawable.ic_star_vote);
            holder.ivStar3.setImageResource(R.drawable.ic_star_vote);
            holder.ivStar4.setImageResource(R.drawable.ic_star_vote);
            holder.ivStar5.setImageResource(R.drawable.ic_star_vote);
        }

    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tvTitle;
        private TextView tvLocation;
        private TextView tvPrice;
        private ImageView ivStar1, ivStar2, ivStar3, ivStar4, ivStar5;
        public MyViewHolder(View itemView){
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title_place);
            tvLocation = itemView.findViewById(R.id.tv_location);
            tvPrice = itemView.findViewById(R.id.tv_price);
            ivStar1 = itemView.findViewById(R.id.iv_star_1);
            ivStar2 = itemView.findViewById(R.id.iv_star_2);
            ivStar3 = itemView.findViewById(R.id.iv_star_3);
            ivStar4 = itemView.findViewById(R.id.iv_star_4);
            ivStar5 = itemView.findViewById(R.id.iv_star_5);
        }
    }
}
