package com.example.travel_tour_booking_app;

public class PaymentInfoModel {
    private String cardNumber;
    private String name;
    private String phoneNumber;

    public PaymentInfoModel(){}

    public PaymentInfoModel(String cardNumber, String name, String phoneNumber) {
        this.cardNumber = cardNumber;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    // Constructors, getters, setters (có thể tự động tạo bằng Android Studio)

    private String getCardNumber(){
        return cardNumber;
    }

    private String getName(){
        return name;
    }

    private String getPhoneNumber(){
        return phoneNumber;
    }
}
