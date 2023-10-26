package com.example.travel_tour_booking_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class PaginationAdapter extends RecyclerView.Adapter<PaginationAdapter.MyViewHolder> {
    Context context;
    List<Pagination> paginations;

    public PaginationAdapter(Context context, List<Pagination> paginations){
        this.context = context;
        this.paginations = paginations;
    }
    @Override
    public PaginationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.item_pagination, parent, false);
        PaginationAdapter.MyViewHolder myViewHolder = new PaginationAdapter.MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PaginationAdapter.MyViewHolder holder, int position) {
        holder.tvPage.setText(paginations.get(position).getPage());

    }

    @Override
    public int getItemCount() {
        return paginations.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tvPage;
        public MyViewHolder(View itemView){
            super(itemView);
            tvPage = itemView.findViewById(R.id.tv_pigination_discover);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }
    }
}