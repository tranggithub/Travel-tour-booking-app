package com.example.travel_tour_booking_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DetailPromotionActivity extends AppCompatActivity {
    private static String FIREBASE_REALTIME_DATABASE_URL = "https://travel-tour-booking-app-default-rtdb.asia-southeast1.firebasedatabase.app/";
    ArrayList<DetailNews> detailPromotionList;
    DetailNewsAdapter detailPromotionAdapter;
    Promotion promotion;
    boolean isAdmin;
    TextView tv_update_promotion, tv_title, tv_date;
    RecyclerView rvDetailNews;
    String Key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_promotion);

        tv_title = findViewById(R.id.tv_title_details_promotion);
        tv_date = findViewById(R.id.tv_date_details_promotion);
        tv_update_promotion = findViewById(R.id.tv_update_promotion);
        rvDetailNews = findViewById(R.id.rv_detail_promotion);

        //Lấy nội dung từ intent()
        Bundle bundle = getIntent().getExtras();
        if (bundle!= null){
            Key = bundle.getString("Key");
        }
        promotion = (Promotion) getIntent().getSerializableExtra("DetailPromotionList");

        ScrollToTop();

    }
    @Override
    public void onResume() {
        super.onResume();
        LoadView();
    }

    @Override
    public void onStart() {
        super.onStart();
        LoadView();
    }
    private void LoadView(){
        DatabaseReference databaseReferenceHotel = FirebaseDatabase.getInstance(FIREBASE_REALTIME_DATABASE_URL).getReference("Android Promotion");
        databaseReferenceHotel.child(Key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    promotion = snapshot.getValue(Promotion.class);
                    promotion.setKey(snapshot.getKey());
                    if (promotion!= null){
                        tv_title.setText(promotion.getTitle());
                        tv_date.setText(promotion.getStartDateString()+" - "+promotion.getEndDateString());
                        //Thêm nội dung detail
                        detailPromotionList = new ArrayList<>();

                        DetailNews tempDetailNews1 = new DetailNews(promotion.getThumbnail(), null,null,true);
                        detailPromotionList.add(tempDetailNews1);

                        for (DetailNews item : promotion.getDetailPromotionList()){
                            detailPromotionList.add(item);
                        }
                        detailPromotionAdapter = new DetailNewsAdapter(getBaseContext(),detailPromotionList,15);


                        rvDetailNews.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                        rvDetailNews.setAdapter(detailPromotionAdapter);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void ScrollToTop()
    {
        Button button = findViewById(R.id.btn_detail_promotion_up);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScrollView scrollView = findViewById(R.id.sv_detail_promotion);
                scrollView.fullScroll(ScrollView.FOCUS_UP);
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
                        tv_update_promotion.setVisibility(View.GONE);
                        isAdmin = false;

                    } else if ("admin".equals(userDetails.getRole()) && userDetails.getDelected() == 0) {
                        tv_update_promotion.setVisibility(View.VISIBLE);
                        isAdmin = true;

                    } else {
                        Toast.makeText(DetailPromotionActivity.this, "Người dùng đã bị xóa", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi ở đây
            }
        });
    }

    public void ChangeUpdatePromotion(View view){
        Intent intent = new Intent(DetailPromotionActivity.this, UpdatePromotionActivity.class);
        intent.putExtra("DetailPromotion", (Promotion) promotion);
        startActivity(intent);
    }
    public void GoBack(View view){
        finish();
    }
}