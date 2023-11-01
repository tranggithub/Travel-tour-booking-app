package com.example.travel_tour_booking_app;

import java.text.SimpleDateFormat;
import java.util.Date;

public class News {
    String Titile;
    Date UploadDate;
    String Text;
    String Thumbnail;

    public News(String titile, Date uploadDate, String text) {
        Titile = titile;
        UploadDate = uploadDate;
        Text = text;
    }

    public News(String titile, Date uploadDate, String text, String thumbnail) {
        Titile = titile;
        UploadDate = uploadDate;
        Text = text;
        Thumbnail = thumbnail;
    }

    public void setTitile(String titile) {
        Titile = titile;
    }

    public void setUploadDate(Date uploadDate) {
        UploadDate = uploadDate;
    }

    public void setText(String text) {
        Text = text;
    }

    public String getTitile() {
        return Titile;
    }

    public Date getUploadDate() {
        return UploadDate;
    }

    public String getText() {
        return Text;
    }
    public String getCurrentDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy"); // Định dạng ngày theo "dd-MM-yy"
        String formattedDate = sdf.format(date); // Lấy ngày hiện tại và định dạng nó
        return formattedDate;}

    public String getThumbnail() {
        return Thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        Thumbnail = thumbnail;
    }
}
