package com.example.travel_tour_booking_app;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class DiscoverFragment extends Fragment {
    ArrayList<Country> countryList;
    CountryAdapter countryAdapter;
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

        // Inflate the layout for this fragment
        return view;
    }
}