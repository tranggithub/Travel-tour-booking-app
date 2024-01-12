package com.example.travel_tour_booking_app;

public class Notification {
    String title;
    String text;
    String ThumbnailURL;
    String date;
    String Key;

    public Notification(String title, String text, String thumbnailURL, String date) {
        this.title = title;
        this.text = text;
        ThumbnailURL = thumbnailURL;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String getThumbnailURL() {
        return ThumbnailURL;
    }

    public String getDate() {
        return date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setThumbnailURL(String thumbnailURL) {
        ThumbnailURL = thumbnailURL;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }
}
