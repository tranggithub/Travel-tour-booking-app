package com.example.travel_tour_booking_app;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helper {
    public static boolean isValidDate(String inputDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setLenient(false);

        try {
            // Parsing chuỗi ngày
            Date parsedDate = dateFormat.parse(inputDate);
            return true;
        } catch (ParseException e) {
            // Ngày không đúng định dạng
            return false;
        }
    }

    public static boolean hasEmptyElement(List<String> stringList) {
        for (String str : stringList) {
            if (str.isEmpty()) {
                return true;
            }
        }
        return false;
    }
    public static boolean isNaturalNumber(String str) {
        try {
            int number = Integer.parseInt(str);
            return number > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public static boolean isValidCurrencyFormat(String str) {
        // Biểu thức chính quy cho định dạng tiền tệ: "100.000.000"
        String currencyRegex = "^[1-9]\\d{0,2}(\\.\\d{3})*(\\.\\d+)?$";

        Pattern pattern = Pattern.compile(currencyRegex);
        Matcher matcher = pattern.matcher(str);

        return matcher.matches();
    }
    public static boolean isValidTimeFormat(String str) {
        // Biểu thức chính quy cho định dạng giờ: "HH:mm"
        String timeRegex = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$";

        Pattern pattern = Pattern.compile(timeRegex);
        Matcher matcher = pattern.matcher(str);

        return matcher.matches();
    }

}
