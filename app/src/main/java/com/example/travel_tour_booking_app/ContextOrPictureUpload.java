package com.example.travel_tour_booking_app;

import android.net.Uri;

public class ContextOrPictureUpload {
    boolean isPicture = false;
    Uri ImageUri = null;

    public ContextOrPictureUpload(boolean isPicture) {
        this.isPicture = isPicture;
    }

    public boolean isPicture() {
        return isPicture;
    }

    public void setPicture(boolean picture) {
        isPicture = picture;
    }
}
