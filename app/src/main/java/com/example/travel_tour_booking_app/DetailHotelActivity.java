package com.example.travel_tour_booking_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.UnstableApi;

import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
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

@UnstableApi public class DetailHotelActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    TextView tv_edit, tvPrice;
    Hotel hotel;
    boolean isAdmin;
    ImageView ivBack;
    Place tours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_hotel);
        Fragment reviewFragment = new ReviewFragment();
        replaceFragment(reviewFragment);

        initID();

            hotel = (Hotel) getIntent().getSerializableExtra("Hotel");
            tours = (Place) getIntent().getSerializableExtra("Tour");

            tvPrice.setText(tours.getPrice());

            // Pass the Hotel object to the fragment using a Bundle
            Bundle bundle = new Bundle();
            bundle.putSerializable("Hotel", hotel);

            reviewFragment.setArguments(bundle);

        //Phân quyền - admin
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Nếu là admin thì sẽ có chức năng sửa ngược lại thì không
            checkIsAdmin(currentUser.getUid());
        }


        ChangeEdit();
        GoBack();
    }

    private void initID() {
        tv_edit = findViewById(R.id.tv_detail_hotel_edit);
        ivBack = findViewById(R.id.iv_returnbutton_review);
        tvPrice = findViewById(R.id.tv_price_review);
    }

    private void ChangeEdit() {
        tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailHotelActivity.this, UpdateHotelActivity.class);
                intent.putExtra("Hotel", (Hotel) hotel);
                startActivity(intent);
            }
        });
    }

    private void checkIsAdmin(String userId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://travel-tour-booking-app-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("users");

        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ReadWriteUserDetails userDetails = snapshot.getValue(ReadWriteUserDetails.class);

                    if ("user".equals(userDetails.getRole()) && userDetails.getDelected() == 0) {
                        tv_edit.setVisibility(View.GONE);
                        isAdmin = false;

                    } else if ("admin".equals(userDetails.getRole()) && userDetails.getDelected() == 0) {
                        tv_edit.setVisibility(View.VISIBLE);
                        isAdmin = true;

                    } else {
                        Toast.makeText(DetailHotelActivity.this, "Người dùng đã bị xóa", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi ở đây
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_review, fragment);
        fragmentTransaction.commit();
    }
    private void GoBack(){
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
       }
}