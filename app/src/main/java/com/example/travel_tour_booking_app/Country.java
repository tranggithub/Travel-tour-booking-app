package com.example.travel_tour_booking_app;

public class Country {
    int Image;
    String Name;

    public Country(int image, String name) {
        Image = image;
        Name = name;
    }

    public int getImage() {
        return Image;
    }

    public String getName() {
        return Name;
    }

    public void setImage(int image) {
        Image = image;
    }

    public void setName(String name) {
        Name = name;
    }
}
