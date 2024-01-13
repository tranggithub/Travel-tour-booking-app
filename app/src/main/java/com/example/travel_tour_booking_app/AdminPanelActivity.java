package com.example.travel_tour_booking_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class AdminPanelActivity extends AppCompatActivity {

    Button btnDangXuat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        btnDangXuat = findViewById(R.id.btn_DangXuat_admin);
        btnDangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                YesNoDialog dialog;
                dialog = new YesNoDialog(AdminPanelActivity.this,"Bạn có xác nhận đăng xuất ?","Có", "Không");
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                dialog.btn_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        FirebaseAuth.getInstance().signOut();
                        finish();
                    }
                });
            }
        });
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
    public void ChangePlace(View view){
        Intent intent = new Intent(this, ListTourAdminActivity.class);
        startActivity(intent);
    }
    public void ChangeHotel (View view){
        Intent intent = new Intent(this, ListHotelAdminActivity.class);
        startActivity(intent);
    }
    public void ChangeNotification (View view){
        Intent intent = new Intent(this, ListNotificationAdminActivity.class);
        startActivity(intent);
    }
    public void ChangeUser (View view){
        Intent intent = new Intent(this, ListUserAdminActivity.class);
        startActivity(intent);
    }
    public void Chat (View view){
        Intent intent = new Intent(this, ListChatAdminActivity.class);
        startActivity(intent);
    }

}