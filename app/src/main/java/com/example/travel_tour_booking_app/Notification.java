package com.example.travel_tour_booking_app;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class Notification implements Serializable {
    String Title;
    String UploadDate;
    String Text;
    String Thumbnail;
    String Key;
    boolean isActive;
    ArrayList<DetailNews> detailNotificationArrayList = new ArrayList<>();

    public Notification()
    {

    }
    public Notification(String title, String uploadDate, String text, String thumbnail, String key, boolean isActive, ArrayList<DetailNews> detailNewsArrayList) {
        Title = title;
        UploadDate = uploadDate;
        Text = text;
        Thumbnail = thumbnail;
        Key = key;
        this.isActive = isActive;
        this.detailNotificationArrayList = detailNotificationArrayList;
    }


    public void setTitle(String title) {
        Title = title;
    }

    public void setUploadDate(String uploadDate) {
        UploadDate = uploadDate;
    }

    public void setText(String text) {
        Text = text;
    }

    public String getTitle() {
        return Title;
    }

    public String getUploadDate() {
        return UploadDate;
    }

    public String getText() {
        return Text;
    }
    public static String getCurrentDate() {
        // Lấy thời gian hiện tại
        Calendar calendar = Calendar.getInstance();
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Ho_Chi_Minh"); // Đặt múi giờ cho GMT+7

        // Định dạng ngày giờ
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd/MM/yyyy, HH:mm (z)", new Locale("vi", "VN"));
        sdf.setTimeZone(timeZone);

        String formattedDate = sdf.format(calendar.getTime());
        return formattedDate;}

    public String getThumbnail() {
        return Thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        Thumbnail = thumbnail;
    }

    public ArrayList<DetailNews> getDetailNotificationArrayList() {
        return detailNotificationArrayList;
    }

    public void setDetailNotificationArrayList(ArrayList<DetailNews> detailNotificationArrayList) {
        this.detailNotificationArrayList = detailNotificationArrayList;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getKey() {
        return Key;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
