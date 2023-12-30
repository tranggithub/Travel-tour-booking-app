package com.example.travel_tour_booking_app;

import static com.example.travel_tour_booking_app.UserInformationActivity.FIREBASE_REALTIME_DATABASE_URL;

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
    FirebaseUser currentUser;
    TextView tv_update_tour;
    String key;
    //Tour
    TextView tv_title, tv_date, tv_text, tv_location, tv_price, tv_duration, tv_price_navigation, tv_star;
    RatingBar rb_star, rb_hotel_star;
    //Hotel
    TextView tv_hotel_name, tv_hotel_diachi, tv_hotel_star, tv_hotel_comment, tv_hotel_breakfast, tv_hotel_checkin, tv_hotel_checkout,tv_hotel_age_free, tv_hotel_age_fee, tv_hotel_additional_fee;
    ImageView iv_hotel_thumbnail;
    RecyclerView rv_hotel_tiennghi;
    ArrayList<TienNghiChung> tienNghiChungArrayList;
    TienNghiChungAdapter tienNghiChungAdapter;
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
    String DatabaseUrl = "https://travel-tour-booking-app-default-rtdb.asia-southeast1.firebasedatabase.app";
    DatabaseReference databaseReferenceHotel;
    Hotel hotel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_detail);

        mAuth = FirebaseAuth.getInstance();
        tv_update_tour = findViewById(R.id.tv_update_tour);
        currentUser = mAuth.getCurrentUser();
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

            //Firebase
            databaseReferenceHotel = FirebaseDatabase.getInstance(DatabaseUrl).getReference("Android Hotel");
            databaseReferenceHotel.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot itemSnapshot: snapshot.getChildren()){
                        Hotel tempHotel = itemSnapshot.getValue(Hotel.class);
                        tempHotel.setKey(itemSnapshot.getKey());
                        if(tempHotel.getKey().contains(tours.getHotel())){
                            hotel = tempHotel;
                            //Hotel
                            tv_hotel_diachi.setText(hotel.getAddress());
                            tv_hotel_name.setText(hotel.getName());
                            tv_hotel_comment.setText("Từ " + hotel.getComments().size() + " đánh giá");
                            float averageStar = 0;
                            if(hotel.getComments()!= null){
                                for (Comment item:hotel.getComments()){
                                    averageStar+=item.getStar();
                                }
                                if(hotel.getComments().size()!=0){
                                    averageStar = averageStar/(hotel.getComments().size());
                                }
                            }
                            tv_hotel_star.setText(averageStar + " sao");
                            rb_hotel_star.setRating(averageStar);
                            Glide.with(getBaseContext()).load(hotel.getThumbnail()).into(iv_hotel_thumbnail);

                            tienNghiChungAdapter.setTienNghiChungs(hotel.getTienNghiChungs());

                            tv_hotel_breakfast.setText(hotel.getBreakfast());
                            tv_hotel_checkin.setText(hotel.getTimeCheckIn());
                            tv_hotel_checkout.setText(hotel.getTimeCheckOut());
                            tv_hotel_age_free.setText(hotel.getChildrenAgeFree()+ " tuổi trở xuống");
                            tv_hotel_age_fee.setText(hotel.getChildrenAgeAdditionFee()+ " tuổi trở xuống");
                            tv_hotel_additional_fee.setText("Phụ phí thêm là " + hotel.getChildenFee());
                            break;
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });


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
        tv_hotel_star = findViewById(R.id.tv_danhgia1);
        tv_hotel_comment = findViewById(R.id.tv_danhgia2);
        rb_hotel_star = findViewById(R.id.rtbProductRating1);
        tv_hotel_breakfast = findViewById(R.id.tv_restaurant);
        tv_hotel_checkin = findViewById(R.id.tv_gionhanphong);
        tv_hotel_checkout = findViewById(R.id.tv_giotraphong);
        tv_hotel_age_free = findViewById(R.id.tv_free1);
        tv_hotel_age_fee = findViewById(R.id.tv_thanhtoan1);
        tv_hotel_additional_fee = findViewById(R.id.tv_thanhtoan2);
        iv_hotel_thumbnail = findViewById(R.id.iv_detail_tour_hotel_thumbnail);

        rv_hotel_tiennghi = findViewById(R.id.rv_detail_tour_tiennghichung);

        tienNghiChungArrayList = new ArrayList<>();
        tienNghiChungAdapter = new TienNghiChungAdapter(this, tienNghiChungArrayList);
        rv_hotel_tiennghi.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        rv_hotel_tiennghi.setAdapter(tienNghiChungAdapter);

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
        DatabaseReference databaseReference = FirebaseDatabase.getInstance(FIREBASE_REALTIME_DATABASE_URL).getReference("users");
        databaseReference.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails userDetails = snapshot.getValue(ReadWriteUserDetails.class);
                ArrayList<String> tempListLikeTour = userDetails.getListLikeTours();
                if (tempListLikeTour.contains(tours.getKey())){
                    iv_heart.setVisibility(View.GONE);
                    iv_heart_love.setVisibility(View.VISIBLE);
                }
                else {
                    iv_heart.setVisibility(View.VISIBLE);
                    iv_heart_love.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        iv_heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_heart.setVisibility(View.GONE);
                iv_heart_love.setVisibility(View.VISIBLE);
                DatabaseReference databaseReference = FirebaseDatabase.getInstance(FIREBASE_REALTIME_DATABASE_URL).getReference("users");
                databaseReference.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ReadWriteUserDetails userDetails = snapshot.getValue(ReadWriteUserDetails.class);
                        ArrayList<String> tempListLikeTour = userDetails.getListLikeTours();
                        tempListLikeTour.add(tours.getKey());
                        userDetails.setListLikeTours(tempListLikeTour);
                        databaseReference.child(currentUser.getUid()).setValue(userDetails)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        // Lịch sử tìm kiếm đã được cập nhật thành công
                                    } else {
                                        // Lỗi khi cập nhật lịch sử tìm kiếm
                                    }
                                });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        iv_heart_love.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_heart.setVisibility(View.VISIBLE);
                iv_heart_love.setVisibility(View.GONE);

                DatabaseReference databaseReference = FirebaseDatabase.getInstance(FIREBASE_REALTIME_DATABASE_URL).getReference("users");
                databaseReference.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ReadWriteUserDetails userDetails = snapshot.getValue(ReadWriteUserDetails.class);
                        ArrayList<String> tempListLikeTour = userDetails.getListLikeTours();
                        if (tempListLikeTour.contains(tours.getKey())){
                            tempListLikeTour.remove(tours.getKey());
                            userDetails.setListLikeTours(tempListLikeTour);
                            databaseReference.child(currentUser.getUid()).setValue(userDetails)
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            // Lịch sử tìm kiếm đã được cập nhật thành công
                                        } else {
                                            // Lỗi khi cập nhật lịch sử tìm kiếm
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
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
    public void ChangeDetailHotel(View v){
        Intent intent = new Intent(TourDetailActivity.this, DetailHotelActivity.class);
        intent.putExtra("Hotel", (Hotel) hotel);
        intent.putExtra("Tour", (Place) tours);
        startActivity(intent);
    }
}