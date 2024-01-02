package com.example.travel_tour_booking_app;

import java.util.Comparator;

public class StarComparator implements Comparator<Place> {
    @Override
    public int compare(Place o1, Place o2) {
        return Double.compare(o2.getView(), o2.getView());
    }
}
