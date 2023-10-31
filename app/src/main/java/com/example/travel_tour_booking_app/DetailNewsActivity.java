package com.example.travel_tour_booking_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;

import java.util.ArrayList;

public class DetailNewsActivity extends AppCompatActivity {
    ArrayList<DetailNews> detailNewsArrayList;
    DetailNewsAdapter detailNewsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_news);

        //Thêm nội dung detail
        detailNewsArrayList = new ArrayList<>();

        DetailNews tempDetailNews1 = new DetailNews(0,
                null,
                "Một số điểm cắm trại có đầy đủ dịch vụ (glamping) gần Hà Nội và TP HCM đã được đặt kín cho kỳ nghỉ 30/4 trước cả tháng.",
                false);
        detailNewsArrayList.add(tempDetailNews1);
        DetailNews tempDetailNews = new DetailNews(R.drawable.img_leu2,
                "Du khách hoạt động tự do tại các bãi cắm trại dịch vụ. Ảnh: Bích Phương.",
                null,
                true);
        detailNewsArrayList.add(tempDetailNews);
        detailNewsAdapter = new DetailNewsAdapter(this,detailNewsArrayList);

        RecyclerView rvDetailNews = findViewById(R.id.rv_detail_news);
        rvDetailNews.setLayoutManager(new LinearLayoutManager(this));
        rvDetailNews.setAdapter(detailNewsAdapter);
        ScrollToTop();

    }
    public void ScrollToTop()
    {
        Button button = findViewById(R.id.btn_detail_news_up);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScrollView scrollView = findViewById(R.id.sv_detail_news);
                scrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        });

    }
}