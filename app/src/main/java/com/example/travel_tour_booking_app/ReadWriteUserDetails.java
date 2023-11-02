package com.example.travel_tour_booking_app;

public class ReadWriteUserDetails {
    private String ho;
    private String ten;
    private String urlImage;
    // Default image URL
    private static final String DEFAULT_IMAGE_URL = "gs://travel-tour-booking-app.appspot.com/Android Users Image/default.png";
    ReadWriteUserDetails(String ho, String ten){
        this.ho = ho;
        this.ten = ten;
        this.urlImage = DEFAULT_IMAGE_URL;
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
}
