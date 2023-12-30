package com.example.travel_tour_booking_app;

import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;

public class Comment implements Serializable {
    String userId;
    //        ReadWriteUserDetails user;
    Float Star = null;
    String Text = null;
    String Date = null;
    //    ArrayList<ReadWriteUserDetails> ListUsersLike = new ArrayList<>();
    //    ArrayList<ReadWriteUserDetails> ListUsersDishLike = new ArrayList<>();
    ArrayList<String> ListUsersLike = new ArrayList<>();
    ArrayList<String> ListUsersDishLike = new ArrayList<>();


    //    public Comment(ReadWriteUserDetails user, Float star, String text, String date, ArrayList<ReadWriteUserDetails> listUsersLike, ArrayList<ReadWriteUserDetails> listUsersDishLike) {
//        this.user = user;
//        Star = star;
//        Text = text;
//        Date = date;
//        ListUsersLike = listUsersLike;
//        ListUsersDishLike = listUsersDishLike;
//    }
    public Comment(String userId, float rating, String reviewText,
                   String date) {
        this.userId = userId;
        this.Star = rating;
        this.Text = reviewText;
        this.Date = date;
        this.ListUsersLike = new ArrayList<>();
        this.ListUsersDishLike = new ArrayList<>();
    }
    public Comment(String userId, float rating, String reviewText, ArrayList<String> ListUsersLike , ArrayList<String> ListUsersDishLike,
                   String date) {
        this.userId = userId;
        this.Star = rating;
        this.Text = reviewText;
        this.Date = date;
        this.ListUsersLike = ListUsersLike;
        this.ListUsersDishLike = ListUsersDishLike;
    }
    public Comment() {
    }

    //    public ReadWriteUserDetails getUser() {
//        return user;
//    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

//    public ArrayList<ReadWriteUserDetails> getListUsersLike() {
//        return ListUsersLike;
//    }
//
//    public ArrayList<ReadWriteUserDetails> getListUsersDishLike() {
//        return ListUsersDishLike;
//    }
//
//    public void setUser(ReadWriteUserDetails user) {
//        this.user = user;
//    }

    public void setStar(Float star) {
        Star = star;
    }

    public void setText(String text) {
        Text = text;
    }

    public void setDate(String date) {
        Date = date;
    }

    //    public void setListUsersLike(ArrayList<ReadWriteUserDetails> listUsersLike) {
//        ListUsersLike = listUsersLike;
//    }
//
//    public void setListUsersDishLike(ArrayList<ReadWriteUserDetails> listUsersDishLike) {
//        ListUsersDishLike = listUsersDishLike;
//    }
    public ArrayList<String> getListUsersDishLike() {
        return ListUsersDishLike;
    }

    public void setListUsersDishLike(ArrayList<String> listUsersDishLike) {
        ListUsersDishLike = listUsersDishLike;
    }

    public void setListUsersLike(ArrayList<String> listUsersLike) {
        ListUsersLike = listUsersLike;
    }

    public ArrayList<String> getListUsersLike() {
        return ListUsersLike;
    }

    public int getNumLikes() {
        return ListUsersLike.size();
    }

    public boolean isLiked(String userId) {
        return ListUsersLike.contains(userId);
    }

    public int getNumUnlikes() {
        return ListUsersDishLike.size();
    }

    public boolean isUnliked(String userId) {
        return ListUsersDishLike.contains(userId);
    }
}
