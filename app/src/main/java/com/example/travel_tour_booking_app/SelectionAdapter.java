package com.example.travel_tour_booking_app;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class SelectionAdapter extends ArrayAdapter<Seclection> {
    private Context context;

    public SelectionAdapter(Context context, int layoutID, List<Seclection> objects) {
        super(context, layoutID, objects);
        this.context = context;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView =
                    LayoutInflater.from(context).inflate(R.layout.item_selection, null,
                            false);
        }
        // Get item
        Seclection seclection = getItem(position);
        // Get view
        TextView tvSelection = (TextView)
                convertView.findViewById(R.id.tv_selection);
        ImageView ivSelection = (ImageView)
                convertView.findViewById(R.id.iv_selection);
        LinearLayout llSelection = (LinearLayout)
                convertView.findViewById(R.id.ll_selection);
        // Set Name
        if (seclection.getName()!=null) {
            tvSelection.setText(seclection.getName());
        }
        else tvSelection.setText("");
        //Set Image
        if (seclection.getImage() >-1){
            ivSelection.setImageResource(seclection.getImage());
        }
        return convertView;
    }
}
