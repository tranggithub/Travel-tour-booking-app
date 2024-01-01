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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DetailNotificationActivity extends AppCompatActivity {

    ArrayList<DetailNews> detailNotificationArrayList;
    DetailNotificationAdapter detailNotificationAdapter;
    //FirebaseAuth
    FirebaseAuth mAuth;
    TextView tv_update_notification;
    String title, date, key;
    Notification notification;
    boolean isAdmin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_notification);

        mAuth = FirebaseAuth.getInstance();
        tv_update_notification = findViewById(R.id.tv_update_notification);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Nếu là admin thì sẽ có chức năng sửa ngược lại thì không
            checkIsAdmin(currentUser.getUid());
        }

        TextView tv_title = findViewById(R.id.tv_title_details_notification);
        TextView tv_date = findViewById(R.id.tv_date_details_notification);
        TextView tv_text = findViewById(R.id.tv_text_details_notification);

        //Lấy nội dung được gửi vào intent
        notification = (Notification) getIntent().getSerializableExtra("DetailNotificationList");
        if (notification != null){
            tv_title.setText(notification.getTitle().toString());
            tv_date.setText(notification.getUploadDate().toString());
            tv_text.setText(notification.getText().toString());
        }

        //Thêm nội dung detail
        detailNotificationArrayList = new ArrayList<>();

        DetailNews tempDetailNotifocation1 = new DetailNews(notification.getThumbnail(), null,null,true);
        detailNotificationArrayList.add(tempDetailNotifocation1);

        for (DetailNews item : notification.getDetailNotificationArrayList()){
            detailNotificationArrayList.add(item);
        }
        detailNotificationAdapter = new DetailNotificationAdapter(this,detailNotificationArrayList,15);

        RecyclerView rvDetailNotification = findViewById(R.id.rv_detail_notification);
        rvDetailNotification.setLayoutManager(new LinearLayoutManager(this));
        rvDetailNotification.setAdapter(detailNotificationAdapter);

        ScrollToTop();
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
                        tv_update_notification.setVisibility(View.GONE);
                        isAdmin = false;

                    } else if ("admin".equals(userDetails.getRole()) && userDetails.getDelected() == 0) {
                        tv_update_notification.setVisibility(View.VISIBLE);
                        isAdmin = true;

                    } else {
                        Toast.makeText(DetailNotificationActivity.this, "Người dùng đã bị xóa", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi ở đây
            }
        });
    }
    private void ScrollToTop() {
        Button button = findViewById(R.id.btn_detail_notification_up);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScrollView scrollView = findViewById(R.id.sv_detail_notification);
                scrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        });
    }

    public void ChangeUpdateNotification(View view){
        Intent intent = new Intent(DetailNotificationActivity.this, UpdateNotificationActivity.class);
        intent.putExtra("DetailNotificationList", (Notification) notification);
        startActivity(intent);
    }

    public void GoBack (View view){
        if (isAdmin)
        {
            Intent intent = new Intent(this,ListNotificationAdminActivity.class);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(this,ListNotificationActivity.class);
            startActivity(intent);
        }
        finish();
    }
}

