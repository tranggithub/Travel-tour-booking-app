package com.example.travel_tour_booking_app;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

import java.util.List;

public class ResetPasswordActivity extends AppCompatActivity {

    VideoView videoView;
    EditText edtEmail;
    Button btnResetPassword;
    TextView tvDieuKhoan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        // Video
        videoView = findViewById(R.id.vv_Background_ResetPsw);
        HandleBackground(videoView);

        edtEmail = findViewById(R.id.edt_Email_ResetPsw);
        btnResetPassword = findViewById(R.id.btn_Gui_ResetPsw);

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the email entered by the user
                String email = edtEmail.getText().toString().trim();

                // Check if the email is empty
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(ResetPasswordActivity.this, "Vui lòng nhập địa chỉ email của bạn", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Call the method to send a password reset email
                resetPassword(email);
            }
        });

        //DieuKhoan
        tvDieuKhoan = findViewById(R.id.tv_DieuKhoan_resetpsw);

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
    }

    private void resetPassword(String email) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Email đặt lại mật khẩu đã được gửi thành công
                            Toast.makeText(ResetPasswordActivity.this, "Email đặt lại mật khẩu đã được gửi", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ResetPasswordActivity.this, SuccessResetPasswordActivity.class);
                            intent.putExtra("user_email", email); // Thêm email vào Intent
                            startActivity(intent);
                        } else {
                            // Nếu địa chỉ email không được đăng ký hoặc có lỗi khác xảy ra
                            Toast.makeText(ResetPasswordActivity.this, "Không thể gửi email đặt lại mật khẩu", Toast.LENGTH_SHORT).show();
                            Log.e("ResetPasswordActivity", "Lỗi: " + task.getException());
                        }
                    }
                });
    }


    private void HandleBackground(VideoView videoView){
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.ocean;
        Uri uri = Uri.parse(videoPath);


        videoView.setVideoURI(uri);
        videoView.start();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
    }
    private void startActivity(Class<?> cls) {
        Intent intent = new Intent(ResetPasswordActivity.this, cls);
        startActivity(intent);
    }

}