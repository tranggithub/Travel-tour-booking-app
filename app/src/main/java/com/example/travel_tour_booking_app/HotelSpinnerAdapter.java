package com.example.travel_tour_booking_app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class HotelSpinnerAdapter extends ArrayAdapter<Hotel> {
    private Activity context;
    public HotelSpinnerAdapter(Activity context, int LayoutID, List<Hotel>
            objects) {
        super(context,LayoutID, objects);
        this.context = context;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        if (convertView == null) {
            convertView =
                    LayoutInflater.from(context).inflate(R.layout.item_spinner, null,
                            false);
        }
        // Get item
        Hotel hotel = getItem(position);
        // Get view
        TextView tvHotelName = (TextView)
                convertView.findViewById(R.id.spn_name);

        if (hotel.getName()!=null) {
            tvHotelName.setText(hotel.getName().toString());
        }

        return convertView;
    }
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView =
                    LayoutInflater.from(context).inflate(R.layout.item_drop_down_spinner, parent,
                            false);
        }
        // Get item
        Hotel hotel = getItem(position);
        // Get view
        TextView tvHotelName = (TextView)
                convertView.findViewById(R.id.spn_name);

        if (hotel.getName()!=null) {
            tvHotelName.setText(hotel.getName().toString());
        }

        return convertView;
    }
}