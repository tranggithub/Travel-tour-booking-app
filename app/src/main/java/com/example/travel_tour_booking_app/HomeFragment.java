package com.example.travel_tour_booking_app;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;

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

        // Inflate the layout for this fragment
        return view;

    }
}