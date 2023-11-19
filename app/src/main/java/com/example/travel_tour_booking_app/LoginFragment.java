package com.example.travel_tour_booking_app;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LoginFragment extends Fragment {
    ArrayList<Seclection> selections;
    SelectionAdapter selectionAdapter;
    VideoView videoView;

    EditText edtMail, edtPassword;
    //FirebaseAuth
    FirebaseAuth mAuth;
    TextView tvQuenMatKhau;
    TextView tvChuaCoTaiKhoan;
    TextView tvDieuKhoan;
    GoogleSignInClient googleSignInClient;

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Nếu đã đăng nhập, thì tiếp tục kiểm tra và chuyển hướng người dùng
            checkAndRedirectUser(currentUser.getUid());
        }
    }

    private void checkAndRedirectUser(String userId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://travel-tour-booking-app-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("users");

        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ReadWriteUserDetails userDetails = snapshot.getValue(ReadWriteUserDetails.class);

                    if ("user".equals(userDetails.getRole()) && userDetails.getDelected() == 0) {
                        startActivityWithFinish(HomeActivity.class);
                    } else if ("admin".equals(userDetails.getRole()) && userDetails.getDelected() == 0) {
                        startActivityWithFinish(AdminPanelActivity.class);
                    } else {
                        Toast.makeText(getActivity(), "Người dùng đã bị xóa", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi ở đây
            }
        });
    }

    private void startActivityWithFinish(Class<?> cls) {
        Intent intent = new Intent(getActivity(), cls);
        startActivity(intent);
        getActivity().finish();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        mAuth = FirebaseAuth.getInstance();

        //Selector
        selections = new ArrayList<>();
        selections.add(Seclection.Facebook);
        selections.add(Seclection.Google);
        selections.add(Seclection.Phone);

        selectionAdapter = new SelectionAdapter(getContext(), R.layout.item_selection, selections);

        GridView SelectionGridview = view.findViewById(R.id.gv_DangNhapKhac);
        SelectionGridview.setAdapter(selectionAdapter);

        videoView = view.findViewById(R.id.vv_Background);
        String videoPath = "android.resource://" + getContext().getPackageName() + "/" + R.raw.ocean;
        Uri uri = Uri.parse(videoPath);

        MediaController mediaController = new MediaController(getContext());
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


        //Handle QuenMatKhau
        tvQuenMatKhau = view.findViewById(R.id.tv_QuenMatKhau);
        tvQuenMatKhau.setText(Html.fromHtml("<u>"+"Quên mật khẩu"+"</u>"+"?"));
        tvQuenMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ResetPasswordActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        //Handle DangKy
        tvChuaCoTaiKhoan = view.findViewById(R.id.tv_ChuaCoTaiKhoan);

        String text = "Bạn chưa có tài khoản? Đăng ký";

        SpannableString spannableString = new SpannableString(text);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(getActivity(), RegisterActivity.class);
                startActivity(intent);
            }
        };

        int startIndex = text.indexOf("Đăng ký");
        int endIndex = startIndex + "Đăng ký".length();

        spannableString.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.WHITE);
        spannableString.setSpan(colorSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvChuaCoTaiKhoan.setText(spannableString);
        tvChuaCoTaiKhoan.setMovementMethod(LinkMovementMethod.getInstance());

        //DieuKhoan
        tvDieuKhoan = view.findViewById(R.id.tv_DieuKhoan);
        tvDieuKhoan.setText(Html.fromHtml("Tiếp tục thao tác nghĩa là tôi đã đọc và đồng ý với "+"<u>"+"Điều khoản & Điều kiện"+"</u>"+" và "+"<u>"+"Cam kết bảo mật"+"</u>"+" của 4Travel"));

        edtMail = view.findViewById(R.id.edt_Email);
        edtPassword = view.findViewById(R.id.edt_Psw);

        //Xử lý DangNhap button
        DangNhap(view);

        //Xử lý SelectionGridView
        SelectionGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item based on the position
                Seclection selectedSeclection = selectionAdapter.getItem(position);

                // Perform an action based on the selected item
                if (selectedSeclection != null) {
                    if (selectedSeclection == Seclection.Facebook) {
                        // Handle the Facebook selection
                        Intent intent = new Intent(getActivity(), FacebookLoginActivity.class);
                        startActivity(intent);
                    }
                    if (selectedSeclection == Seclection.Google){
                        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                .requestIdToken(getString(R.string.default_web_client_id))
                                .requestEmail()
                                .build();
                        googleSignInClient = GoogleSignIn.getClient(getActivity(),gso);
                        Intent intent = googleSignInClient.getSignInIntent();
                        startActivityForResult(intent,RC_SIGN_IN);
                    }
                }
            }
        });
        return view;
    }
    int RC_SIGN_IN = 40;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account =  task.getResult(ApiException.class);
                firebaseGoogleAuth(account.getIdToken());
            } catch (ApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void firebaseGoogleAuth(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            ReadWriteUserDetails userDetails = new ReadWriteUserDetails(user.getDisplayName(),user.getPhotoUrl());
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://travel-tour-booking-app-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("users");
                            String userId = user.getUid(); // Get the user's unique ID
                            databaseReference.child(userId).setValue(userDetails);
                            if (userDetails.getDelected() == 0) {
                                Intent intent = new Intent(getActivity(), HomeActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            }
                            else Toast.makeText(getActivity(), "Tài khoản đã bị xóa", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "Đăng nhập không thành công", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    public void DangNhap(View view) {

        Button button = view.findViewById(R.id.btn_DangNhap);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email, psw;
                email = String.valueOf(edtMail.getText());
                psw = String.valueOf(edtPassword.getText());

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(LoginFragment.this.getContext(), "Nhập email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(psw)) {
                    Toast.makeText(LoginFragment.this.getContext(), "Nhập password", Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.signInWithEmailAndPassword(email, psw)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getActivity(), "Đăng nhập thành công.",
                                            Toast.LENGTH_SHORT).show();
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://travel-tour-booking-app-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("users");
                                    if (user != null) {
                                        databaseReference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()) {
                                                    ReadWriteUserDetails userDetails = snapshot.getValue(ReadWriteUserDetails.class);
                                                    if ("user".equals(userDetails.getRole()) && userDetails.getDelected() == 0) {
                                                        Intent intent = new Intent(getActivity(), HomeActivity.class);
                                                        startActivity(intent);
                                                        getActivity().finish();
                                                    } else if ("admin".equals(userDetails.getRole()) && userDetails.getDelected() == 0) {
                                                        Intent intent = new Intent(getActivity(), AdminPanelActivity.class);
                                                        startActivity(intent);
                                                        getActivity().finish();
                                                    } else {
                                                        Toast.makeText(getActivity(), "Người dùng đã bị xóa", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(LoginFragment.this.getContext(), "Đăng nhập thất bại.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
            }
        });
    }
}