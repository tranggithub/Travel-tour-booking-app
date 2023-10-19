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
import android.widget.Button;
import android.widget.GridView;
import android.widget.ScrollView;
import android.widget.TextView;

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
        News tempNews = new News(
                "Khu vui chơi ở Huế",
                new Date(2023,4,14),
                "Nhiều thực khách tỏ ra thích thú với bố trí của công viên");
        newss.add(tempNews);

        newsAdapter = new NewsAdapter(getContext(),newss);

        RecyclerView rvNews = view.findViewById(R.id.rv_new);
        rvNews.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvNews.setAdapter(newsAdapter);

        ScrollToTop(view);
        // Inflate the layout for this fragment
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
}