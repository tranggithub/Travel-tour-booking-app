package com.example.travel_tour_booking_app;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class DiscoverFragment extends Fragment {
    ArrayList<Country> countryList;
    CountryAdapter countryAdapter;
    ArrayList<Pagination> paginationArrayList;
    PaginationAdapter paginationAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover, container, false);

        //List country
        Country tempCountry = new Country(R.drawable.img_dubai, "Dubai");

        countryList = new ArrayList<>();
        countryList.add(tempCountry);
        countryList.add(tempCountry);
        countryList.add(tempCountry);
        countryList.add(tempCountry);
        countryList.add(tempCountry);
        countryList.add(tempCountry);
        countryList.add(tempCountry);
        countryList.add(tempCountry);
        countryList.add(tempCountry);

        countryAdapter = new CountryAdapter(getContext(),countryList);

        RecyclerView recyclerViewCountry = view.findViewById(R.id.rv_places_discover);
        recyclerViewCountry.setAdapter(countryAdapter);

        //Pagination
        Pagination tempPagination = new Pagination("1");

        paginationArrayList = new ArrayList<>();
        paginationArrayList.add(tempPagination);
        paginationArrayList.add(tempPagination);
        paginationArrayList.add(tempPagination);
        paginationArrayList.add(tempPagination);
        paginationArrayList.add(tempPagination);
        paginationArrayList.add(tempPagination);

        paginationAdapter = new PaginationAdapter(getContext(),paginationArrayList);

        RecyclerView recyclerViewPagination = view.findViewById(R.id.rv_pigination_discover);
        recyclerViewPagination.setAdapter(paginationAdapter);
        // Inflate the layout for this fragment

        ClickOnNoiDia(view);
        ClickOnQuocTe(view);
        return view;
    }

    private void ClickOnNoiDia (View view)
    {
        Button btnNoiDia = view.findViewById(R.id.btn_NoiDia);
        Button btnQuocTe = view.findViewById(R.id.btn_QuocTe);
        btnNoiDia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnNoiDia.setTextColor(getResources().getColor(R.color.maincolor));
                btnQuocTe.setTextColor(getResources().getColor(R.color.white));
            }
        });
    }
    private void ClickOnQuocTe (View view)
    {
        Button btnNoiDia = view.findViewById(R.id.btn_NoiDia);
        Button btnQuocTe = view.findViewById(R.id.btn_QuocTe);
        btnQuocTe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnQuocTe.setTextColor(getResources().getColor(R.color.maincolor));
                btnNoiDia.setTextColor(getResources().getColor(R.color.white));
            }
        });
    }
}