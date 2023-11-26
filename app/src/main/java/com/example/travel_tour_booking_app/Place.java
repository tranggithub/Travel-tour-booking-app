package com.example.travel_tour_booking_app;

import java.io.Serializable;
import java.util.ArrayList;

public class Place implements Serializable {
    String Title;
    String Date;
    String Location;
    String Price;
    String Duration;
    String Thumbnail_Image;
    String Text;
    ArrayList<DetailNews> Schedule;
    Hotel hotel;
    String PlaneFrom;
    String PlaneDuration;
    int NumberOfSegment;
    String PlaneDate;
    String PlaneBrand;
    String TimeTakeOff;
    String TimeLanding;
    ArrayList<TienIch> PlaneTienIch;
    String CarType;
    ArrayList<TienIch> CarTienIch;
    boolean IsActive;
    double Star = 0;
    int NumberOfPeople;
    int SumOfPoint;

    public Place(String title, String date, String location, String price, String duration, String thumbnail_Image, String text, ArrayList<DetailNews> schedule, Hotel hotel, String planeFrom, String planeDuration, int numberOfSegment, String planeDate, String planeBrand, String timeTakeOff, String timeLanding, ArrayList<TienIch> planeTienIch, String carType, ArrayList<TienIch> carTienIch, boolean isActive) {
        Title = title;
        Date = date;
        Location = location;
        Price = price;
        Duration = duration;
        Thumbnail_Image = thumbnail_Image;
        Text = text;
        Schedule = schedule;
        this.hotel = hotel;
        PlaneFrom = planeFrom;
        PlaneDuration = planeDuration;
        NumberOfSegment = numberOfSegment;
        PlaneDate = planeDate;
        PlaneBrand = planeBrand;
        TimeTakeOff = timeTakeOff;
        TimeLanding = timeLanding;
        PlaneTienIch = planeTienIch;
        CarType = carType;
        CarTienIch = carTienIch;
        IsActive = isActive;
    }


    public void setDuration(String duration) {
        Duration = duration;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public void setThumbnail_Image(String thumbnail_Image) {
        Thumbnail_Image = thumbnail_Image;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setStar(double star) {
        Star = star;
    }

    public String getDuration() {
        return Duration;
    }

    public String getThumbnail_Image() {
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

    public double getStar()
    {
        if(NumberOfPeople == 0)
            return 0;
        Star = (double) SumOfPoint/NumberOfPeople;
        return Star;
    }

    public String getDate() {
        return Date;
    }

    public String getText() {
        return Text;
    }

    public ArrayList<DetailNews> getSchedule() {
        return Schedule;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public String getPlaneFrom() {
        return PlaneFrom;
    }

    public String getPlaneDuration() {
        return PlaneDuration;
    }

    public int getNumberOfSegment() {
        return NumberOfSegment;
    }

    public String getPlaneDate() {
        return PlaneDate;
    }

    public String getPlaneBrand() {
        return PlaneBrand;
    }

    public String getTimeTakeOff() {
        return TimeTakeOff;
    }

    public String getTimeLanding() {
        return TimeLanding;
    }

    public ArrayList<TienIch> getPlaneTienIch() {
        return PlaneTienIch;
    }

    public String getCarType() {
        return CarType;
    }

    public ArrayList<TienIch> getCarTienIch() {
        return CarTienIch;
    }

    public boolean isActive() {
        return IsActive;
    }

    public void setDate(String date) {
        Date = date;
    }

    public void setText(String text) {
        Text = text;
    }

    public void setSchedule(ArrayList<DetailNews> schedule) {
        Schedule = schedule;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public void setPlaneFrom(String planeFrom) {
        PlaneFrom = planeFrom;
    }

    public void setPlaneDuration(String planeDuration) {
        PlaneDuration = planeDuration;
    }

    public void setNumberOfSegment(int numberOfSegment) {
        NumberOfSegment = numberOfSegment;
    }

    public void setPlaneDate(String planeDate) {
        PlaneDate = planeDate;
    }

    public void setPlaneBrand(String planeBrand) {
        PlaneBrand = planeBrand;
    }

    public void setTimeTakeOff(String timeTakeOff) {
        TimeTakeOff = timeTakeOff;
    }

    public void setTimeLanding(String timeLanding) {
        TimeLanding = timeLanding;
    }

    public void setPlaneTienIch(ArrayList<TienIch> planeTienIch) {
        PlaneTienIch = planeTienIch;
    }

    public void setCarType(String carType) {
        CarType = carType;
    }

    public void setCarTienIch(ArrayList<TienIch> carTienIch) {
        CarTienIch = carTienIch;
    }

    public void setActive(boolean active) {
        IsActive = active;
    }
}
