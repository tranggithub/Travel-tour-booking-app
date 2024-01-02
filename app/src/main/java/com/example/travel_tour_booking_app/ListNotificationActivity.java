package com.example.travel_tour_booking_app;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.Normalizer;
import java.util.ArrayList;

public class ListNotificationActivity extends AppCompatActivity {
    ArrayList<Notification> notifications;
    NotificationAdapter notificationAdapter;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    ArrayList<Pagination> paginationArrayList;
    PaginationAdapter paginationAdapter;
    String DatabaseUrl = "https://travel-tour-booking-app-default-rtdb.asia-southeast1.firebasedatabase.app";
    EditText et_search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);


        //Progress layout
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog alertDialog = builder.create();
//        alertDialog.show();

        //Notification
        notifications = new ArrayList<>();

        notificationAdapter = new NotificationAdapter(this, notifications);

        RecyclerView rvNotification = findViewById(R.id.rv_list_notifications);
        rvNotification.setLayoutManager(new LinearLayoutManager(this));
        rvNotification.setAdapter(notificationAdapter);


        //Firebase
        databaseReference = FirebaseDatabase.getInstance(DatabaseUrl).getReference("Android Notification");
//        alertDialog.show();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                notifications.clear();
                for (DataSnapshot itemSnapshot: snapshot.getChildren()){
                    Notification tempNotification = itemSnapshot.getValue(Notification.class);
                    tempNotification.setKey(itemSnapshot.getKey());
                    if (tempNotification.isActive())
                        notifications.add(tempNotification);
                }
                notificationAdapter.notifyDataSetChanged();
                alertDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
//                alertDialog.dismiss();
            }
        });

        //Pagination
        Pagination tempPagination = new Pagination("1");

        paginationArrayList = new ArrayList<>();
        paginationArrayList.add(tempPagination);
        paginationArrayList.add(tempPagination);
        paginationArrayList.add(tempPagination);
        paginationArrayList.add(tempPagination);
        paginationArrayList.add(tempPagination);
        paginationArrayList.add(tempPagination);

        paginationAdapter = new PaginationAdapter(this,paginationArrayList);

        RecyclerView recyclerViewPagination = findViewById(R.id.rv_pigination_list_notification);
        recyclerViewPagination.setAdapter(paginationAdapter);

        //Search Function
        et_search = findViewById(R.id.et_notifications_search);

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                performSearch(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Scroll to Top
        ScrollToTop();
        GoBack();
    }

    private void performSearch(String query) {
        ArrayList<Notification> searchList = new ArrayList<>();
        for (Notification item : notifications){
            if(normalizeString(item.getTitle().toLowerCase()).contains(query.toLowerCase())){
                searchList.add(item);
            }
        }
        notificationAdapter.searchNotification(searchList);
    }
    private String normalizeString(String input) {
        // Loại bỏ dấu từ chuỗi
        String regex = "\\p{InCombiningDiacriticalMarks}+";
        String normalizedString = Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll(regex, "")
                .toLowerCase();
        return normalizedString;
    }

    public void ScrollToTop()
    {
        Button button = findViewById(R.id.btn_up);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScrollView scrollView = findViewById(R.id.sv_list_notifications);
                scrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        });

    }

    public void GoBack()
    {
        ImageView imageView = findViewById(R.id.iv_returnbutton_list_notifications);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
