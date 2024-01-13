package com.example.travel_tour_booking_app;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class SuccessResetPasswordActivity extends AppCompatActivity {

    VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_reset_password);

        String userEmail = getIntent().getStringExtra("user_email");

        // Video
        videoView = findViewById(R.id.vv_Background_SuccessResetPsw);
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.ocean;
        Uri uri = Uri.parse(videoPath);

        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);

        videoView.setVideoURI(uri);
        videoView.start();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });

        TextView tvThongBao = findViewById(R.id.tv_ThongBao_SuccessResetPsw);
        tvThongBao.setText("Đường link thay đổi mật khẩu đã gửi vào email "+ userEmail +".");

        TextView tvDieuKhoan = findViewById(R.id.tv_DieuKhoan_successresetpsw);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        String normalText = "Tiếp tục thao tác nghĩa là tôi đã đọc và đồng ý với ";
        String linkText1 = "Điều khoản & Điều kiện";
        String linkText2 = "Cam kết bảo mật";

        spannableStringBuilder.append(normalText);
        int start1 = spannableStringBuilder.length();
        spannableStringBuilder.append(linkText1);
        spannableStringBuilder.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                startActivity(TermsAndConditionActivity.class);
            }
        }, start1, spannableStringBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableStringBuilder.append(" và ");

        int start2 = spannableStringBuilder.length();
        spannableStringBuilder.append(linkText2);
        spannableStringBuilder.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                startActivity(SecurityCommitmentActivity.class);
            }
        }, start2, spannableStringBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.append(" của 4Travel.");

        tvDieuKhoan.setText(spannableStringBuilder);
        tvDieuKhoan.setMovementMethod(LinkMovementMethod.getInstance());

        TextView tvQuayLai = findViewById(R.id.tv_quaylaitrangdangnhap_successresetpsw);
        tvQuayLai.setText(Html.fromHtml("<u>Quay lại trang đăng nhập</u>"));
        tvQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(LoginActivity.class);
                finish();
            }
        });
    }
    private void startActivity(Class<?> cls) {
        Intent intent = new Intent(SuccessResetPasswordActivity.this, cls);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.ocean;
        Uri uri = Uri.parse(videoPath);

        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);

        videoView.setVideoURI(uri);
        videoView.start();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.ocean;
        Uri uri = Uri.parse(videoPath);

        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);

        videoView.setVideoURI(uri);
        videoView.start();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
    }
}