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

public class DetailNewsActivity extends AppCompatActivity {
    ArrayList<DetailNews> detailNewsArrayList;
    DetailNewsAdapter detailNewsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_news);

        TextView tv_title = findViewById(R.id.tv_title_details_news);
        TextView tv_date = findViewById(R.id.tv_date_details_news);

        //Lấy nội dung từ intent()
        Bundle bundle = getIntent().getExtras();
        News news = (News) getIntent().getSerializableExtra("DetailNewsList");
        if (bundle!= null){
            tv_title.setText(bundle.getString("Title"));
            tv_date.setText(bundle.getString("Date"));
        }
        //Thêm nội dung detail
        detailNewsArrayList = new ArrayList<>();

        DetailNews tempDetailNews1 = new DetailNews(news.getThumbnail(), null,null,true);
        detailNewsArrayList.add(tempDetailNews1);

        for (DetailNews item : news.getDetailNewsArrayList()){
            detailNewsArrayList.add(item);
        }
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