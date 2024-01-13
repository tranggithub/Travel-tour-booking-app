package com.example.travel_tour_booking_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    ArrayList<Search> searches;
    EditText et_home_search;
    Button btn_search_home;
    SearchAdapter searchAdapter;
    RecyclerView rv_search_item;
    FirebaseAuth mAuth;
    FirebaseUser user;
    ArrayList<String> historyKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initID();

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        searches = new ArrayList<>();
        showSearchHistory();
//        searches.add(new Search(R.drawable.icon_history,"Canada"));

        searchAdapter = new SearchAdapter(this,searches);
        rv_search_item.setLayoutManager(new LinearLayoutManager(this));
        rv_search_item.setAdapter(searchAdapter);



        ChangeToSearchResult();
    }
    private void ChangeToSearchResult(){
        btn_search_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this,SearchResultActivity.class);
                intent.putExtra("Text",et_home_search.getText().toString());
                startActivity(intent);

                String key = et_home_search.getText().toString();
                HandleSearchHistory(key);
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
    public void HandleSearchHistory(String key){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://travel-tour-booking-app-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("users");

        databaseReference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ReadWriteUserDetails userDetails = snapshot.getValue(ReadWriteUserDetails.class);
                    historyKey = userDetails.getSearchHistory();
                    if (historyKey.size() == 3){
                        historyKey.remove(0);
                    }
                    historyKey.add(key);
                    userDetails.setSearchHistory(historyKey);
                    updateSearchHistory(userDetails);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi ở đây
            }
        });
    }
    private void updateSearchHistory(ReadWriteUserDetails userDetails) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://travel-tour-booking-app-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("users");

        // Update lịch sử tìm kiếm trên Firebase
        databaseReference.child(user.getUid()).setValue(userDetails)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Lịch sử tìm kiếm đã được cập nhật thành công
                    } else {
                        // Lỗi khi cập nhật lịch sử tìm kiếm
                    }
                });
    }
    private void showSearchHistory(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://travel-tour-booking-app-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("users");

        databaseReference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ReadWriteUserDetails userDetails = snapshot.getValue(ReadWriteUserDetails.class);
                    if (userDetails!=null) {
                        searches.clear();
                        searches.add(new Search(R.drawable.icon_bin,"Xóa lịch sử tìm kiếm"));
                        for (String key : userDetails.getSearchHistory()) {
                            searches.add(new Search(R.drawable.icon_history, key));
                        }

                        searchAdapter.sortByNews();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi ở đây
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        showSearchHistory();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        showSearchHistory();
    }
}