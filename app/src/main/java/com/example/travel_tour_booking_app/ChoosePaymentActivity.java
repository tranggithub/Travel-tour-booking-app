package com.example.travel_tour_booking_app;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class ChoosePaymentActivity extends AppCompatActivity {

    private Button btnNext;
    private double totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_payment);

        ImageButton buttonZaloPay = findViewById(R.id.paymentZalopay);

        buttonZaloPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openZaloPay();
            }
        });

        btnNext = findViewById(R.id.btnNext);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lấy giá trị price từ Intent
                Intent intent = new Intent(ChoosePaymentActivity.this, ChoosePaymentActivity.class);
                intent.putExtra("TOTAL_AMOUNT", totalPrice);
                startActivity(intent);
            }
        });

        // Lấy giá trị price từ Intent khi Activity được khởi tạo
        totalPrice = getIntent().getDoubleExtra("TOTAL_AMOUNT", 0.0);
    }

    private void openZaloPay() {
        if (isZaloPayInstalled()) {
            String zaloPayScheme = "zlp://app";
            Uri uri = Uri.parse(zaloPayScheme);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                // Xử lý khi không thể mở ứng dụng ZaloPay
                showErrorMessage("Ứng dụng ZaloPay không thể mở. Vui lòng kiểm tra và thử lại sau.");
            }
        } else {
            // Xử lý khi ứng dụng ZaloPay chưa được cài đặt
            showErrorMessage("Ứng dụng ZaloPay chưa được cài đặt. Vui lòng tải và cài đặt ứng dụng trước khi tiếp tục thanh toán.");
        }
    }

    private void showErrorMessage(String message) {
        // Hiển thị thông báo lỗi cho người dùng, ví dụ sử dụng Toast
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private boolean isZaloPayInstalled() {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo("vn.com.zalopay", PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
