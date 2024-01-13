 package com.example.travel_tour_booking_app;

import static com.example.travel_tour_booking_app.UserInformationActivity.FIREBASE_REALTIME_DATABASE_URL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

 public class ListChatAdminActivity extends AppCompatActivity {

    ImageView ivBack;
    RecyclerView rvListChats;
    ArrayList<Messages> messagesArrayList;
    MessagesAdapter messagesAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_chat_admin);
        findViewByIds();

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        messagesArrayList = new ArrayList<>();
        messagesAdapter = new MessagesAdapter(messagesArrayList);
        rvListChats.setLayoutManager(new LinearLayoutManager(this));
        rvListChats.setAdapter(messagesAdapter);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance(FIREBASE_REALTIME_DATABASE_URL).getReference("Messages");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    Messages messages = itemSnapshot.getValue(Messages.class);
                    if (messages.getStatus() == Boolean.TRUE)  messagesArrayList.add(messages);
                }
                messagesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        messagesAdapter.setOnMessageClickListener(new MessagesAdapter.OnMessageClickListener() {
            @Override
            public void onMessageClick(String userId) {
                Intent intent = new Intent(ListChatAdminActivity.this, ChatAdminActivity.class);
                intent.putExtra("USER_ID", userId);
                startActivity(intent);
            }
        });
    }
    private void findViewByIds(){
        ivBack = findViewById(R.id.iv_returnbutton_list_hotels);
        rvListChats = findViewById(R.id.rv_list_chats);
    }
}