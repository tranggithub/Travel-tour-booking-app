package com.example.travel_tour_booking_app;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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

    Boolean isClick = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_review, container, false);

        findViewById(rootView);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance(FIREBASE_REALTIME_DATABASE_URL).getReference("users");
        if (user != null){
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
        if (bundle != null){
            hotel = (Hotel) bundle.getSerializable("Hotel");

            tvHotelName.setText(hotel.getName());

            List<DetailNews> imgHotelList = new ArrayList<>();
            imgHotelList = hotel.getDetailPictureList();
            HotelImageAdapter hotelImageAdapter = new HotelImageAdapter(getActivity(), imgHotelList);
            rcvImgHotel.setAdapter(hotelImageAdapter);

            String star = String.valueOf(hotel.getAvgStar());
            tvSaoDanhGia.setText(star);

            rbHotelRating.setRating(hotel.getAvgStar());

            String soDanhGia = hotel.getSizeComments() +" đánh giá";
            tvSoDanhGia.setText(soDanhGia);

            tempComments = hotel.getComments();
            reviewAdapter = new ReviewAdapter(tempComments, getContext(), user.getUid());
            rcvReview.setAdapter(reviewAdapter);
            ivSendComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (rbDanhGiaCuaBan.getRating() == 0){
                        Toast.makeText(getActivity(), "Hãy đánh giá sao cho khách sạn", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if (edtCommentText.getText().toString().equals("") || edtCommentText.getText() == null) {
                            Toast.makeText(getActivity(), "Hãy nhập bình luận cho khách sạn", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            String today = getCurrentDate();
                            String userId = user.getUid();
                            Float myRating = rbDanhGiaCuaBan.getRating();
                            String text = edtCommentText.getText().toString();

                            Comment comment = new Comment(userId, myRating, text, today);
                            tempComments.add(comment);
                            reviewAdapter.notifyItemInserted(tempComments.size() - 1);

                            hotel.setComments(tempComments);

                            edtCommentText.setText("");
                            String star = String.valueOf(hotel.getAvgStar());
                            tvSaoDanhGia.setText(star);
                            rbHotelRating.setRating(hotel.getAvgStar());
                            String soDanhGia = hotel.getSizeComments() +" đánh giá";
                            tvSoDanhGia.setText(soDanhGia);
                            Toast.makeText(getActivity(), "Bình luận thành công", Toast.LENGTH_SHORT).show();

                            UploadComment(hotel);
                        }
                    }
                }
            });
        }
        return rootView;
    }

    private void UploadComment(Hotel hotel) {
        DatabaseReference databaseReferenceHotel = FirebaseDatabase.getInstance(FIREBASE_REALTIME_DATABASE_URL).getReference("Android Hotel");
        databaseReferenceHotel.child(hotel.getKey()).setValue(hotel)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getActivity(), "Lưu bình luận thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Lưu bình luận thất bại", Toast.LENGTH_SHORT).show();
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
    }

}
