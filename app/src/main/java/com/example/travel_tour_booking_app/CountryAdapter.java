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

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.MyViewHolder> {
    Context context;
    List<Country> countries;

    public CountryAdapter(Context context, List<Country> countries){
        this.context = context;
        this.countries = countries;
    }
    @Override
    public CountryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.item_places_discover, parent, false);
        CountryAdapter.MyViewHolder myViewHolder = new CountryAdapter.MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CountryAdapter.MyViewHolder holder, int position) {
        holder.tvName.setText(countries.get(position).getName());
        holder.ivConutry.setImageResource(countries.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tvName;
        private ImageView ivConutry;
        public MyViewHolder(View itemView){
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name_country);
            ivConutry = itemView.findViewById(R.id.iv_country);
        }
    }
}