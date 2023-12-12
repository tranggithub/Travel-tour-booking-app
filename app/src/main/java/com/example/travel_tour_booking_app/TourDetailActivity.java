package com.example.travel_tour_booking_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TourDetailActivity extends AppCompatActivity {
    ArrayList<DetailNews> detailSchedules;
    DetailNewsAdapter detailScheduleAdapter;
    //FirebaseAuth
    FirebaseAuth mAuth;
    TextView tv_update_tour;
    String key;
    //Tour
    TextView tv_title, tv_date, tv_text, tv_location, tv_price, tv_duration, tv_price_navigation, tv_star;
    RatingBar rb_star, rb_hotel_star;
    //Hotel
    TextView tv_hotel_name, tv_hotel_diachi, tv_hotel_star, tv_hotel_comment;

    //Plane
    TextView tv_planefrom, tv_planeduration, tv_segment,tv_planedate, tv_planebrand, tv_timetakeoff, tv_timelanding;

    //Car
    TextView tv_cartype;

    //Hiển thị
    CheckBox ckb_isActive;
    ImageView iv_thumbnail;

    Place tours;
    boolean isAdmin;
    TienIchAdapter tienIchAdapterPlane;
    TienIchAdapter tienIchAdapterCar;
    ArrayList<TienIch> tienIchPlane;
    ArrayList<TienIch> tienIchCar;
    RecyclerView rv_TienIch_Plane;
    RecyclerView rv_TienIch_Car;
    ImageView iv_share;
    ImageView iv_heart;
    ImageView iv_heart_love;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_detail);

        mAuth = FirebaseAuth.getInstance();
        tv_update_tour = findViewById(R.id.tv_update_tour);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Nếu là admin thì sẽ có chức năng sửa ngược lại thì không
            checkIsAdmin(currentUser.getUid());
        }

        initID();

        //Share
        Share();

        //Heart
        Heart();

        //Lấy nội dung được gửi vào intent
        tours = (Place) getIntent().getSerializableExtra("Tour");
        if (tours != null){
            tv_title.setText(tours.getTitle().toString());
            tv_date.setText(tours.getDate().toString());
            tv_text.setText(tours.getText().toString());
            tv_location.setText(tours.getLocation().toString());
            tv_price.setText(tours.getPrice().toString());
            tv_duration.setText(tours.getDuration().toString());
            tv_price_navigation.setText(tours.getPrice());
            Glide.with(this).load(tours.getThumbnail_Image()).into(iv_thumbnail);
            tv_star.setText(tours.getStar() + " sao");
            rb_star.setRating((float) tours.getStar());

            //Hotel
            tv_hotel_name.setText("Khách sạn " + tours.getHotel().getName());
            tv_hotel_diachi.setText(tours.getHotel().getAddress());

            //Plane
            tienIchPlane = tours.getPlaneTienIch();
            tienIchAdapterPlane = new TienIchAdapter(this,tienIchPlane);
            rv_TienIch_Plane.setLayoutManager(new LinearLayoutManager(this));
            rv_TienIch_Plane.setAdapter(tienIchAdapterPlane);
            tv_planefrom.setText(tours.getPlaneFrom());
            tv_planeduration.setText(tours.getPlaneDuration());
            tv_segment.setText(tours.NumberOfSegment + " chặng");
            tv_planedate.setText(tours.getPlaneDate());
            tv_planebrand.setText(tours.getPlaneBrand());
            tv_timetakeoff.setText(tours.getTimeTakeOff());
            tv_timelanding.setText(tours.getTimeLanding());

            //Car
            tienIchCar = tours.getCarTienIch();
            tienIchAdapterCar = new TienIchAdapter(this,tienIchCar);
            rv_TienIch_Car.setLayoutManager(new LinearLayoutManager(this));
            rv_TienIch_Car.setAdapter(tienIchAdapterCar);
            tv_cartype.setText(tours.getCarType());

        }
        //        if (bundle!= null){
