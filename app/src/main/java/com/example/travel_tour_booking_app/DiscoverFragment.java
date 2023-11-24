package com.example.travel_tour_booking_app;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class DiscoverFragment extends Fragment {
    ArrayList<Country> countryList;
    ArrayList<Country> countryNoiDia;
    ArrayList<Country> countryQuocTe;
    CountryAdapter countryAdapter;
    ArrayList<Pagination> paginationArrayList;
    PaginationAdapter paginationAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover, container, false);

        //List country
        //Quốc tế
        countryQuocTe = new ArrayList<>();
        countryQuocTe.add(Country.Dubai);
        countryQuocTe.add(Country.Canada);
        countryQuocTe.add(Country.Iceland);
        countryQuocTe.add(Country.NhatBan);
        countryQuocTe.add(Country.Phap);
        countryQuocTe.add(Country.TayBanNha);
        countryQuocTe.add(Country.ThaiLan);
        countryQuocTe.add(Country.TayBanNha);
        countryQuocTe.add(Country.ThoNhiKy);

        //Nội địa
        countryNoiDia = new ArrayList<>();

        countryNoiDia.add(Country.DaLat);
        countryNoiDia.add(Country.DaNang);
        countryNoiDia.add(Country.CanTho);
        countryNoiDia.add(Country.NhaTrang);
        countryNoiDia.add(Country.HaiPhong);
        countryNoiDia.add(Country.HaNoi);
        countryNoiDia.add(Country.HoChiMinh);
        countryNoiDia.add(Country.PhuQuoc);
        countryNoiDia.add(Country.Vinh);

        countryAdapter = new CountryAdapter(getActivity(),countryNoiDia);

        RecyclerView recyclerViewCountry = view.findViewById(R.id.rv_places_discover);
        recyclerViewCountry.setAdapter(countryAdapter);


        Button btnNoiDia = view.findViewById(R.id.btn_NoiDia);
        Button btnQuocTe = view.findViewById(R.id.btn_QuocTe);

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

                countryAdapter.setCountries(countryNoiDia);
                countryAdapter.notifyDataSetChanged();


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

                countryAdapter.setCountries(countryQuocTe);
                countryAdapter.notifyDataSetChanged();
            }
        });
    }
}