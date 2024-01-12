package com.example.travel_tour_booking_app;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.MyViewHolder> {
    Context context;
    List<Hotel> hotels;
    int layout;

    public HotelAdapter (Context context, List<Hotel> hotels, int layout){
        this.context = context;
        this.hotels = hotels;
        this.layout = layout;
    }
    @Override
    public HotelAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(context).inflate(layout, parent, false);
        HotelAdapter.MyViewHolder myViewHolder = new HotelAdapter.MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HotelAdapter.MyViewHolder holder, int position) {
        Glide.with(context).load(hotels.get(position).getThumbnail()).into(holder.ivThumbnail);
        holder.tvTitle.setText(hotels.get(position).getName());
        holder.tvLocation.setText(hotels.get(position).getAddress());
        holder.tvPrice.setText("");
        ArrayList<Comment> comments = hotels.get(position).getComments();
        float star;
        if(comments == null || comments.size() == 0)
            star = 0;
        else {
            float sum = 0;
            for (Comment item:comments){
                sum += item.getStar();
            }
            star = sum/comments.size();
        }
        if (star < 1) {
            holder.ivStar1.setImageResource(R.drawable.ic_star_not_vote);
            holder.ivStar2.setImageResource(R.drawable.ic_star_not_vote);
            holder.ivStar3.setImageResource(R.drawable.ic_star_not_vote);
            holder.ivStar4.setImageResource(R.drawable.ic_star_not_vote);
            holder.ivStar5.setImageResource(R.drawable.ic_star_not_vote);
        } else if (star < 2) {
            holder.ivStar1.setImageResource(R.drawable.ic_star_vote);
            holder.ivStar2.setImageResource(R.drawable.ic_star_not_vote);
            holder.ivStar3.setImageResource(R.drawable.ic_star_not_vote);
            holder.ivStar4.setImageResource(R.drawable.ic_star_not_vote);
            holder.ivStar5.setImageResource(R.drawable.ic_star_not_vote);
        } else if (star < 3) {
            holder.ivStar1.setImageResource(R.drawable.ic_star_vote);
            holder.ivStar2.setImageResource(R.drawable.ic_star_vote);
            holder.ivStar3.setImageResource(R.drawable.ic_star_not_vote);
            holder.ivStar4.setImageResource(R.drawable.ic_star_not_vote);
            holder.ivStar5.setImageResource(R.drawable.ic_star_not_vote);
        } else if (star < 4) {
            holder.ivStar1.setImageResource(R.drawable.ic_star_vote);
            holder.ivStar2.setImageResource(R.drawable.ic_star_vote);
            holder.ivStar3.setImageResource(R.drawable.ic_star_vote);
            holder.ivStar4.setImageResource(R.drawable.ic_star_not_vote);
            holder.ivStar5.setImageResource(R.drawable.ic_star_not_vote);
        } else if (star < 5) {
            holder.ivStar1.setImageResource(R.drawable.ic_star_vote);
            holder.ivStar2.setImageResource(R.drawable.ic_star_vote);
            holder.ivStar3.setImageResource(R.drawable.ic_star_vote);
            holder.ivStar4.setImageResource(R.drawable.ic_star_vote);
            holder.ivStar5.setImageResource(R.drawable.ic_star_not_vote);
        } else {
            holder.ivStar1.setImageResource(R.drawable.ic_star_vote);
            holder.ivStar2.setImageResource(R.drawable.ic_star_vote);
            holder.ivStar3.setImageResource(R.drawable.ic_star_vote);
            holder.ivStar4.setImageResource(R.drawable.ic_star_vote);
            holder.ivStar5.setImageResource(R.drawable.ic_star_vote);
        }
        holder.cl_item_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailHotelActivity.class);
                intent.putExtra("Hotel", (Hotel) hotels.get(holder.getAdapterPosition()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return hotels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tvTitle;
        private TextView tvLocation;
        private TextView tvPrice;
        private ImageView ivThumbnail;
        private ImageView ivStar1, ivStar2, ivStar3, ivStar4, ivStar5;
        private ConstraintLayout cl_item_place;

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
            ivThumbnail = itemView.findViewById(R.id.iv_place);

            cl_item_place = itemView.findViewById(R.id.cl_item_place);

        }
    }
    public void searchHotel(ArrayList<Hotel> hotels){
        this.hotels = hotels;
        notifyDataSetChanged();
    }

    public void sortByNews(){
        Collections.reverse(hotels);
        notifyDataSetChanged();
    }

}
