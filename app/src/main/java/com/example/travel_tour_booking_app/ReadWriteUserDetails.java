package com.example.travel_tour_booking_app;

public class ReadWriteUserDetails {
    private String ho;
    private String ten;
    private String urlImage;
    private String role;
    private int delected;
    // Default image URL
    private static final String DEFAULT_IMAGE_URL = "gs://travel-tour-booking-app.appspot.com/Android Users Image/default.png";

    ReadWriteUserDetails(String ho, String ten) {
        this.ho = ho;
        this.ten = ten;
        this.urlImage = DEFAULT_IMAGE_URL;
        this.role = "user";
        this.delected = 0;
    }
    ReadWriteUserDetails(String userDisplayname){
        this.ho = null;
        this.ten = userDisplayname;
        this.urlImage = DEFAULT_IMAGE_URL;
        this.role = "user";
        this.delected = 0;
    }

    public String getHo() {
        return ho;
    }

    public void setHo(String ho) {
        this.ho = ho;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setDelected(int delected) {
        this.delected = delected;
    }

    public int getDelected() {
        return delected;
    }

}
