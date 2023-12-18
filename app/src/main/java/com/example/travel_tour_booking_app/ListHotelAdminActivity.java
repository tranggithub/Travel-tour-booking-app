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
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.Normalizer;
import java.util.ArrayList;

public class ListHotelAdminActivity extends AppCompatActivity {
    ArrayList<Hotel> hotels;
    HotelAdapter hotelAdapter;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;

    ArrayList<Pagination> paginationArrayList;
    PaginationAdapter paginationAdapter;
    String DatabaseUrl = "https://travel-tour-booking-app-default-rtdb.asia-southeast1.firebasedatabase.app";
    EditText et_search;
    TextView tv_add_hotels;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_hotel_admin);

        initID();
        //Progress layout
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        //News
        hotels = new ArrayList<>();

        hotelAdapter = new HotelAdapter(this,hotels,R.layout.item_place);

        RecyclerView rvNews = findViewById(R.id.rv_list_hotels);
        rvNews.setLayoutManager(new LinearLayoutManager(this));
        rvNews.setAdapter(hotelAdapter);

        //Firebase
        databaseReference = FirebaseDatabase.getInstance(DatabaseUrl).getReference("Android Hotel");
//        alertDialog.show();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                hotels.clear();
                for (DataSnapshot itemSnapshot: snapshot.getChildren()){
                    Hotel tempHotel = itemSnapshot.getValue(Hotel.class);
                    tempHotel.setKey(itemSnapshot.getKey());
                    hotels.add(tempHotel);
                }
                hotelAdapter.notifyDataSetChanged();
                alertDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                alertDialog.dismiss();
            }
        });

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
        et_search = findViewById(R.id.et_hotels_search);

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
        ChaneToAddHotel();
    }
    private void initID(){
        tv_add_hotels = findViewById(R.id.tv_add_hotels);
    }
    private void ChaneToAddHotel(){
        tv_add_hotels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListHotelAdminActivity.this, UploadHotelActivity.class);
                startActivity(intent);
            }
        });
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
    private void performSearch(String query) {
        ArrayList<Hotel> searchList = new ArrayList<>();
        for (Hotel item : hotels){
            if(normalizeString(item.getName().toLowerCase()).contains(query.toLowerCase())){
                searchList.add(item);
            }
        }
        hotelAdapter.searchHotel(searchList);
    }
    private String normalizeString(String input) {
        // Loại bỏ dấu từ chuỗi
        String regex = "\\p{InCombiningDiacriticalMarks}+";
        String normalizedString = Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll(regex, "")
                .toLowerCase();
        return normalizedString;
    }

    public void GoBack(View view){
        finish();
    }
}