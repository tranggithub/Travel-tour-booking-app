package com.example.travel_tour_booking_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserFragment extends Fragment {
    Button btnSetting;
    TextView tvName, tvEmail, tvSdt;
    FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_user_dark, container, false);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://travel-tour-booking-app-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("users");

        tvName = view.findViewById(R.id.textView6);
        tvEmail = view.findViewById(R.id.textView7);
        tvSdt = view.findViewById(R.id.textView8);

        if (user != null) {
            databaseReference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        ReadWriteUserDetails userDetails = snapshot.getValue(ReadWriteUserDetails.class);
                        String fullName;
                        if (userDetails != null) {
                            if (userDetails.getHo()!=null){
                                fullName = userDetails.getHo() + " " + userDetails.getTen();
                            }
                            else {
                                fullName = userDetails.getTen();
                            }
                            tvName.setText(fullName);
                            if (user.getEmail()!=null)
                                tvEmail.setText("Email: "+user.getEmail());
                            else tvEmail.setText("Email: ");
                            if (user.getPhoneNumber()!=null)
                                tvSdt.setText("Số điện thoại: "+user.getPhoneNumber());
                            else tvSdt.setText("Số điện thoại: ");
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
//                    // Handle any errors that may occur
                    Toast.makeText(getActivity(), "Lỗi khi đọc dữ liệu người dùng.", Toast.LENGTH_SHORT).show();
                }
            });
        }

        //Handle button Setting
        btnSetting = view.findViewById(R.id.button7);
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return view;
    }
}
