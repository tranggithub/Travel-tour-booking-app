package com.example.travel_tour_booking_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;


public class HomeFragment extends Fragment {
    HomeActivity binding;
    ArrayList<Seclection> seclections;
    SelectionAdapter selectionAdapter;
    ArrayList<Place> places;
    PlaceAdapter placeAdapter;
    ArrayList<Promotion> promotions;
    PromotionAdapter promotionAdapter;
    ArrayList<News> newss;
    NewsAdapter newsAdapter;
    DatabaseReference databaseReference;
    String DatabaseUrl = "https://travel-tour-booking-app-default-rtdb.asia-southeast1.firebasedatabase.app";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //Selector
        seclections = new ArrayList<>();
        seclections.add(Seclection.Place);
        seclections.add(Seclection.Promotion);
        seclections.add(Seclection.News);
        seclections.add(Seclection.Question);

        selectionAdapter =new SelectionAdapter(getContext()
                , R.layout.item_selection,seclections);

        GridView SelectionGridview = view.findViewById(R.id.gv_selection);
        SelectionGridview.setAdapter(selectionAdapter);

        //Place
        places = new ArrayList<>();
        Place tempPlace = new Place("Chuyến du lịch Toronto","Canada","9.190.123",4,R.drawable.img_toronto);
        places.add(tempPlace);

        placeAdapter = new PlaceAdapter(getContext(),places);

        RecyclerView rvPlace = view.findViewById(R.id.rv_place);
        rvPlace.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvPlace.setAdapter(placeAdapter);

        //Promotion
        promotions = new ArrayList<>();
        Promotion tempPromtion = new Promotion(
                "Liên kết ngân hàng",
                R.drawable.img_promotion,
                new Date(2023,4,14),
                new Date(2023,5,20),
                "Liên kết thẻ ngân hàng ngay để nhận giảm giá");
        promotions.add(tempPromtion);

        promotionAdapter = new PromotionAdapter(getContext(),promotions);

        RecyclerView rvPromotion = view.findViewById(R.id.rv_promotion);
        rvPromotion.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvPromotion.setAdapter(promotionAdapter);

        //News
        newss = new ArrayList<>();

        newsAdapter = new NewsAdapter(getContext(),newss);

        RecyclerView rvNews = view.findViewById(R.id.rv_new);
        rvNews.setLayoutManager(new LinearLayoutManager(getContext()));
        rvNews.setAdapter(newsAdapter);


        //Firebase
        databaseReference = FirebaseDatabase.getInstance(DatabaseUrl).getReference("Android News");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                newss.clear();
                for (DataSnapshot itemSnapshot: snapshot.getChildren()){
                    News tempNews = itemSnapshot.getValue(News.class);
                    newss.add(tempNews);
                }
                newsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //Xử lý kéo về đầu trang
        ScrollToTop(view);
        // Inflate the layout for this fragment

        //Xu ly Xem tat ca
        XemTatCaTinTuc(view);

        return view;

    }

    public void ScrollToTop(View v)
    {
        Button button = v.findViewById(R.id.btn_up);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScrollView scrollView = v.findViewById(R.id.sv_home);
                scrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        });

    }
    void XemTatCaTinTuc(View view)
    {
        Button button = view.findViewById(R.id.btn_readall_news);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),ListNewsActivity.class);
                startActivity(intent);
            }
        });
    }
}