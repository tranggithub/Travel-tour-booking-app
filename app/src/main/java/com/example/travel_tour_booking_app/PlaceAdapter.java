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

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.MyViewHolder> {
    Context context;
    List<Place> places;
    int layout;
    private int maxItemsToShow = -1; // Mặc định không giới hạn

    public PlaceAdapter (Context context, List<Place> places, int layout, int maxItemsToShow){
        this.context = context;
        this.places = places;
        this.layout = layout;
        this.maxItemsToShow = maxItemsToShow;
    }
    public PlaceAdapter (Context context, List<Place> places, int layout){
        this.context = context;
        this.places = places;
        this.layout = layout;
    }
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(context).inflate(layout, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context).load(places.get(position).getThumbnail_Image()).into(holder.ivThumbnail);
        holder.tvTitle.setText(places.get(position).getTitle());
        holder.tvLocation.setText(places.get(position).getLocation() + " (" + places.get(position).getDuration() + ")");
        holder.tvPrice.setText(places.get(position).getPrice());
        holder.tvView.setText(places.get(position).getView()+"");
        if (places.get(position).getStar() < 1) {
            holder.ivStar1.setImageResource(R.drawable.ic_star_not_vote);
            holder.ivStar2.setImageResource(R.drawable.ic_star_not_vote);
            holder.ivStar3.setImageResource(R.drawable.ic_star_not_vote);
            holder.ivStar4.setImageResource(R.drawable.ic_star_not_vote);
            holder.ivStar5.setImageResource(R.drawable.ic_star_not_vote);
        } else if (places.get(position).getStar() < 2) {
            holder.ivStar1.setImageResource(R.drawable.ic_star_vote);
            holder.ivStar2.setImageResource(R.drawable.ic_star_not_vote);
            holder.ivStar3.setImageResource(R.drawable.ic_star_not_vote);
            holder.ivStar4.setImageResource(R.drawable.ic_star_not_vote);
            holder.ivStar5.setImageResource(R.drawable.ic_star_not_vote);
        } else if (places.get(position).getStar() < 3) {
            holder.ivStar1.setImageResource(R.drawable.ic_star_vote);
            holder.ivStar2.setImageResource(R.drawable.ic_star_vote);
            holder.ivStar3.setImageResource(R.drawable.ic_star_not_vote);
            holder.ivStar4.setImageResource(R.drawable.ic_star_not_vote);
            holder.ivStar5.setImageResource(R.drawable.ic_star_not_vote);
        } else if (places.get(position).getStar() < 4) {
            holder.ivStar1.setImageResource(R.drawable.ic_star_vote);
            holder.ivStar2.setImageResource(R.drawable.ic_star_vote);
            holder.ivStar3.setImageResource(R.drawable.ic_star_vote);
            holder.ivStar4.setImageResource(R.drawable.ic_star_not_vote);
            holder.ivStar5.setImageResource(R.drawable.ic_star_not_vote);
        } else if (places.get(position).getStar() < 5) {
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
                Intent intent = new Intent(context, TourDetailActivity.class);
                intent.putExtra("Tour", (Place) places.get(holder.getAdapterPosition()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (maxItemsToShow == -1) {
            // Trường hợp không giới hạn số lượng item
            return places.size();
        } else {
            // Trường hợp giới hạn số lượng item
            return Math.min(places.size(), maxItemsToShow);
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tvTitle;
        private TextView tvLocation;
        private TextView tvPrice, tvView;
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
            tvView = itemView.findViewById(R.id.tv_view);

            cl_item_place = itemView.findViewById(R.id.cl_item_place);

        }
    }
    public void searchPlace(ArrayList<Place> places){
        this.places = places;
        notifyDataSetChanged();
    }
    public void sortByDuration(){
        Collections.sort(places,new DurationComparator());
        notifyDataSetChanged();
    }
    public void sortByPrice(){
        Collections.sort(places,new PriceComparator());
        notifyDataSetChanged();
    }
    public void sortByView(){
        Collections.sort(places,new ViewComparator());
        notifyDataSetChanged();
    }

    public void sortByNews(){
        Collections.reverse(places);
        notifyDataSetChanged();
    }
}
