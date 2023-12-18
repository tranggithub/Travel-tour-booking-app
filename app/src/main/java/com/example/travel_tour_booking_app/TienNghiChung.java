package com.example.travel_tour_booking_app;

import java.io.Serializable;

public enum TienNghiChung implements Serializable {
    Wifi(R.drawable.ic_wifi,"Wifi"),
    Bed(R.drawable.ic_two_bed,"Giường"),
    Bathtub(R.drawable.ic_spa,"Bồn tắm"),
    TV(R.drawable.ic_tvshow,"TV"),
    Conditioner(R.drawable.ic_cool,"Máy lạnh"),
    Parking(R.drawable.ic_packing,"Điểm đỗ xe"),
    Exercise(R.drawable.ic_exercise,"Thể dục"),
    Wine(R.drawable.ic_wine,"Quầy rượu");
    int Icon;
    String name;

    TienNghiChung(int icon, String name) {
        Icon = icon;
        this.name = name;
    }

    public int getIcon() {
        return Icon;
    }

    public String getName() {
        return name;
    }

    public void setIcon(int icon) {
        Icon = icon;
    }

    public void setName(String name) {
        this.name = name;
    }
}
