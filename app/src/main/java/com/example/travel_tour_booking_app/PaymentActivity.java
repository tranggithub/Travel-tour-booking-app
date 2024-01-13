package com.example.travel_tour_booking_app;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PaymentActivity extends AppCompatActivity {

    private int count1 = 0, count2 = 0;
    private TextView tvCountadult, tvCountchild, tvPrice;

    private Button btnNext;


    private final double ADULT_PRICE = 1000000; // Giá tiền cho mỗi người lớn
    private final double CHILD_PRICE = 500000;  // Giá tiền cho mỗi trẻ em


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_payment);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button mainPassengerInfoButton = findViewById(R.id.main_passenger_infor);

        tvCountadult = findViewById(R.id.tvCountadult);
        Button plusadult = findViewById(R.id.plusadult);
        Button minusadult = findViewById(R.id.minusadult);

        plusadult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count1++;
                updateCount();
                updatePrice();
            }
        });

        minusadult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (count1 > 0) {
                    count1--;
                    updateCount();
                    updatePrice();
                }
            }
        });

        tvCountchild = findViewById(R.id.tvCountchild);
        Button pluschild = findViewById(R.id.pluschild);
        Button minuschild = findViewById(R.id.minuschild);

        pluschild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count2++;
                updateCount();
                updatePrice();
            }
        });

        minuschild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (count2 > 0) {
                    count2--;
                    updateCount();
                    updatePrice();
                }
            }
        });

        tvPrice = findViewById(R.id.price);
        btnNext = findViewById(R.id.btnNext);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                double totalAmount = (count1 * ADULT_PRICE) + (count2 * CHILD_PRICE);
                Intent intent = new Intent(PaymentActivity.this, PaymentDiscountActivity.class);
                intent.putExtra("TOTAL_AMOUNT", totalAmount);
                startActivity(intent);
            }
        });


        mainPassengerInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){
                // Tạo Intent để chuyển sang trang mới
                Intent intent = new Intent(PaymentActivity.this, PaymentMainInfor.class);
                startActivity(intent);
            }
        });
    }
    private void updateCount() {
        tvCountadult.setText(String.valueOf(count1));
        tvCountchild.setText(String.valueOf(count2));
    }

    private void updatePrice() {
        tvCountadult.setText(String.valueOf(count1));
        tvCountchild.setText(String.valueOf(count2));

        // Tính giá tiền dựa trên số lượng người lớn và trẻ em
        double totalAmount = (count1 * ADULT_PRICE) + (count2 * CHILD_PRICE);

        // Hiển thị giá tiền
        tvPrice.setText(String.format("%,.0f VND", totalAmount));
    }
}
