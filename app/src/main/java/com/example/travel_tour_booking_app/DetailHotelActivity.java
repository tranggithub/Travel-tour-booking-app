package com.example.travel_tour_booking_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class DetailHotelActivity extends AppCompatActivity {
    TextView tv_edit;
    Hotel hotel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_hotel);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            hotel = (Hotel) getIntent().getSerializableExtra("Hotel", Hotel.class);
        }
        initID();
        ChangeEdit();
    }
    private void initID(){
        tv_edit = findViewById(R.id.tv_detail_hotel_edit);
    }
    private void ChangeEdit(){
        tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailHotelActivity.this,UpdateHotelActivity.class);
                intent.putExtra("Hotel",(Hotel) hotel);
                startActivity(intent);
            }
        });
    }
}