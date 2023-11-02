package com.example.travel_tour_booking_app;

public class ReadWriteUserDetails {
    private String ho;
    private String ten;
    private String urlImage;
    // Default image URL
    private static final String DEFAULT_IMAGE_URL = "URL_OF_YOUR_DEFAULT_IMAGE";
    ReadWriteUserDetails(String ho, String ten){
        this.ho = ho;
        this.ten = ten;
        this.urlImage = DEFAULT_IMAGE_URL;
    }
}
