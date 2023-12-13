package com.example.travel_tour_booking_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    ArrayList<Search> searches;
    EditText et_home_search;
    Button btn_search_home;
    SearchAdapter searchAdapter;
    RecyclerView rv_search_item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initID();

        searches = new ArrayList<>();
        searchAdapter = new SearchAdapter(this,searches);
        rv_search_item.setLayoutManager(new LinearLayoutManager(this));
        rv_search_item.setAdapter(searchAdapter);

        searches.add(new Search(R.drawable.icon_history,"Canada"));
        searches.add(new Search(R.drawable.icon_bin,"Xóa lịch sử tìm kiếm"));

        ChangeToSearchResult();
    }
    private void ChangeToSearchResult(){
        btn_search_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this,SearchResultActivity.class);
                intent.putExtra("Text",et_home_search.getText().toString());
                startActivity(intent);
            }
        });

    }

    private void initID(){
        rv_search_item = findViewById(R.id.rv_search_item);
        btn_search_home = findViewById(R.id.btn_search_home);
        et_home_search = findViewById(R.id.et_home_search);
    }
    public void GoBack (View view){
        finish();
    }
}