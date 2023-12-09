package com.example.travel_tour_booking_app;

import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

public class Comment implements Serializable {
    ReadWriteUserDetails user;
    Float Star = null;
    String Text = null;
    String Date = null;
    ArrayList<ReadWriteUserDetails> ListUsersLike = new ArrayList<>();
    ArrayList<ReadWriteUserDetails> ListUsersDishLike = new ArrayList<>();


    public Comment(ReadWriteUserDetails user, Float star, String text, String date, ArrayList<ReadWriteUserDetails> listUsersLike, ArrayList<ReadWriteUserDetails> listUsersDishLike) {
        this.user = user;
        Star = star;
        Text = text;
        Date = date;
        ListUsersLike = listUsersLike;
        ListUsersDishLike = listUsersDishLike;
    }

    public Comment() {
    }

    public ReadWriteUserDetails getUser() {
        return user;
    }

    public Float getStar() {
        return Star;
    }

    public String getText() {
        return Text;
    }

    public String getDate() {
        return Date;
    }

    public ArrayList<ReadWriteUserDetails> getListUsersLike() {
        return ListUsersLike;
    }

    public ArrayList<ReadWriteUserDetails> getListUsersDishLike() {
        return ListUsersDishLike;
    }

    public void setUser(ReadWriteUserDetails user) {
        this.user = user;
    }

    public void setStar(Float star) {
        Star = star;
    }

    public void setText(String text) {
        Text = text;
    }

    public void setDate(String date) {
        Date = date;
    }

    public void setListUsersLike(ArrayList<ReadWriteUserDetails> listUsersLike) {
        ListUsersLike = listUsersLike;
    }

    public void setListUsersDishLike(ArrayList<ReadWriteUserDetails> listUsersDishLike) {
        ListUsersDishLike = listUsersDishLike;
    }
}
