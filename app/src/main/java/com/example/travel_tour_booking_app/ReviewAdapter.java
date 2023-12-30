package com.example.travel_tour_booking_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private  ArrayList<Comment> reviewList;
    private final Context context;
    private String userId;

    public ReviewAdapter(ArrayList<Comment> reviewList, Context context, String userId) {
        this.reviewList = reviewList;
        this.context = context;
        this.userId = userId;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Comment currentItem = reviewList.get(position);

        // Load image using Picasso
        Picasso.get().load(currentItem.getAvatarUrl()).into(holder.ivAvatar);

        // Set other data to views
        holder.tvTen.setText(currentItem.getName());
        holder.rbVotingStar.setRating(currentItem.getStar());
        holder.tvReviewText.setText(currentItem.getText());
        holder.tvNumLike.setText(String.valueOf(currentItem.getNumLikes()));
        holder.tvNumUnlike.setText(String.valueOf(currentItem.getNumUnlikes()));
        holder.tvDate.setText(currentItem.getDate());

        // Update like and unlike button states
        updateLikeButtonState(holder, currentItem);
        updateUnlikeButtonState(holder, currentItem);

        // Set up click listeners for like and unlike buttons
        holder.ivLike.setOnClickListener(view -> handleLikeButtonClick(currentItem, holder));
        holder.ivUnlike.setOnClickListener(view -> handleUnlikeButtonClick(currentItem, holder));
    }

    private void handleLikeButtonClick(Comment currentItem, ReviewViewHolder holder) {
        if (!currentItem.isLiked(userId)) {
            // If the review is not already liked, proceed with the like action
            currentItem.ListUsersLike.add(userId);
            if (currentItem.isUnliked(userId))
            {
                currentItem.ListUsersDishLike.remove(userId);
            }
        } else {
            // If the review is already liked, undo the like action
            currentItem.ListUsersLike.remove(userId);
        }

        // Update the UI
        updateLikeButtonState(holder, currentItem);
        updateUnlikeButtonState(holder, currentItem);
    }

    private void handleUnlikeButtonClick(Comment currentItem, ReviewViewHolder holder) {
        if (!currentItem.isUnliked(userId)) {
            // If the review is not already unliked, proceed with the unlike action
            currentItem.ListUsersDishLike.add(userId);
            if (currentItem.isLiked(userId))
            {
                currentItem.ListUsersLike.remove(userId);
            }
        } else {
            // If the review is already unliked, undo the unlike action
            currentItem.ListUsersDishLike.remove(userId);
        }

        // Update the UI
        updateLikeButtonState(holder, currentItem);
        updateUnlikeButtonState(holder, currentItem);
        holder.tvNumLike.setText(String.valueOf(currentItem.getNumLikes()));
        holder.tvNumUnlike.setText(String.valueOf(currentItem.getNumUnlikes()));
    }

    private void updateLikeButtonState(ReviewViewHolder holder, Comment currentItem) {
        if (currentItem.isLiked(userId)) {
            holder.ivLike.setImageResource(R.drawable.img_liked);
        } else {
            holder.ivLike.setImageResource(R.drawable.img_like);
        }
        holder.tvNumLike.setText(String.valueOf(currentItem.getNumLikes()));
    }

    private void updateUnlikeButtonState(ReviewViewHolder holder, Comment currentItem) {
        int unlikeDrawableResId = currentItem.isUnliked(userId) ? R.drawable.img_unliked : R.drawable.img_unlike;
        holder.ivUnlike.setImageResource(unlikeDrawableResId);
        holder.tvNumUnlike.setText(String.valueOf(currentItem.getNumUnlikes()));
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAvatar, ivLike, ivUnlike;
        TextView tvTen, tvReviewText, tvNumLike, tvNumUnlike, tvDate;
        RatingBar rbVotingStar;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);

            ivAvatar = itemView.findViewById(R.id.iv_avatar_itemreview);
            tvTen = itemView.findViewById(R.id.tv_ten_itemreview);
            rbVotingStar = itemView.findViewById(R.id.rb_votingstar_itemreview);
            tvReviewText = itemView.findViewById(R.id.tv_reviewtext_itemreview);
            ivLike = itemView.findViewById(R.id.iv_like_review);
            tvNumLike = itemView.findViewById(R.id.tv_numlike_review);
            ivUnlike = itemView.findViewById(R.id.iv_unlike_review);
            tvNumUnlike = itemView.findViewById(R.id.tv_numunlike_review);
            tvDate = itemView.findViewById(R.id.tv_date_review);
        }
    }
    public void setReviewList(ArrayList<Comment> comments){
        this.reviewList = comments;
        notifyDataSetChanged();
    }
}
