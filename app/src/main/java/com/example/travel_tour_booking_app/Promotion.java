package com.example.travel_tour_booking_app;

import android.os.Build;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class Promotion implements Serializable {
    String Key;
    String Title;
    String Thumbnail;
    String startDateString;
    String endDateString;
    String Text;
    ArrayList<DetailNews> detailPromotionList;
    boolean isActive;

    public Promotion() {
    }

    public Promotion(String title, String thumbnail, String startDateString, String endDateString, String text, ArrayList<DetailNews> detailPromotionList, boolean isActive) {
        Title = title;
        Thumbnail = thumbnail;
        this.startDateString = startDateString;
        this.endDateString = endDateString;
        Text = text;
        this.detailPromotionList = detailPromotionList;
        this.isActive = isActive;
    }

    public void setTitle(String title) {
        Title = title;
    }


    public void setText(String text) {
        Text = text;
    }

    public String getTitle() {
        return Title;
    }

    public String getText() {
        return Text;
    }
    public String getCurrentDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy"); // Định dạng ngày theo "dd-MM-yy"
        String formattedDate = sdf.format(date); // Lấy ngày hiện tại và định dạng nó
        return formattedDate;}

    public ArrayList<DetailNews> getDetailPromotionList() {
        return detailPromotionList;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setDetailPromotionList(ArrayList<DetailNews> detailPromotionList) {
        this.detailPromotionList = detailPromotionList;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setThumbnail(String thumbnail) {
        Thumbnail = thumbnail;
    }

    public void setStartDateString(String startDateString) {
        this.startDateString = startDateString;
    }

    public void setEndDateString(String endDateString) {
        this.endDateString = endDateString;
    }

    public String getThumbnail() {
        return Thumbnail;
    }

    public String getStartDateString() {
        return startDateString;
    }

    public String getEndDateString() {
        return endDateString;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

//    public boolean isExpired()
//    {
//        // Ngày hôm nay
//        LocalDate today = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            today = LocalDate.now();
//        }
//
//        // Chuyển đổi chuỗi thành đối tượng LocalDate
//        LocalDate startDate = parseDateString(startDateString);
//        LocalDate endDate = parseDateString(endDateString);
//
//        // Kiểm tra xem hôm nay có nằm trong khoảng hạn sử dụng không
//        if (isWithinRange(today, startDate, endDate)) {
//            return false;
//        } else {
//            return true;
//        }
//    }
//    private static LocalDate parseDateString(String dateString) {
//        DateTimeFormatter formatter = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            return LocalDate.parse(dateString, formatter);
//        }
//        return null;
//    }
//
//    // Hàm kiểm tra xem một ngày có nằm trong khoảng hay không
//    private static boolean isWithinRange(LocalDate date, LocalDate startDate, LocalDate endDate) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            return !date.isBefore(startDate) && !date.isAfter(endDate);
//        }
//        return false;
//    }
}
