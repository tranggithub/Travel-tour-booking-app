package com.example.travel_tour_booking_app;

public class ConvertNumber {
    public static int extractNumberFromString(String inputString) {
        // Loại bỏ tất cả các ký tự không phải số từ chuỗi
        String numericString = inputString.replaceAll("[^0-9]", "");

        // Chuyển đổi chuỗi số thành số nguyên
        try {
            return Integer.parseInt(numericString);
        } catch (NumberFormatException e) {
            // Xử lý trường hợp không thể chuyển đổi
            e.printStackTrace();
            return -1; // Hoặc giá trị mặc định khác tùy thuộc vào yêu cầu của bạn
        }
    }
}
