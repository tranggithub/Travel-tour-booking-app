package com.example.travel_tour_booking_app;
public class BankCard {
    private String cardId;
    private String cardNumber;
    private String cardName;
    private String expiryDate;

    // Constructor mặc định không có đối số
    public BankCard() {}

    // Constructor với đối số
    public BankCard(String cardId, String cardNumber, String cardName, String expiryDate) {
        this.cardId = cardId;
        this.cardNumber = cardNumber;
        this.cardName = cardName;
        this.expiryDate = expiryDate;
    }

    // Các phương thức getter và setter

    public String getCardId() {
        return cardId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getCardName() {
        return cardName;
    }

    public String getExpiryDate() {
        return expiryDate;
    }
}
