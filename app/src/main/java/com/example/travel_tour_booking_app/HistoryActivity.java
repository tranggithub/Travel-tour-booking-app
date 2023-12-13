package com.example.travel_tour_booking_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    RecyclerView rv_booked, rv_recently_viewed, rv_loved;
    ArrayList<Place> arrayListBooked, arrayListRecentlyViewed, arrayListLoved;
    PlaceAdapter placeAdapterBooked, placeAdapterRecentlyViewed, placeAdapterLoved;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        initID();

        //Booked
        arrayListBooked = new ArrayList<>();
        placeAdapterBooked = new PlaceAdapter(this,arrayListBooked,R.layout.item_place);
        rv_booked.setLayoutManager(new LinearLayoutManager(this));
        rv_booked.setAdapter(placeAdapterBooked);

        //Recently View
        arrayListRecentlyViewed = new ArrayList<>();
        placeAdapterRecentlyViewed = new PlaceAdapter(this, arrayListRecentlyViewed, R.layout.item_place);
        rv_recently_viewed.setLayoutManager(new LinearLayoutManager(this));
        rv_recently_viewed.setAdapter(placeAdapterRecentlyViewed);

        //Loved
        arrayListLoved = new ArrayList<>();
        placeAdapterLoved = new PlaceAdapter(this, arrayListLoved, R.layout.item_place);
        rv_loved.setLayoutManager(new LinearLayoutManager(this));
        rv_loved.setAdapter(placeAdapterLoved);

    }

    private void initID(){
        rv_booked = findViewById(R.id.rv_booked);
        rv_recently_viewed = findViewById(R.id.rv_recently_viewed);
        rv_loved = findViewById(R.id.rv_loved);
    }

    public void GoBack(View view){
        finish();
    }
}