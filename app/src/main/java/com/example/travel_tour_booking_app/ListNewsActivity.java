package com.example.travel_tour_booking_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.Date;

public class ListNewsActivity extends AppCompatActivity {
    ArrayList<News> newss;
    NewsAdapter newsAdapter;
    ArrayList<Pagination> paginationArrayList;
    PaginationAdapter paginationAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_news);

        //News
        newss = new ArrayList<>();
        News tempNews = new News(
                "Khu vui chơi ở Huế",
                new Date(2023,4,14),
                "Nhiều thực khách tỏ ra thích thú với bố trí của công viên");
        newss.add(tempNews);

        newsAdapter = new NewsAdapter(this,newss);

        RecyclerView rvNews = findViewById(R.id.rv_list_news);
        rvNews.setLayoutManager(new LinearLayoutManager(this));
        rvNews.setAdapter(newsAdapter);

        //Pagination
        Pagination tempPagination = new Pagination("1");

        paginationArrayList = new ArrayList<>();
        paginationArrayList.add(tempPagination);
        paginationArrayList.add(tempPagination);
        paginationArrayList.add(tempPagination);
        paginationArrayList.add(tempPagination);
        paginationArrayList.add(tempPagination);
        paginationArrayList.add(tempPagination);

        paginationAdapter = new PaginationAdapter(this,paginationArrayList);

        RecyclerView recyclerViewPagination = findViewById(R.id.rv_pigination_list_new);
        recyclerViewPagination.setAdapter(paginationAdapter);

        //Scroll to Top
        ScrollToTop();
    }
    public void ScrollToTop()
    {
        Button button = findViewById(R.id.btn_up);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScrollView scrollView = findViewById(R.id.sv_list_news);
                scrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        });

    }

    public void GoBack()
    {
        ImageView imageView = findViewById(R.id.iv_returnbutton_list_news);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}