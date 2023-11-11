package com.example.travel_tour_booking_app;

import java.io.Serializable;

public class DetailNews implements Serializable {
    String Picture = null;
    String SubtitleImage;
    String DetailText;
    boolean isImage;

    public DetailNews() {
    }

    public DetailNews(String picture, String subtitleImage, String detailText, boolean isImage) {
        Picture = picture;
        SubtitleImage = subtitleImage;
        DetailText = detailText;
        this.isImage = isImage;
    }

    public DetailNews(boolean isImage) {
        this.isImage = isImage;
    }

    public String getPicture() {
        return Picture;
    }

    public String getSubtitleImage() {
        return SubtitleImage;
    }

    public String getDetailText() {
        return DetailText;
    }

    public boolean isImage() {
        return isImage;
    }


    public void setPicture(String picture) {
        Picture = picture;
    }

    public void setSubtitleImage(String subtitleImage) {
        SubtitleImage = subtitleImage;
    }

    public void setDetailText(String detailText) {
        DetailText = detailText;
    }

    public void setImage(boolean image) {
        isImage = image;
    }
}
