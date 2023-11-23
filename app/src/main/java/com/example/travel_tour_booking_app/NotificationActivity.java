package com.example.travel_tour_booking_app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {
    ArrayList<Notification> notifications;
    NotificationAdapter notificationAdapter;
    ArrayList<Pagination> paginationArrayList;
    PaginationAdapter paginationAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        //Progress layout
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog alertDialog = builder.create();
//        alertDialog.show();

        //News
        notifications = new ArrayList<>();

        notificationAdapter = new NotificationAdapter(this, notifications);

        RecyclerView rvNews = findViewById(R.id.rv_list_notifications);
        rvNews.setLayoutManager(new LinearLayoutManager(this));
        rvNews.setAdapter(notificationAdapter);

        //Nếu thay đổi thì cập nhật ở đây
        notifications.add(new Notification("Bạn đã chuẩn bị gì cho Lễ?",
                "Hãy để 4Travel gợi ý cho bạn các chuyến đi thật thú vị trong Lễ Giổ Tổ, 30/04 và 01/05 nhé.",
                "123","19:05 26/04/2023"));
        notificationAdapter.notifyDataSetChanged();

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
    }
}