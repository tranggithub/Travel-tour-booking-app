package com.example.travel_tour_booking_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PiginationAdapter extends RecyclerView.Adapter<PiginationAdapter.MyViewHolder> {
    Context context;
    List<Pigination> piginations;

    public PiginationAdapter (Context context, List<Pigination> piginations){
        this.context = context;
        this.piginations = piginations;
    }
    @Override
    public PiginationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.item_pagination, parent, false);
        PiginationAdapter.MyViewHolder myViewHolder = new PiginationAdapter.MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PiginationAdapter.MyViewHolder holder, int position) {
        holder.tvPage.setText(piginations.get(position).getPage());

    }

    @Override
    public int getItemCount() {
        return piginations.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tvPage;
        public MyViewHolder(View itemView){
            super(itemView);
            tvPage = itemView.findViewById(R.id.tv_pigination_discover);
        }
    }
}