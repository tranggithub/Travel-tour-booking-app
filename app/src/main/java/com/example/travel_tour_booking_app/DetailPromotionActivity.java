package com.example.travel_tour_booking_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

public class DetailPromotionActivity extends AppCompatActivity {
    ArrayList<DetailNews> detailPromotionList;
    DetailNewsAdapter detailPromotionAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_promotion);

        TextView tv_title = findViewById(R.id.tv_title_details_promotion);
        TextView tv_date = findViewById(R.id.tv_date_details_promotion);

        //Lấy nội dung từ intent()
        Bundle bundle = getIntent().getExtras();
        Promotion promotion = (Promotion) getIntent().getSerializableExtra("DetailPromotionList");
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
}