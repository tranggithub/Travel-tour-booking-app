package com.example.travel_tour_booking_app;

import java.util.Comparator;

public class DurationComparator implements Comparator<Place> {

    @Override
    public int compare(Place o1, Place o2) {
        return Integer.compare(ConvertNumber.extractNumberFromString(o1.getDuration()), ConvertNumber.extractNumberFromString(o2.getDuration()));
    }

}
