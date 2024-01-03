package com.example.travel_tour_booking_app;

import java.util.Comparator;

public class ViewComparator implements Comparator<Place> {
    @Override
    public int compare(Place o1, Place o2) {
        return Integer.compare(o2.getView(), o1.getView());
    }
}
