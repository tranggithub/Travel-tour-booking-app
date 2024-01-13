package com.example.travel_tour_booking_app;

public class CommentModel {
    private String comment;
    private float rating;

    public CommentModel() {
        // Cần phải có constructor mặc định cho Firebase
    }

    public CommentModel(String comment, float rating) {
        this.comment = comment;
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public float getRating() {
        return rating;
    }
}
