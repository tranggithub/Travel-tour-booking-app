package com.example.travel_tour_booking_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CommentRateAdapter extends RecyclerView.Adapter<CommentRateAdapter.CommentViewHolder>{
    private List<CommentModel> comments;

    public CommentRateAdapter(List<CommentModel> comments) {
        this.comments = comments;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        CommentModel comment = comments.get(position);

        holder.textViewComment.setText(comment.getComment());
        holder.ratingBarComment.setRating(comment.getRating());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView textViewComment;
        RatingBar ratingBarComment;

        CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewComment = itemView.findViewById(R.id.textViewComment);
            ratingBarComment = itemView.findViewById(R.id.ratingBarComment);
        }
    }
}
