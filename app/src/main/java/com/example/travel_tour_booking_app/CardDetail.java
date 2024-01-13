package com.example.travel_tour_booking_app;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

public class CardDetail extends AppCompatActivity {
    private TextView textViewCardNumber, textViewCardHolder, textViewExpiryDate;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_detail);

        textViewCardNumber = findViewById(R.id.textViewCardNumber);
        textViewCardHolder = findViewById(R.id.textViewCardName);
        textViewExpiryDate = findViewById(R.id.textViewExpiryDate);

        // Nhận dữ liệu từ Intent
        BankCard selectedCard = getIntent().getParcelableExtra("selectedCard");
        if (selectedCard != null) {
            // Hiển thị thông tin thẻ ngân hàng
            textViewCardNumber.setText("Card Number: " + selectedCard.getCardNumber());
            textViewCardHolder.setText("Card Holder: " + selectedCard.getCardName());
            textViewExpiryDate.setText("Expiry Date: " + selectedCard.getExpiryDate());
        }
    }
}
