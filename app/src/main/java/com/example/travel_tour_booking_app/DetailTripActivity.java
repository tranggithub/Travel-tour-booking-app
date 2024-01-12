package com.example.travel_tour_booking_app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DetailTripActivity extends AppCompatActivity {
    ArrayList<DetailNews> detailTripArrayList;
    DetailNewsAdapter detailTripAdapter;
    Button btn_more_info;
    TextView tv_thongtinlichtrinh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_detail);

        tv_thongtinlichtrinh = findViewById(R.id.tv_thongtinlichtrinh);
        btn_more_info = findViewById(R.id.btn_more_info);
        btn_more_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (btn_more_info.getText().toString().equalsIgnoreCase("Xem thêm"))
                {
                    tv_thongtinlichtrinh.setMaxLines(Integer.MAX_VALUE);//your TextView
                    btn_more_info.setText("Xem thêm");
                }
                else
                {
                    tv_thongtinlichtrinh.setMaxLines(3);//your TextView
                    btn_more_info.setText("Ẩn bớt");
                }
            }
        });
    }

//    public void ScrollToTop()
//    {
//        Button button = findViewById(R.id.btn_detail_trip_up);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ScrollView scrollView = findViewById(R.id.sv_detail_trip);
//                scrollView.fullScroll(ScrollView.FOCUS_UP);
//            }
//        });
//
//    }
//    public void GoBack()
//    {
//        ImageView imageView = findViewById(R.id.iv_returnbutton_list_trip);
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
//    }

}

