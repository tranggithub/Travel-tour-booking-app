package com.example.travel_tour_booking_app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.travel_tour_booking_app.databinding.ActivityHomeBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;


public class HomeFragment extends Fragment {
    EditText et_home_search;
    ArrayList<Seclection> seclections;
    SelectionAdapter selectionAdapter;
    ArrayList<Place> places, placesPopular;
    PlaceAdapter placePopularAdapter;
    PlaceAdapter placeAdapter;
    ArrayList<Promotion> promotions;
    PromotionAdapter promotionAdapter;
    ArrayList<News> newss;
    NewsAdapter newsAdapter;
    DatabaseReference databaseReferenceNews;
    DatabaseReference databaseReferencePromotions;
    ActivityHomeBinding binding;
    String DatabaseUrl = "https://travel-tour-booking-app-default-rtdb.asia-southeast1.firebasedatabase.app";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initID(view);

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

        //Popular Place
        placesPopular = new ArrayList<>();

        placePopularAdapter = new PlaceAdapter(getContext(),placesPopular,R.layout.item_popular_place,5);

        ViewPager2 vpPopular_Place = view.findViewById(R.id.vp_popularplace);
        vpPopular_Place.setAdapter(placePopularAdapter);

        //Place
        places = new ArrayList<>();

        placeAdapter = new PlaceAdapter(getContext(),places,R.layout.item_place,2);

        RecyclerView rvPlace = view.findViewById(R.id.rv_place);
        rvPlace.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvPlace.setAdapter(placeAdapter);


        //Firebase
        databaseReferenceNews = FirebaseDatabase.getInstance(DatabaseUrl).getReference("Android Tours");

        databaseReferenceNews.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                placesPopular.clear();
                places.clear();
                for (DataSnapshot itemSnapshot: snapshot.getChildren()){
                    Place tempPlace = itemSnapshot.getValue(Place.class);
                    tempPlace.setKey(itemSnapshot.getKey());
                    if (tempPlace.isActive()){
                        places.add(tempPlace);
                        placesPopular.add(tempPlace);
                    }

                }
                placePopularAdapter.sortByView();
                placeAdapter.sortByNews();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        //Promotion
        promotions = new ArrayList<>();
        promotionAdapter = new PromotionAdapter(getContext(),promotions,2);

        RecyclerView rvPromotion = view.findViewById(R.id.rv_promotion);
        rvPromotion.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvPromotion.setAdapter(promotionAdapter);

        //Firebase
        databaseReferencePromotions = FirebaseDatabase.getInstance(DatabaseUrl).getReference("Android Promotion");

        databaseReferencePromotions.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                promotions.clear();
                for (DataSnapshot itemSnapshot: snapshot.getChildren()){
                    Promotion tempPromotion = itemSnapshot.getValue(Promotion.class);
                    tempPromotion.setKey(itemSnapshot.getKey());
                    if (tempPromotion.isActive())
                        promotions.add(tempPromotion);
                }
                promotionAdapter.sortByNews();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //News
        newss = new ArrayList<>();

        newsAdapter = new NewsAdapter(getContext(),newss,2);

        RecyclerView rvNews = view.findViewById(R.id.rv_new);
        rvNews.setLayoutManager(new LinearLayoutManager(getContext()));
        rvNews.setAdapter(newsAdapter);


        //Firebase
        databaseReferenceNews = FirebaseDatabase.getInstance(DatabaseUrl).getReference("Android News");

        databaseReferenceNews.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                newss.clear();
                for (DataSnapshot itemSnapshot: snapshot.getChildren()){
                    News tempNews = itemSnapshot.getValue(News.class);
                    tempNews.setKey(itemSnapshot.getKey());
                    if (tempNews.isActive())
                        newss.add(tempNews);
                }
                newsAdapter.sortByNews();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //Xu ly Ho tro
        SelectionGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Seclection selectedSeclection = selectionAdapter.getItem(position);
                if (selectedSeclection != null) {
                    if (selectedSeclection == Seclection.Question) {
                        Intent intent = new Intent(getActivity(), ChatActivity.class);
                        startActivity(intent);
                    }
                    if (selectedSeclection == Seclection.Place) {
                        Intent intent = new Intent(getActivity(), ListTourActivity.class);
                        intent.putExtra("Place", "");
                        intent.putExtra("Appointment", (String) null);
                        intent.putExtra("Date", "");
                        intent.putExtra("Price","Không giới hạn");
                        startActivity(intent);
                    }
                    if (selectedSeclection == Seclection.News) {
                        Intent intent = new Intent(getActivity(), ListNewsActivity.class);
                        startActivity(intent);
                    }
                    if (selectedSeclection == Seclection.Promotion) {
                        Intent intent = new Intent(getActivity(), ListPromotionActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });

        //Xử lý kéo về đầu trang
        ScrollToTop(view);
        // Inflate the layout for this fragment

        //Xu ly Xem tat ca
        XemTatCaTinTuc(view);
        XemTatCaKhuyenMai(view);
        XemTatCaDiaDiem(view);

        ChangeToSearch();

        return view;

    }
    private void initID(View view){
        et_home_search = view.findViewById(R.id.et_home_search);
    }
    private void ChangeToSearch(){
        et_home_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),SearchActivity.class);
                startActivity(intent);
            }
        });
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
    void XemTatCaKhuyenMai(View view)
    {
        Button button = view.findViewById(R.id.btn_readall_promotions);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),ListPromotionActivity.class);
                startActivity(intent);
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
    public void XemTatCaDiaDiem(View view)
    {
        Button button = view.findViewById(R.id.btn_readall_place);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ShareDialog shareDialog = new ShareDialog(getActivity());
//                shareDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                shareDialog.show();
                ((HomeActivity) requireActivity()).setBottomNavigationSelectedItem(R.id.btn_discover);
            }
        });

    }

}