//            tv_title.setText(bundle.getString("Title"));
//            tv_date.setText(bundle.getString("Date"));
//        }
        //Thêm nội dung detail
        detailSchedules = new ArrayList<>();

        for (DetailNews item : tours.getSchedule()){
            detailSchedules.add(item);
        }
        detailScheduleAdapter = new DetailNewsAdapter(this,detailSchedules,20);

        RecyclerView rvDetailNews = findViewById(R.id.rv_detail_tour_schedule);
        rvDetailNews.setLayoutManager(new LinearLayoutManager(this));
        rvDetailNews.setAdapter(detailScheduleAdapter);

        ScrollToTop();

    }

    private void initID() {
        //Share
        iv_share = findViewById(R.id.iv_share_trip);

        //Heart
        iv_heart = findViewById(R.id.iv_heart_trip);
        iv_heart_love = findViewById(R.id.iv_heart_trip_love);

        //Tour
        tv_title = findViewById(R.id.tv_detail_tour_title);
        tv_date = findViewById(R.id.tv_detail_tour_date);
        tv_text = findViewById(R.id.tv_detail_tour_text);
        tv_location =  findViewById(R.id.tv_detail_tour_diadiem);
        tv_price = findViewById(R.id.tv_detail_tour_price);
        tv_duration = findViewById(R.id.tv_detail_tour_duration);
        iv_thumbnail = findViewById(R.id.img_toronto);
        tv_star = findViewById(R.id.tv_danhgia);
        rb_star = findViewById(R.id.rtbProductRating);

        //Hotel
        tv_hotel_name = findViewById(R.id.tv_detail_tour_hotel_name);
        tv_hotel_diachi = findViewById(R.id.tv_diadiem_khachsan);
        tv_price_navigation = findViewById(R.id.tv_price);

        //Plane
        tv_planefrom = findViewById(R.id.tv_info);
        tv_planeduration = findViewById(R.id.tv_thoigianbay);
        tv_segment = findViewById(R.id.tv_thoigianbay1);
        tv_planedate = findViewById(R.id.tv_detail_tour_date_plane);
        tv_planebrand = findViewById(R.id.tv_hangbay);
        tv_timetakeoff = findViewById(R.id.tv_chitietbay);
        tv_timelanding = findViewById(R.id.tv_chitietbay6);
        rv_TienIch_Plane = findViewById(R.id.rv_detail_tour_tiennghi_plane);

        //Car
        tv_cartype = findViewById(R.id.tv_car);
        rv_TienIch_Car = findViewById(R.id.rv_detail_tour_tiennghi_car);
    }

    private void Share(){
        iv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareDialog dialog;
                dialog = new ShareDialog(TourDetailActivity.this);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
    }

    private void Heart(){
        iv_heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_heart.setVisibility(View.GONE);
                iv_heart_love.setVisibility(View.VISIBLE);
            }
        });

        iv_heart_love.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_heart.setVisibility(View.VISIBLE);
                iv_heart_love.setVisibility(View.GONE);
            }
        });
    }

    private void checkIsAdmin(String userId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://travel-tour-booking-app-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("users");

        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ReadWriteUserDetails userDetails = snapshot.getValue(ReadWriteUserDetails.class);

                    if ("user".equals(userDetails.getRole()) && userDetails.getDelected() == 0) {
                        tv_update_tour.setVisibility(View.GONE);
                        isAdmin = false;

                    } else if ("admin".equals(userDetails.getRole()) && userDetails.getDelected() == 0) {
                        tv_update_tour.setVisibility(View.VISIBLE);
                        isAdmin = true;

                    } else {
                        Toast.makeText(TourDetailActivity.this, "Nội dung đã bị xóa", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi ở đây
            }
        });
    }
    public void ScrollToTop()
    {
        Button button = findViewById(R.id.btn_detail_tour_up);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScrollView scrollView = findViewById(R.id.sv_detail_trip);
                scrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        });

    }

    public void ChangeUpdateNews(View view){
        Intent intent = new Intent(this, UpdateTourActivity.class);
        intent.putExtra("Tour", (Place) tours);
        startActivity(intent);
    }

    public void GoBack (View view){
        if (isAdmin)
        {
            Intent intent = new Intent(this,ListTourAdminActivity.class);
            startActivity(intent);
        }
        finish();
    }
}