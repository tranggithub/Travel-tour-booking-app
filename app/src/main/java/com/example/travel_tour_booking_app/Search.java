package com.example.travel_tour_booking_app;

public class Search {
    Integer Icon;
    String Text;

    public Search(Integer icon, String text) {
        Icon = icon;
        Text = text;
    }

    public Integer getIcon() {
        return Icon;
    }

    public String getText() {
        return Text;
    }

    public void setIcon(Integer icon) {
        Icon = icon;
    }

    public void setText(String text) {
        Text = text;
    }
}
