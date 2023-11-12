package com.example.travel_tour_booking_app;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    VideoView videoView;
    EditText edtPsw;
    TextView tvItNhat8KyTu;
    TextView tvDangTu;
    TextView tvChuHoaCuThuong, tvDaCoTaiKhoan, tvDieuKhoan;
    EditText edtEmail, edtHo, edtTen, edtNhapLaiPsw;
    Button btnDangKy;
    CheckBox ckbDieuKhoan;
    // Kiểm tra psw
    boolean yeuCauPsw = false;

    //Firebase
    FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();

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

        //Handle DangNhap
        tvDaCoTaiKhoan = (TextView) findViewById(R.id.tv_DaCoTaiKhoan);

        String text = "Bạn đã có tài khoản? Đăng nhập";

        SpannableString spannableString = new SpannableString(text);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        };

        int startIndex = text.indexOf("Đăng nhập");
        int endIndex = startIndex + "Đăng nhập".length();

        spannableString.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.WHITE);
        spannableString.setSpan(colorSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvDaCoTaiKhoan.setText(spannableString);
        tvDaCoTaiKhoan.setText(spannableString);
        tvDaCoTaiKhoan.setMovementMethod(LinkMovementMethod.getInstance());

        tvDieuKhoan = findViewById(R.id.tv_DieuKhoan_register);
        tvDieuKhoan.setText(Html.fromHtml("Tiếp tục thao tác nghĩa là tôi đã đọc và đồng ý với "+"<u>"+"Điều khoản & Điều kiện"+"</u>"+" và "+"<u>"+"Cam kết bảo mật"+"</u>"+" của 4Travel"));

        edtEmail = findViewById(R.id.edt_Email_register);
        btnDangKy = findViewById(R.id.btn_DangKy_register);
        edtHo = findViewById(R.id.edt_Ho_register);
        edtTen = findViewById(R.id.edt_Ten_register);
        edtNhapLaiPsw = findViewById(R.id.edt_NhapLaiPsw_register);
        ckbDieuKhoan = findViewById(R.id.ckb_DongYDieuKhoan_register);

        // Password
        edtPsw = findViewById(R.id.edt_Psw_register);
        tvItNhat8KyTu = findViewById(R.id.tv_ItNhat8KyTu);
        tvDangTu = findViewById(R.id.tv_DangTu);
        tvChuHoaCuThuong = findViewById(R.id.tv_ChuHoaCuThuong);

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
                if (password.matches(".*[a-zA-Z].*") && password.matches(".*[0-9].*") && password.matches(".*[@#\\$%^&+=\\$*].*")) {
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

                if (password.length() >= 8 &&
                        password.matches(".*[a-zA-Z].*") && password.matches(".*[0-9].*") && password.matches(".*[@#\\$%^&+=\\$*].*") &&
                        password.matches(".*[a-z].*") && password.matches(".*[A-Z].*")){
                    yeuCauPsw = true;}
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Do nothing
            }

    });
        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, psw, ho, ten, nhaplaipsw;
                email = String.valueOf(edtEmail.getText());
                psw = String.valueOf(edtPsw.getText());
                ho = String.valueOf(edtHo.getText());
                ten = String.valueOf(edtTen.getText());
                nhaplaipsw = String.valueOf(edtNhapLaiPsw.getText());
                if (TextUtils.isEmpty(ho)){
                    Toast.makeText(RegisterActivity.this, "Nhập họ", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(ten)){
                    Toast.makeText(RegisterActivity.this, "Nhập tên", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(email)){
                    Toast.makeText(RegisterActivity.this, "Nhập email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(psw)){
                    Toast.makeText(RegisterActivity.this, "Nhập password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!yeuCauPsw){
                    Toast.makeText(RegisterActivity.this, "Password chưa hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(nhaplaipsw)){
                    Toast.makeText(RegisterActivity.this, "Nhập lại password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!psw.equals(nhaplaipsw)){
                    Toast.makeText(RegisterActivity.this, "Nhập lại password không chính xác", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!ckbDieuKhoan.isChecked()){
                    Toast.makeText(RegisterActivity.this, "Chưa đồng ý điều khoản", Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.createUserWithEmailAndPassword(email, psw)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    // Enter User Data into Firebase Realtime Database
                                    ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(ho, ten);

                                    // Extracting User reference from Database for "User"
                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://travel-tour-booking-app-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("users");
                                    String userId = user.getUid(); // Get the user's unique ID
                                    databaseReference.child(userId).setValue(writeUserDetails)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(RegisterActivity.this, "Đã lưu thông tin người dùng.",
                                                                Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    } else {
                                                        Toast.makeText(RegisterActivity.this, "Đăng ký thất bại. Hãy thử lại!",
                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                } else {
                                    // Handle authentication errors
                                    try {
                                        throw task.getException();
                                    } catch (FirebaseAuthInvalidUserException e) {
                                        Toast.makeText(RegisterActivity.this, "Email này không tồn tại hoặc đã được sử dụng.",
                                                Toast.LENGTH_SHORT).show();
                                        edtEmail.requestFocus();
                                    } catch (FirebaseAuthUserCollisionException e) {
                                        Toast.makeText(RegisterActivity.this, "Email đã được đăng ký. Hãy dùng email khác.",
                                                Toast.LENGTH_SHORT).show();
                                        edtEmail.requestFocus();
                                    } catch (Exception e) {
                                        Log.e("RegisterActivity", e.getMessage());
                                        Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                    }

                                    // If sign-up fails, display a message to the user.
                                    Toast.makeText(RegisterActivity.this, "Đăng ký thất bại. Hãy thử lại!",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });
    }
}
