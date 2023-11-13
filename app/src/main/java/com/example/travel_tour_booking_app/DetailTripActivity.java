package com.example.travel_tour_booking_app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DetailTripActivity extends Activity {
    ArrayList<DetailNews> detailTripArrayList;
    DetailNewsAdapter detailTripAdapter;

    public void ScrollToTop()
    {
        Button button = findViewById(R.id.btn_detail_trip_up);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScrollView scrollView = findViewById(R.id.sv_detail_trip);
                scrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        });

    }
    public void GoBack()
    {
        ImageView imageView = findViewById(R.id.iv_returnbutton_list_trip);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
