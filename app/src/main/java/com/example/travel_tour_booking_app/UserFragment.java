package com.example.travel_tour_booking_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

public class UserFragment extends Fragment {
    TextView btnSetting, btnChangeInfor, btnNotification;
    ImageView iconNotification;
    TextView tvName, tvEmail, tvSdt, tvHistory;
    ImageView ivAvatar;
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
        ivAvatar = view.findViewById(R.id.imageView2);
        btnChangeInfor = view.findViewById(R.id.button);
        tvHistory = view.findViewById(R.id.button3);

        if (user != null) {
            databaseReference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        ReadWriteUserDetails userDetails = snapshot.getValue(ReadWriteUserDetails.class);
                        String fullName;
                        if (userDetails != null) {
                            if (userDetails.getHo() != null) {
                                fullName = userDetails.getHo() + " " + userDetails.getTen();
                            } else {
                                fullName = userDetails.getTen();
                            }
                            tvName.setText(fullName);
                            if (user.getEmail() != null)
                                tvEmail.setText("Email: " + user.getEmail());
                            else tvEmail.setText("Email: ");
                            if (userDetails.getSdt() != null) {
                                tvSdt.setText("Số điện thoại: " + userDetails.getSdt());
                            } else {
                                tvSdt.setText("Số điện thoại: ");
                            }
                            Picasso.get().load(userDetails.getUrlImage()).into(ivAvatar);
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

        //Handle button Notification
        btnNotification = view.findViewById(R.id.button2);
        iconNotification = view.findViewById(R.id.noti);
        ChangeToNotification();

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

        //Handle button ChangeInfor
        btnChangeInfor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserInformationActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        ChangeToHistory();

        return view;
    }

    private void ChangeToNotification() {
        Intent intent = new Intent(getActivity(), ListNotificationActivity.class);
        iconNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(intent);
            }
        });

        btnNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });

    }

    private void ChangeToHistory(){
        tvHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),HistoryActivity.class);
                startActivity(intent);
            }
        });
    }
}
