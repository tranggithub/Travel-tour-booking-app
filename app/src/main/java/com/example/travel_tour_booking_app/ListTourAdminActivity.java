package com.example.travel_tour_booking_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListTourAdminActivity extends AppCompatActivity {
    ArrayList<Place> places;
    PlaceAdapter placeAdapter;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;

    ArrayList<Pagination> paginationArrayList;
    PaginationAdapter paginationAdapter;
    String DatabaseUrl = "https://travel-tour-booking-app-default-rtdb.asia-southeast1.firebasedatabase.app";
    EditText et_search;
    TextView tv_add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_tour_admin);
    }
}