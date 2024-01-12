package com.example.travel_tour_booking_app;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class PhoneLoginActivity extends AppCompatActivity {
    private EditText edtPhoneNumber, edtMaXacNhan;
    private Button btnGuiMa, btnXacNhan;
    private TextView tvMaXacNhan, tvGuiLaiMa;
    private VideoView videoView;

    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);

        videoView = findViewById(R.id.vv_Background);
        HandleBackground(videoView);

        mAuth = FirebaseAuth.getInstance();

        edtPhoneNumber = findViewById(R.id.edt_PhoneNumber);
        edtMaXacNhan = findViewById(R.id.edt_MaXacNhan);
        btnGuiMa = findViewById(R.id.btn_GuiMa);
        btnXacNhan = findViewById(R.id.btn_XacNhan);
        tvMaXacNhan = findViewById(R.id.tv_MaXacNhan);
        tvGuiLaiMa = findViewById(R.id.tv_GuiLaiMa);

        btnGuiMa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = edtPhoneNumber.getText().toString().trim();
                if (!phoneNumber.isEmpty()) {
                    sendVerificationCode(phoneNumber);
                    showVerificationViews();
                    Toast.makeText(PhoneLoginActivity.this, "Đã gửi mã thành công", Toast.LENGTH_SHORT).show();
                } else {
                    // Handle case where phone number is empty
                    Toast.makeText(PhoneLoginActivity.this, "Gửi mã không thành công. Hãy gửi lại", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = edtMaXacNhan.getText().toString().trim();
                if (!code.isEmpty()) {
                    verifyCode(code);
                } else {
                    Toast.makeText(PhoneLoginActivity.this, "Hãy nhập mã xác thực", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Xử lý callbacks khi xác thực số điện thoại thay đổi trạng thái
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                // Xác thực tự động (nếu có)
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                // Xử lý lỗi xác thực
                // ...
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                // Lưu lại mã xác thực và token để xác nhận lại (nếu cần)
                verificationId = s;
            }
        };
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        HandleBackground(videoView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        HandleBackground(videoView);
    }

    private void HandleBackground(VideoView videoView) {
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

    private void sendVerificationCode(String phoneNumber) {
        // Gửi mã xác thực đến số điện thoại
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks
        );
    }

    private void verifyCode(String code) {
        // Xác nhận mã xác thực
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://travel-tour-booking-app-default-rtdb.asia-southeast1.firebasedatabase.app/")
                                .getReference("users");

                        databaseReference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ReadWriteUserDetails userDetails;
                                if (snapshot.exists()) {
                                    userDetails = snapshot.getValue(ReadWriteUserDetails.class);
                                } else {
                                    userDetails = new ReadWriteUserDetails();
                                    userDetails.setSdt(user.getPhoneNumber());
                                    pushUserDetailsToDatabase(userDetails, user.getUid());
                                }
                                if ("user".equals(userDetails.getRole()) && userDetails.getDelected() == 0) {
                                    startActivityWithFinish(HomeActivity.class);
                                } else if ("admin".equals(userDetails.getRole()) && userDetails.getDelected() == 0) {
                                    startActivityWithFinish(AdminPanelActivity.class);
                                } else {
                                    Toast.makeText(PhoneLoginActivity.this, "Người dùng đã bị xóa", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Xử lý lỗi ở đây
                            }
                        });
                        Toast.makeText(PhoneLoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(PhoneLoginActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void startActivityWithFinish(Class<?> cls) {
        Intent intent = new Intent(PhoneLoginActivity.this, cls);
        startActivity(intent);
        finish();
    }

    private void pushUserDetailsToDatabase(ReadWriteUserDetails userDetails, String userId) {
        // Đường dẫn đến "users" trong Firebase Realtime Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://travel-tour-booking-app-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("users");

        // Đẩy userDetails lên Firebase Realtime Database dưới dạng một child của user có id là userId
        databaseReference.child(userId).setValue(userDetails)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(PhoneLoginActivity.this, "Dữ liệu người dùng đã được lưu trữ", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(PhoneLoginActivity.this, "Lỗi khi lưu trữ dữ liệu người dùng", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showVerificationViews() {
        // Hiển thị các view liên quan đến việc xác nhận mã
        edtPhoneNumber.setEnabled(false);
        tvMaXacNhan.setVisibility(View.VISIBLE);
        edtMaXacNhan.setVisibility(View.VISIBLE);
        btnGuiMa.setVisibility(View.INVISIBLE);
        btnXacNhan.setVisibility(View.VISIBLE);
        tvGuiLaiMa.setVisibility(View.VISIBLE);
    }
}