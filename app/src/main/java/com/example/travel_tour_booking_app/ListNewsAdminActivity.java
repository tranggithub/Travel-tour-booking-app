package com.example.travel_tour_booking_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.Normalizer;
import java.util.ArrayList;

public class ListNewsAdminActivity extends AppCompatActivity {
    ArrayList<News> newss;
    NewsAdapter newsAdapter;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;

    ArrayList<Pagination> paginationArrayList;
    PaginationAdapter paginationAdapter;
    String DatabaseUrl = "https://travel-tour-booking-app-default-rtdb.asia-southeast1.firebasedatabase.app";
    EditText et_search;
    TextView tv_add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_news_admin);


        //change to upload page
        AddNews();

        //News
        newss = new ArrayList<>();

        newsAdapter = new NewsAdapter(this,newss);

        RecyclerView rvNews = findViewById(R.id.rv_list_news);
        rvNews.setLayoutManager(new LinearLayoutManager(this));
        rvNews.setAdapter(newsAdapter);

        LoadNews();

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

        //Search Function
        et_search = findViewById(R.id.et_news_search);

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                performSearch(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Scroll to Top
        ScrollToTop();
    }

    private void performSearch(String query) {
        ArrayList<News> searchList = new ArrayList<>();
        for (News item : newss){
            if(normalizeString(item.getTitle().toLowerCase()).contains(query.toLowerCase())){
                searchList.add(item);
            }
        }
        newsAdapter.searchNews(searchList);
    }
    private String normalizeString(String input) {
        // Loại bỏ dấu từ chuỗi
        String regex = "\\p{InCombiningDiacriticalMarks}+";
        String normalizedString = Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll(regex, "")
                .toLowerCase();
        return normalizedString;
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

    @Override
    protected void onResume() {
        super.onResume();
        LoadNews();
    }

    private void LoadNews(){
        //Progress layout
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        //Firebase
        databaseReference = FirebaseDatabase.getInstance(DatabaseUrl).getReference("Android News");
//        alertDialog.show();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                newss.clear();
                for (DataSnapshot itemSnapshot: snapshot.getChildren()){
                    News tempNews = itemSnapshot.getValue(News.class);
                    tempNews.setKey(itemSnapshot.getKey());
                    newss.add(tempNews);
                }
                newsAdapter.notifyDataSetChanged();
                alertDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                alertDialog.dismiss();
            }
        });
    }

    public void GoBack(View view)
    {
        finish();
    }

    public void AddNews()
    {
        tv_add = findViewById(R.id.tv_add_news);
        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListNewsAdminActivity.this, UploadNewsActivity.class);
                startActivity(intent);
            }
        });
    }


}