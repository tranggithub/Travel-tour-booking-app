package com.example.travel_tour_booking_app;

import java.io.Serializable;

public enum TienIch implements Serializable {
    Plan(R.drawable.ic_airplane_take_off,"Máy bay thân rộng"),
    Tivi(R.drawable.ic_tvshow,"Có tivi"),
    Food(R.drawable.ic_food_bar,"Cung cấp các bữa ăn"),
    Electric(R.drawable.ic_plug,"Cung cấp ổ cắm điện"),
    Conditioner(R.drawable.ic_car_fresh_air,"Có máy lạnh"),
    Sitting(R.drawable.ic_packing,"Chỗ ngồi thoải mái");
    int Icon;
    String name;

    TienIch(int icon, String name) {
        Icon = icon;
        this.name = name;
    }

    TienIch() {
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
