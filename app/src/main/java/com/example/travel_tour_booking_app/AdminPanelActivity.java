package com.example.travel_tour_booking_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AdminPanelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);
    }

    public void ChangeNews(View view)
    {
        Intent intent = new Intent(this, ListNewsAdminActivity.class);
        startActivity(intent);
    }

    public void ChangePromotion(View view){
        Intent intent = new Intent(this, ListPromotionAdminActivity.class);
        startActivity(intent);
    }
}