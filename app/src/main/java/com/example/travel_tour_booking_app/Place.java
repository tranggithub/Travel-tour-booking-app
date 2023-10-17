package com.example.travel_tour_booking_app;

public class Place {
    String Title;
    String Location;
    String Price;
    int Duration;
    int Thumbnail_Image;
    double Star = 0;

    public Place (String title, String location, String price, int duration, int thumbnail_Image)
    {
        Title = title;
        Location = location;
        Price = price;
        Duration = duration;
        Thumbnail_Image = thumbnail_Image;
    }

    public void setDuration(int duration) {
        Duration = duration;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public void setThumbnail_Image(int thumbnail_Image) {
        Thumbnail_Image = thumbnail_Image;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setStar(double star) {
        Star = star;
    }

    public int getDuration() {
        return Duration;
    }

    public int getThumbnail_Image() {
        return Thumbnail_Image;
    }

    public String getLocation() {
        return Location;
    }

    public String getPrice() {
        return Price;
    }

    public String getTitle() {
        return Title;
    }

    public double getStar() {
        return Star;
    }

    public void countStar(int NumberOfPeople, int SumOfPoint)
    {
        Star = (double) SumOfPoint/NumberOfPeople;
    }
}
