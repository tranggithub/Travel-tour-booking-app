package com.example.travel_tour_booking_app;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Promotion {
    String Title;
    int ThumbnailImage;
    Date begin;
    Date end;
    String Text;

    public Promotion(String title, int thumbnailImage, Date begin, Date end, String text) {
        Title = title;
        ThumbnailImage = thumbnailImage;
        this.begin = begin;
        this.end = end;
        Text = text;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setThumbnailImage(int thumbnailImage) {
        ThumbnailImage = thumbnailImage;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public void setText(String text) {
        Text = text;
    }

    public String getTitle() {
        return Title;
    }

    public int getThumbnailImage() {
        return ThumbnailImage;
    }

    public Date getBegin() {
        return begin;
    }

    public Date getEnd() {
        return end;
    }

    public String getText() {
        return Text;
    }
    public String getCurrentDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy"); // Định dạng ngày theo "dd-MM-yy"
        String formattedDate = sdf.format(date); // Lấy ngày hiện tại và định dạng nó
        return formattedDate;}

}
