package com.example.travel_tour_booking_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CommentRating extends AppCompatActivity {

    EditText editTextComment;
    RatingBar ratingBar;
    Button buttonSubmit;

    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_rating);

        FirebaseApp.initializeApp(this);
        mDatabase = FirebaseDatabase.getInstance().getReference();


        final EditText editTextComment = findViewById(R.id.editTextComment);
        final RatingBar ratingBar = findViewById(R.id.ratingBar);
        @SuppressLint("WrongViewCast") Button buttonSubmit = findViewById(R.id.buttonSubmit);

        RecyclerView recyclerViewComments = findViewById(R.id.recyclerViewComments);

        final List<CommentModel> commentList = new ArrayList<>();
        final CommentRateAdapter commentAdapter = new CommentRateAdapter(commentList);
        recyclerViewComments.setAdapter(commentAdapter);
        recyclerViewComments.setLayoutManager(new LinearLayoutManager(this));


        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = editTextComment.getText().toString();
                float rating = ratingBar.getRating();

                // Tạo một đối tượng CommentModel để lưu trữ dữ liệu
                CommentModel commentModel = new CommentModel(comment, rating);

                CommentModel newComment = new CommentModel(editTextComment.getText().toString(), ratingBar.getRating());
                commentList.add(newComment);
                commentAdapter.notifyDataSetChanged();

                // Lưu bình luận vào Firebase Realtime Database
                String key = mDatabase.child("comments").push().getKey();
                mDatabase.child("comments").child(key).setValue(commentModel);

                Toast.makeText(CommentRating.this, "Bình luận: " + comment + ", Đánh giá: " + rating, Toast.LENGTH_SHORT).show();
            }
        });

        mDatabase.child("comments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // Duyệt qua tất cả các bình luận và hiển thị chúng
                for (DataSnapshot commentSnapshot : dataSnapshot.getChildren()) {
                    CommentModel commentModel = commentSnapshot.getValue(CommentModel.class);
                    if (commentModel != null) {
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}