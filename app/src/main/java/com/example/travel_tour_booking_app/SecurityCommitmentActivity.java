package com.example.travel_tour_booking_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

public class SecurityCommitmentActivity extends AppCompatActivity {
    ImageView ivBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_commitment);

        TextView textViewThu1 = findViewById(R.id.tv_thu1_p1);
        TextView textViewThu2 = findViewById(R.id.tv_thu2_p1);

        String htmlTextThu1 = "Khi được sự đồng ý của người dùng, chúng tôi có thể thu thập dữ liệu cá nhân của người dùng để cung cấp dịch vụ của chúng tôi. Những dữ liệu mà chúng tôi có thể thu thập:<ul><li>&nbsp;&nbsp;&nbsp;Thông tin dùng để đăng nhập tài khoản</li><li>&nbsp;&nbsp;&nbsp;Thông tin cá nhân cụ thể (họ, tên, số điện thoại, thẻ ngân hàng, vị trí địa lý...)</li></ul>";

        String htmlTextThu2 = "Khi được sự đồng ý của người dùng, chúng tôi có thể thu thập dữ liệu cá nhân của người dùng để cung cấp dịch vụ của chúng tôi cho người dùng. Những dữ liệu mà chúng tôi có thể thu thập:<ul><li>&nbsp;&nbsp;&nbsp;Thông tin dùng để đăng nhập tài khoản</li><li>&nbsp;&nbsp;&nbsp;Thông tin cá nhân cụ thể (họ, tên, số điện thoại, thẻ ngân hàng, vị trí địa lý...)</li></ul>";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            // Sử dụng Html.fromHtml cho phiên bản Android N trở lên
            textViewThu1.setText(Html.fromHtml(htmlTextThu1, Html.FROM_HTML_MODE_COMPACT));
            textViewThu2.setText(Html.fromHtml(htmlTextThu2, Html.FROM_HTML_MODE_COMPACT));
        } else {
            textViewThu1.setText(Html.fromHtml(htmlTextThu1));
            textViewThu2.setText(Html.fromHtml(htmlTextThu2));
        }

        ScrollToTop();

        //Quay ve
        ivBack = findViewById(R.id.iv_returnbutton_bm);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void ScrollToTop()
    {
        Button button = findViewById(R.id.btn_up_bm);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScrollView scrollView = findViewById(R.id.sv_bm);
                scrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        });

    }
}