package com.example.travel_tour_booking_app;

import static com.example.travel_tour_booking_app.UserInformationActivity.FIREBASE_REALTIME_DATABASE_URL;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.awt.font.TextAttribute;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class ReviewFragment extends Fragment {
    private static String FIREBASE_REALTIME_DATABASE_URL = "https://travel-tour-booking-app-default-rtdb.asia-southeast1.firebasedatabase.app/";
    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference databaseReference;
    TextView tvHotelName, tvSaoDanhGia, tvSoDanhGia;
    RatingBar rbHotelRating, rbDanhGiaCuaBan;
    RecyclerView rcvImgHotel, rcvReview;
    ImageView ivAvatar, ivSendComment;
    EditText edtCommentText;
    Hotel hotel;
    ReviewAdapter reviewAdapter;
    ArrayList<Comment> tempComments;
    TextView tv1Sao, tv2Sao, tv3Sao, tv4Sao, tv5Sao;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_review, container, false);
        findViewById(rootView);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance(FIREBASE_REALTIME_DATABASE_URL).getReference("users");
        if (user != null) {
            databaseReference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ReadWriteUserDetails userDetails = snapshot.getValue(ReadWriteUserDetails.class);
                    String avatar = userDetails.getUrlImage();
                    Picasso.get().load(avatar).into(ivAvatar);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getActivity(), "Lỗi người dùng", Toast.LENGTH_SHORT).show();
                }
            });
        }


        // Receive data from the Bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            hotel = (Hotel) bundle.getSerializable("Hotel");

            LoadView();

            rcvReview.setLayoutManager(new LinearLayoutManager(getActivity()));
            rcvReview.setAdapter(reviewAdapter);
            ivSendComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (rbDanhGiaCuaBan.getRating() == 0) {
                        Toast.makeText(getActivity(), "Hãy đánh giá sao cho khách sạn", Toast.LENGTH_SHORT).show();
                    } else {
                        if (edtCommentText.getText().toString().equals("") || edtCommentText.getText() == null) {
                            Toast.makeText(getActivity(), "Hãy nhập bình luận cho khách sạn", Toast.LENGTH_SHORT).show();
                        } else {
                            UploadComment();
                            reviewAdapter.sortByDate(tempComments);

                            edtCommentText.setText("");
                            String star = String.valueOf(hotel.getAvgStar());
                            tvSaoDanhGia.setText(star);
                            rbHotelRating.setRating(hotel.getAvgStar());
                            String soDanhGia = hotel.getSizeComments() + " đánh giá";
                            tvSoDanhGia.setText(soDanhGia);
                            Toast.makeText(getActivity(), "Bình luận thành công", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }

        Sort(hotel);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        LoadView();
    }

    @Override
    public void onStart() {
        super.onStart();
        LoadView();
    }
    private void LoadView() {
        DatabaseReference databaseReferenceHotel = FirebaseDatabase.getInstance(FIREBASE_REALTIME_DATABASE_URL).getReference("Android Hotel");
        databaseReferenceHotel.child(hotel.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    hotel = snapshot.getValue(Hotel.class);

                    tvHotelName.setText(hotel.getName());

                    List<DetailNews> imgHotelList = hotel.getDetailPictureList();
                    HotelImageAdapter hotelImageAdapter = new HotelImageAdapter(getActivity(), imgHotelList);
                    rcvImgHotel.setAdapter(hotelImageAdapter);

                    String star = String.valueOf(hotel.getAvgStar());
                    tvSaoDanhGia.setText(star);

                    rbHotelRating.setRating(hotel.getAvgStar());

                    String soDanhGia = hotel.getSizeComments() + " đánh giá";
                    tvSoDanhGia.setText(soDanhGia);

                    tempComments = hotel.getComments();
                    reviewAdapter = new ReviewAdapter(tempComments, getActivity(), user.getUid(), hotel);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void UploadComment() {
        String today = getCurrentDate();
        String userId = user.getUid();
        Float myRating = rbDanhGiaCuaBan.getRating();
        String text = edtCommentText.getText().toString();
        Comment comment = new Comment(userId, myRating, text, new ArrayList<String>(), new ArrayList<String>(), today);
        tempComments.add(comment);
        hotel.setComments(tempComments);
        DatabaseReference databaseReferenceHotel = FirebaseDatabase.getInstance(FIREBASE_REALTIME_DATABASE_URL).getReference("Android Hotel");
        databaseReferenceHotel.child(hotel.getKey()).setValue(hotel)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Lịch sử tìm kiếm đã được cập nhật thành công
                    } else {
                        // Lỗi khi cập nhật lịch sử tìm kiếm
                    }
                });
    }


    public static String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd 'tháng' MM',' yyyy", new Locale("vi", "VN"));
        return dateFormat.format(calendar.getTime());
    }

    private void findViewById(View rootView) {
        tvHotelName = rootView.findViewById(R.id.tv_hotelname_review);
        tvSaoDanhGia = rootView.findViewById(R.id.tv_saodanhgia_review);
        tvSoDanhGia = rootView.findViewById(R.id.tv_luotdanhgia_review);
        rbHotelRating = rootView.findViewById(R.id.rb_ratinghotel_review);
        rcvImgHotel = rootView.findViewById(R.id.rcv_imghotel_review);
        rcvReview = rootView.findViewById(R.id.rcv_review_review);
        rbDanhGiaCuaBan = rootView.findViewById(R.id.rb_danggiacuaban_review);
        ivAvatar = rootView.findViewById(R.id.iv_avatar_review);
        edtCommentText = rootView.findViewById(R.id.edt_message_review);
        ivSendComment = rootView.findViewById(R.id.iv_sendbutton_review);

        tv1Sao = rootView.findViewById(R.id.tv_1sao_review);
        tv2Sao = rootView.findViewById(R.id.tv_2sao_review);
        tv3Sao = rootView.findViewById(R.id.tv_3sao_review);
        tv4Sao = rootView.findViewById(R.id.tv_4sao_review);
        tv5Sao = rootView.findViewById(R.id.tv_5sao_review);
    }


    private void Sort(Hotel hotel){
        tv1Sao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reviewAdapter.sortByStar(hotel.getComments(),1);
            }
        });
        tv2Sao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reviewAdapter.sortByStar(hotel.getComments(), 2);
            }
        });
        tv3Sao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reviewAdapter.sortByStar(hotel.getComments(), 3);
            }
        });
        tv4Sao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reviewAdapter.sortByStar(hotel.getComments(),4);
            }
        });
        tv5Sao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reviewAdapter.sortByStar(hotel.getComments(),5);
            }
        });
    }
}
