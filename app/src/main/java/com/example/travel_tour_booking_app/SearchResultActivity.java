package com.example.travel_tour_booking_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.Normalizer;
import java.util.ArrayList;

public class SearchResultActivity extends AppCompatActivity {
    ArrayList<Place> places;
    PlaceAdapter placeAdapter;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;

    ArrayList<Pagination> paginationArrayList;
    PaginationAdapter paginationAdapter;
    String DatabaseUrl = "https://travel-tour-booking-app-default-rtdb.asia-southeast1.firebasedatabase.app";
    TextView tv_place, tv_no_place, tv_sort_by_price, tv_sort_by_star, tv_sort_by_duration;
    String location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            location = bundle.getString("Text");
        }

        initID();

        tv_place.setText("Kết quả tìm kiếm '" + location + "'");

        //Progress layout
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        //Places
        places = new ArrayList<>();

        placeAdapter = new PlaceAdapter(this,places,R.layout.item_place);

        RecyclerView rvNews = findViewById(R.id.rv_list_tours);
        rvNews.setLayoutManager(new LinearLayoutManager(this));
        rvNews.setAdapter(placeAdapter);

        //Firebase
        databaseReference = FirebaseDatabase.getInstance(DatabaseUrl).getReference("Android Tours");
//        alertDialog.show();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                places.clear();
                for (DataSnapshot itemSnapshot: snapshot.getChildren()){
                    Place tempPlace = itemSnapshot.getValue(Place.class);
                    tempPlace.setKey(itemSnapshot.getKey());
                    if(tempPlace.isActive()
                            && (normalizeString(tempPlace.getLocation().toLowerCase()).contains(normalizeString(location.toLowerCase()))
                            || normalizeString(tempPlace.getTitle().toLowerCase()).contains(normalizeString(location.toLowerCase())))){
                        places.add(tempPlace);
                    }
                }
                placeAdapter.notifyDataSetChanged();
                if(placeAdapter.getItemCount()==0){
                    tv_no_place.setVisibility(View.VISIBLE);
                }
                alertDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
//                alertDialog.dismiss();
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



        //Scroll to Top
        ScrollToTop();

        //Sort
        Sort();

    }
    private void initID(){
        tv_place = findViewById(R.id.tv_list_tour_place);
        tv_no_place = findViewById(R.id.tv_list_tour_null);
        tv_sort_by_price = findViewById(R.id.tv_list_tour_sortbyprice);
        tv_sort_by_star = findViewById(R.id.tv_list_tour_sortbyview);
        tv_sort_by_duration = findViewById(R.id.tv_list_tour_sortbyduration);
    }
    private void Sort(){
        tv_sort_by_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_sort_by_price.setBackgroundTintList(ContextCompat.getColorStateList(SearchResultActivity.this, R.color.maincolor));
                tv_sort_by_star.setBackgroundTintList(ContextCompat.getColorStateList(SearchResultActivity.this, R.color.unselected));
                tv_sort_by_duration.setBackgroundTintList(ContextCompat.getColorStateList(SearchResultActivity.this, R.color.unselected));

                placeAdapter.sortByPrice();
            }
        });
        tv_sort_by_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_sort_by_star.setBackgroundTintList(ContextCompat.getColorStateList(SearchResultActivity.this, R.color.maincolor));
                tv_sort_by_price.setBackgroundTintList(ContextCompat.getColorStateList(SearchResultActivity.this, R.color.unselected));
                tv_sort_by_duration.setBackgroundTintList(ContextCompat.getColorStateList(SearchResultActivity.this, R.color.unselected));

                placeAdapter.sortByView();
            }
        });
        tv_sort_by_duration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_sort_by_duration.setBackgroundTintList(ContextCompat.getColorStateList(SearchResultActivity.this, R.color.maincolor));
                tv_sort_by_price.setBackgroundTintList(ContextCompat.getColorStateList(SearchResultActivity.this, R.color.unselected));
                tv_sort_by_star.setBackgroundTintList(ContextCompat.getColorStateList(SearchResultActivity.this, R.color.unselected));

                placeAdapter.sortByDuration();
            }
        });
    }
    private void performSearch(String query) {
        ArrayList<Place> searchList = new ArrayList<>();
        for (Place item : places){
            if(normalizeString(item.getLocation().toLowerCase()).contains(query.toLowerCase())){
                searchList.add(item);
            }
        }
        placeAdapter.searchPlace(searchList);
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
                ScrollView scrollView = findViewById(R.id.sv_list_tours);
                scrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        });

    }

    public void GoBack(View view)
    {
        finish();
    }


}