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

public class DetailNewsActivity extends AppCompatActivity {
    ArrayList<DetailNews> detailNewsArrayList;
    DetailNewsAdapter detailNewsAdapter;
    //FirebaseAuth
    FirebaseAuth mAuth;
    TextView tv_update_news;
    String title, date, key;
    //News chứa nội dung tương ứng với trang được chọn
    News news;
    boolean isAdmin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_news);

        mAuth = FirebaseAuth.getInstance();
        tv_update_news = findViewById(R.id.tv_update_news);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Nếu là admin thì sẽ có chức năng sửa ngược lại thì không
            checkIsAdmin(currentUser.getUid());
        }


        TextView tv_title = findViewById(R.id.tv_title_details_news);
        TextView tv_date = findViewById(R.id.tv_date_details_news);
        TextView tv_text = findViewById(R.id.tv_text_details_news);

        //Lấy nội dung được gửi vào intent
        news = (News) getIntent().getSerializableExtra("DetailNewsList");
        if (news != null){
            tv_title.setText(news.getTitle().toString());
            tv_date.setText(news.getUploadDate().toString());
            tv_text.setText(news.getText().toString());
        }
//        if (bundle!= null){
//            tv_title.setText(bundle.getString("Title"));
//            tv_date.setText(bundle.getString("Date"));
//        }
        //Thêm nội dung detail
        detailNewsArrayList = new ArrayList<>();

        DetailNews tempDetailNews1 = new DetailNews(news.getThumbnail(), null,null,true);
        detailNewsArrayList.add(tempDetailNews1);

        for (DetailNews item : news.getDetailNewsArrayList()){
            detailNewsArrayList.add(item);
        }
        detailNewsAdapter = new DetailNewsAdapter(this,detailNewsArrayList,15);

        RecyclerView rvDetailNews = findViewById(R.id.rv_detail_news);
        rvDetailNews.setLayoutManager(new LinearLayoutManager(this));
        rvDetailNews.setAdapter(detailNewsAdapter);

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
                        tv_update_news.setVisibility(View.GONE);
                        isAdmin = false;

                    } else if ("admin".equals(userDetails.getRole()) && userDetails.getDelected() == 0) {
                        tv_update_news.setVisibility(View.VISIBLE);
                        isAdmin = true;

                    } else {
                        Toast.makeText(DetailNewsActivity.this, "Người dùng đã bị xóa", Toast.LENGTH_SHORT).show();
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
        Button button = findViewById(R.id.btn_detail_news_up);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScrollView scrollView = findViewById(R.id.sv_detail_news);
                scrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        });

    }

    public void ChangeUpdateNews(View view){
        Intent intent = new Intent(DetailNewsActivity.this, UpdateNewsActivity.class);
        intent.putExtra("DetailNewsList", (News) news);
        startActivity(intent);
    }

    public void GoBack (View view){
        if (isAdmin)
        {
            Intent intent = new Intent(this,ListNewsAdminActivity.class);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(this,ListNewsActivity.class);
            startActivity(intent);
        }
        finish();
    }
}