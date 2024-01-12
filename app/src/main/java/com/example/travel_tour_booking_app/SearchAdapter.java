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
import java.util.Iterator;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {
    Context context;
    List<Search> searches;

    public SearchAdapter (Context context, List<Search> searches){
        this.context = context;
        this.searches = searches;
    }
    @Override
    public SearchAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.item_search, parent, false);
        SearchAdapter.MyViewHolder myViewHolder = new SearchAdapter.MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.MyViewHolder holder, int position) {
        holder.ivIcon.setImageResource(searches.get(position).getIcon());
        holder.tvText.setText(searches.get(position).getText());


        holder.clSearchItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(searches.get(holder.getAdapterPosition()).getIcon() == R.drawable.icon_bin){
                    removeElements(searches,R.drawable.icon_history);
                    notifyDataSetChanged();
                } else {
                    Intent intent = new Intent(context, SearchResultActivity.class);
                    intent.putExtra("Text", searches.get(holder.getAdapterPosition()).getText());
                    context.startActivity(intent);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return searches.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView tvText;
        private ImageView ivIcon;
        ConstraintLayout clSearchItem;

        public MyViewHolder(View itemView){
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_search_icon);
            tvText = itemView.findViewById(R.id.tv_search_text);
            clSearchItem = itemView.findViewById(R.id.cl_search_item);

        }
    }
    public static void removeElements(List<Search> list, int valueToRemove) {
        // Sử dụng Iterator để loại bỏ các phần tử trong quá trình lặp
        Iterator<Search> iterator = list.iterator();
        while (iterator.hasNext()) {
            Search currentValue = iterator.next();
            if (currentValue.getIcon() == valueToRemove) {
                iterator.remove();
            }
        }
    }

}