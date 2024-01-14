package com.example.travel_tour_booking_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

public class FacebookLoginActivity extends LoginActivity {
    private CallbackManager callbackManager;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Add this in your Application class or the first activity that runs
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this.getApplication());

        // Request required permissions
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));

        mAuth= FirebaseAuth.getInstance();

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        handleFacebookLoginSuccess();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });
    }

    private void handleFacebookLoginSuccess() {
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
                    userDetails.setTen(user.getDisplayName());
                    userDetails.setUrlImage(user.getPhotoUrl().toString());
                    pushUserDetailsToDatabase(userDetails, user.getUid());
                }
                if ("user".equals(userDetails.getRole()) && userDetails.getDelected() == 0) {
                    startActivityWithFinish(HomeActivity.class);
                } else if ("admin".equals(userDetails.getRole()) && userDetails.getDelected() == 0) {
                    startActivityWithFinish(AdminPanelActivity.class);
                } else {
                    Toast.makeText(FacebookLoginActivity.this, "Người dùng đã bị xóa", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi ở đây
            }
        });
        Toast.makeText(FacebookLoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
    }

    private void startActivityWithFinish(Class<?> cls) {
        Intent intent = new Intent(FacebookLoginActivity.this, cls);
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
                        Toast.makeText(FacebookLoginActivity.this, "Dữ liệu người dùng đã được lưu trữ", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(FacebookLoginActivity.this, "Lỗi khi lưu trữ dữ liệu người dùng", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
    // Example method to check if the user is logged in
    private boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null && !accessToken.isExpired();
    }

    // Example method to log out
    private void logout() {
        LoginManager.getInstance().logOut();
    }

}