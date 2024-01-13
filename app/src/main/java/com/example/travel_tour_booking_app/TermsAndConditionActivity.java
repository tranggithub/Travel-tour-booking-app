package com.example.travel_tour_booking_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;

public class TermsAndConditionActivity extends AppCompatActivity {

    ImageView ivBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_condition);
        ScrollToTop();

        //Quay ve
        ivBack = findViewById(R.id.iv_returnbutton_dkvdk);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void ScrollToTop()
    {
        Button button = findViewById(R.id.btn_up_dkvdk);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScrollView scrollView = findViewById(R.id.sv_dkvdk);
                scrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        });

    }
}