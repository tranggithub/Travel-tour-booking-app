package com.example.travel_tour_booking_app;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class PaymentDiscountActivity extends AppCompatActivity {

    private Button btnNext;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_payment_2);

        btnNext = findViewById(R.id.btnNext);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double totalPrice = getIntent().getDoubleExtra("TOTAL_AMOUNT", 0.0);

                // Log để kiểm tra giá trị totalPrice
                Log.d("PaymentDiscountActivity", "Total Price: " + totalPrice);

                // Truyền giá trị totalPrice sang ChoosePaymentActivity
                Intent intent = new Intent(PaymentDiscountActivity.this, ChoosePaymentActivity.class);
                intent.putExtra("TOTAL_AMOUNT", totalPrice);
                startActivity(intent);
            }
        });
    }
}
