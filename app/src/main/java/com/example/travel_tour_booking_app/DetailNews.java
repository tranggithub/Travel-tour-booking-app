package com.example.travel_tour_booking_app;

public class DetailNews {
    int Image;
    String SubtitleImage;
    String DetailText;
    boolean isImage;

    public DetailNews(int image, String subtitleImage, String detailText, boolean isImage) {
        Image = image;
        SubtitleImage = subtitleImage;
        DetailText = detailText;
        this.isImage = isImage;
    }

    public int getImage() {
        return Image;
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

    public void setImage(int image) {
        Image = image;
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
