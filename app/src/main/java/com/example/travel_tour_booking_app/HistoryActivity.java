package com.example.travel_tour_booking_app;

import static com.example.travel_tour_booking_app.UserInformationActivity.FIREBASE_REALTIME_DATABASE_URL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    RecyclerView rv_booked, rv_recently_viewed, rv_loved;
    ArrayList<Place> arrayListBooked, arrayListRecentlyViewed, arrayListLoved;
    PlaceAdapter placeAdapterBooked, placeAdapterRecentlyViewed, placeAdapterLoved;
    ArrayList<Place> tours;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        initID();

        //Tours
        tours = new ArrayList<>();
        DatabaseReference databaseRefTour = FirebaseDatabase.getInstance(FIREBASE_REALTIME_DATABASE_URL).getReference("Android Tours");
        databaseRefTour.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tours.clear();
                for (DataSnapshot itemSnapshot: snapshot.getChildren()){
                    Place tempPlace = itemSnapshot.getValue(Place.class);
                    tempPlace.setKey(itemSnapshot.getKey());
                    if(tempPlace.isActive()){
                        tours.add(tempPlace);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
//                alertDialog.dismiss();
            }
        });

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

        Loved();

    }

    private void Loved() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance(FIREBASE_REALTIME_DATABASE_URL).getReference("users");
        databaseReference.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayListLoved.clear();
                ReadWriteUserDetails userDetails = snapshot.getValue(ReadWriteUserDetails.class);
                ArrayList<String> tempListLikeTour = userDetails.getListLikeTours();
                for (Place tour: tours)
                {
                    if (tempListLikeTour.contains(tour.getKey())){
                        arrayListLoved.add(tour);
                    }
                }
                placeAdapterLoved.sortByNews();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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