package com.example.travel_tour_booking_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TienIchAdapter extends RecyclerView.Adapter<TienIchAdapter.MyViewHolder> {
    Context context;
    List<TienIch> tienIches;

    public TienIchAdapter (Context context, List<TienIch> TienIches){
        this.context = context;
        this.tienIches = TienIches;
    }
    @Override
    public TienIchAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.item_tienich, parent, false);
        TienIchAdapter.MyViewHolder myViewHolder = new TienIchAdapter.MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TienIchAdapter.MyViewHolder holder, int position) {
        holder.tvName.setText(tienIches.get(position).getName());
        holder.vIcon.setBackgroundResource(tienIches.get(position).getIcon());
    }

    @Override
    public int getItemCount() {
        return tienIches.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tvName;
        private View vIcon;
        public MyViewHolder(View itemView){
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_chitietbay3);
            vIcon = itemView.findViewById(R.id.v_food);
        }
    }

}