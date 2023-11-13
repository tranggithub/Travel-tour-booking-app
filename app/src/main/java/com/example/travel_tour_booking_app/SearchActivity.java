package com.example.travel_tour_booking_app;

import static com.example.travel_tour_booking_app.R.id.iv_returnbutton_search;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class SearchActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_search);
    }
    public void GoBack()
    {
        ImageView imageView = findViewById(iv_returnbutton_search);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}

