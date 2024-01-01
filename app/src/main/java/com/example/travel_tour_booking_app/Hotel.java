package com.example.travel_tour_booking_app;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Hotel implements Serializable {
    String Key;
    String Thumbnail;
    String Address;
    String Name;
    String Introduction;
    String TimeCheckIn;
    String TimeCheckOut;
    ArrayList<TienNghiChung> TienNghiChungs;
    String ChildenFee;
    String ChildrenAgeFree;
    String ChildrenAgeAdditionFee;
    String Breakfast;
    ArrayList<Comment> comments;
    ArrayList<DetailNews> detailPictureList;

    public Hotel() {
        this.detailPictureList = new ArrayList<>();
        this.comments = new ArrayList<>();
    }

    public Hotel(String thumbnail, String address, String name, String introduction, String timeCheckIn, String timeCheckOut, ArrayList<TienNghiChung> tienNghiChungs, String childenFee, String childrenAgeFree, String childrenAgeAdditionFee, String breakfast, ArrayList<DetailNews> detailPictureList) {
        Thumbnail = thumbnail;
        Address = address;
        Name = name;
        Introduction = introduction;
        TimeCheckIn = timeCheckIn;
        TimeCheckOut = timeCheckOut;
        TienNghiChungs = tienNghiChungs;
        ChildenFee = childenFee;
        ChildrenAgeFree = childrenAgeFree;
        ChildrenAgeAdditionFee = childrenAgeAdditionFee;
        Breakfast = breakfast;
        this.detailPictureList = detailPictureList;
        this.comments = new ArrayList<>();
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

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public String getIntroduction() {
        return Introduction;
    }

    public String getChildrenAgeAdditionFee() {
        return ChildrenAgeAdditionFee;
    }

    public String getBreakfast() {
        return Breakfast;
    }

    public ArrayList<DetailNews> getDetailPictureList() {
        return detailPictureList;
    }

    public void setIntroduction(String introduction) {
        Introduction = introduction;
    }

    public void setChildrenAgeAdditionFee(String childrenAgeAdditionFee) {
        ChildrenAgeAdditionFee = childrenAgeAdditionFee;
    }

    public void setBreakfast(String breakfast) {
        Breakfast = breakfast;
    }

    public void setDetailPictureList(ArrayList<DetailNews> detailPictureList) {
        this.detailPictureList = detailPictureList;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }
    public float getAvgStar(){
        if (comments == null || comments.isEmpty()) {
            // No comments available, return 0 or any default value as needed.
            return 0.0f;
        }

        float totalStars = 0.0f;

        // Iterate through each comment and sum up the star ratings
        for (Comment comment : comments) {
            totalStars += comment.getStar();
        }
        float averageStar = totalStars / comments.size();

        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return Float.parseFloat(decimalFormat.format(averageStar));
    }
    public int getSizeComments(){
        return comments.size();
    }
}
