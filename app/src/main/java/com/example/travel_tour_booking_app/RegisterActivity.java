package com.example.travel_tour_booking_app;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Video
        videoView = findViewById(R.id.vv_Background_register);
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

        // Password
        EditText edtPsw = findViewById(R.id.edt_Psw_register);
        TextView tvItNhat8KyTu = findViewById(R.id.tv_ItNhat8KyTu);
        TextView tvDangTu = findViewById(R.id.tv_DangTu);
        TextView tvChuHoaCuThuong = findViewById(R.id.tv_ChuHoaCuThuong);

        edtPsw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = s.toString();

                // Check if password has at least 8 characters
                if (password.length() >= 8) {
                    tvItNhat8KyTu.setTextColor(0xFF37E0EE); // Màu xanh
                } else {
                    tvItNhat8KyTu.setTextColor(0xFF779FA1); // Màu xám
                }

                // Check if password contains at least one letter, one digit, and one special character
                if (password.matches(".*[a-zA-Z].*") && password.matches(".*[0-9].*") && password.matches(".*[@#\\$%^&+=].*")) {
                    tvDangTu.setTextColor(0xFF37E0EE); // Màu xanh
                } else {
                    tvDangTu.setTextColor(0xFF779FA1); // Màu xám
                }

                // Check if password contains both uppercase and lowercase letters
                if (password.matches(".*[a-z].*") && password.matches(".*[A-Z].*")) {
                    tvChuHoaCuThuong.setTextColor(0xFF37E0EE); // Màu xanh
                } else {
                    tvChuHoaCuThuong.setTextColor(0xFF779FA1); // Màu xám
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Do nothing
            }
        });
    }
}
