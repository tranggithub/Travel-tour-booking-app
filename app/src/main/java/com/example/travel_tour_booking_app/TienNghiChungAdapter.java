package com.example.travel_tour_booking_app;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class TienNghiChungAdapter extends RecyclerView.Adapter<TienNghiChungAdapter.MyViewHolder> {
    Context context;
    List<TienNghiChung> tienNghiChungs;

    public TienNghiChungAdapter (Context context, List<TienNghiChung> tienNghiChungs){
        this.context = context;
        this.tienNghiChungs = tienNghiChungs;
    }
    @Override
    public TienNghiChungAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.item_tour_detail_tiennghichung, parent, false);
        TienNghiChungAdapter.MyViewHolder myViewHolder = new TienNghiChungAdapter.MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TienNghiChungAdapter.MyViewHolder holder, int position) {
        holder.tvName.setText(tienNghiChungs.get(position).getName());
        holder.vIcon.setBackgroundResource(tienNghiChungs.get(position).getIcon());
    }

    @Override
    public int getItemCount() {
        return tienNghiChungs.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tvName;
        private View vIcon;
        ConstraintLayout clTienNghiChung;
        public MyViewHolder(View itemView){
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_tiennghichung_name);
            vIcon = itemView.findViewById(R.id.v_tiennghichung_icon);
            clTienNghiChung = itemView.findViewById(R.id.cl_tiennghichung);
        }
    }

    public void searchNews(ArrayList<TienNghiChung> tienNghiChungs){
        this.tienNghiChungs = tienNghiChungs;
        notifyDataSetChanged();
    }
    public void setTienNghiChungs(ArrayList<TienNghiChung> tienNghiChungArrayList){
        this.tienNghiChungs = tienNghiChungArrayList;
        notifyDataSetChanged();
    }
}