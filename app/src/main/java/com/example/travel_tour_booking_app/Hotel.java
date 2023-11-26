package com.example.travel_tour_booking_app;

import java.io.Serializable;
import java.util.ArrayList;

public class Hotel implements Serializable {
    String Thumbnail;
    String Name;
    String TimeCheckIn;
    String TimeCheckOut;
    ArrayList<TienNghiChung> TienNghiChungs;
    String ChildenFee;
    String ChildrenAgeFree;

    public Hotel(String thumbnail, String name, String timeCheckIn, String timeCheckOut, ArrayList<TienNghiChung> tienNghiChungs, String childenFee, String childrenFree) {
        Thumbnail = thumbnail;
        Name = name;
        TimeCheckIn = timeCheckIn;
        TimeCheckOut = timeCheckOut;
        TienNghiChungs = tienNghiChungs;
        ChildenFee = childenFee;
        ChildrenAgeFree = childrenFree;
    }

    public Hotel() {
    }

    public String getName() {
        return Name;
    }

    public String getTimeCheckIn() {
        return TimeCheckIn;
    }

    public String getTimeCheckOut() {
        return TimeCheckOut;
    }

    public ArrayList<TienNghiChung> getTienNghiChungs() {
        return TienNghiChungs;
    }

    public String getChildenFee() {
        return ChildenFee;
    }

    public String getChildrenAgeFree() {
        return ChildrenAgeFree;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setTimeCheckIn(String timeCheckIn) {
        TimeCheckIn = timeCheckIn;
    }

    public void setTimeCheckOut(String timeCheckOut) {
        TimeCheckOut = timeCheckOut;
    }

    public void setTienNghiChungs(ArrayList<TienNghiChung> tienNghiChungs) {
        TienNghiChungs = tienNghiChungs;
    }

    public void setChildenFee(String childenFee) {
        ChildenFee = childenFee;
    }

    public void setChildrenAgeFree(String childrenAgeFree) {
        ChildrenAgeFree = childrenAgeFree;
    }

    public String getThumbnail() {
        return Thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        Thumbnail = thumbnail;
    }
}
