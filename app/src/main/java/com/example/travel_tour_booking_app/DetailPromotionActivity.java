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
    ArrayList<DetailNews> detailPromotionList;
    DetailNewsAdapter detailPromotionAdapter;
    Promotion promotion;
    boolean isAdmin;
    TextView tv_update_promotion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_promotion);

        TextView tv_title = findViewById(R.id.tv_title_details_promotion);
        TextView tv_date = findViewById(R.id.tv_date_details_promotion);
        tv_update_promotion = findViewById(R.id.tv_update_promotion);

        //Lấy nội dung từ intent()
        Bundle bundle = getIntent().getExtras();
        promotion = (Promotion) getIntent().getSerializableExtra("DetailPromotionList");
        if (bundle!= null){
            tv_title.setText(bundle.getString("Title"));
            tv_date.setText(bundle.getString("Date"));
        }
        //Thêm nội dung detail
        detailPromotionList = new ArrayList<>();

        DetailNews tempDetailNews1 = new DetailNews(promotion.getThumbnail(), null,null,true);
        detailPromotionList.add(tempDetailNews1);

        for (DetailNews item : promotion.getDetailPromotionList()){
            detailPromotionList.add(item);
        }
        detailPromotionAdapter = new DetailNewsAdapter(this,detailPromotionList,15);

        RecyclerView rvDetailNews = findViewById(R.id.rv_detail_promotion);
        rvDetailNews.setLayoutManager(new LinearLayoutManager(this));
        rvDetailNews.setAdapter(detailPromotionAdapter);

        ScrollToTop();

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