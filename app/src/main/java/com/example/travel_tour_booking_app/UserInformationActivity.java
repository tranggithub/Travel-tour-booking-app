package com.example.travel_tour_booking_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class UserInformationActivity extends AppCompatActivity {
    Button btnConfirm;
    EditText edtFirstName, edtName, edtEmail, edtSdt;
    ImageView ivAvatar;
    FirebaseAuth mAuth;
    ImageView ivBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);

        btnConfirm = findViewById(R.id.btn_XacNhan_userinformation);
        edtFirstName = findViewById(R.id.edt_Ho_userinformation);
        edtName = findViewById(R.id.edt_Ten_userinformation);
        edtEmail = findViewById(R.id.edt_Email_userinformation);
        edtSdt = findViewById(R.id.edt_SoDienThoai_userinformation);
        ivAvatar = findViewById(R.id.iv_avatar_userinformation);
        ivBack = findViewById(R.id.iv_returnbutton_userinformation);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://travel-tour-booking-app-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("users");

        if (user != null) {
            databaseReference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        ReadWriteUserDetails userDetails = snapshot.getValue(ReadWriteUserDetails.class);
                        if (userDetails != null) {
                            if (userDetails.getHo()!=null){
                                edtFirstName.setText(userDetails.getHo());
                                edtName.setText(userDetails.getTen());
                            }
                            else {
                                edtName.setText(userDetails.getTen());
                            }
                            if (user.getEmail()!=null)
                                edtEmail.setText(user.getEmail());
                            else edtEmail.setText("");
                            if (user.getPhoneNumber()!=null)
                                edtSdt.setText(user.getPhoneNumber());
                            else edtSdt.setText("");
                            Picasso.get().load(userDetails.getUrlImage()).into(ivAvatar);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
//                    // Handle any errors that may occur
                    Toast.makeText(UserInformationActivity.this, "Lỗi khi đọc dữ liệu người dùng.", Toast.LENGTH_SHORT).show();
                }
            });
        }

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserInformationActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}