package com.example.travel_tour_booking_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatAdminActivity extends AppCompatActivity {

    Button btnEnd;
    ImageView ivBack, ivSend;
    RecyclerView rvChat;
    EditText edtText;

    ArrayList<ChatMessage> chatMessages;
    Messages messagesTemp = new Messages();
    ChatMessageAdapter chatMessageAdapter;
    DatabaseReference databaseReference;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_admin);
        findViewByIds();

        Intent intent = getIntent();
        userId = intent.getStringExtra("USER_ID");

        chatMessages = new ArrayList<>();
        rvChat.setLayoutManager(new LinearLayoutManager(this));
        chatMessageAdapter = new ChatMessageAdapter(chatMessages);
        rvChat.setAdapter(chatMessageAdapter);

        databaseReference = FirebaseDatabase.getInstance(UserInformationActivity.FIREBASE_REALTIME_DATABASE_URL).getReference("Messages");
        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Messages messages = snapshot.getValue(Messages.class);
                messagesTemp.setChatMessages(messages.getChatMessages());
                for (ChatMessage chatMessage : messages.getChatMessages()){
                    if (chatMessage.isBotMessage() == 0){
                        chatMessages.add(new ChatMessage(chatMessage.getContent(), 1));
                    }
                    else {
                        chatMessages.add(new ChatMessage(chatMessage.getContent(), 0));
                    }
                }
                chatMessageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAnswer();
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Messages messages = snapshot.getValue(Messages.class);
                        messages.setStatus(Boolean.FALSE);
                        databaseReference.child(userId).setValue(messages);
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
    private void findViewByIds(){
        ivBack = findViewById(R.id.iv_returnbutton_chat);
        rvChat = findViewById(R.id.rv_chat);
        ivSend = findViewById(R.id.iv_sendbutton_chat);
        edtText = findViewById(R.id.edt_message_chat);
        btnEnd = findViewById(R.id.btn_end);
    }
    public void addAnswer(){
        String content = edtText.getText().toString();

        if (content == null) return;

        chatMessages.add(new ChatMessage(content, 0));
        chatMessageAdapter.notifyDataSetChanged();

        ArrayList<ChatMessage> chatMessagesTemp = messagesTemp.getChatMessages();
        chatMessagesTemp.add(new ChatMessage(content, 1));
        messagesTemp.setChatMessages(chatMessagesTemp);
        databaseReference.child(userId).setValue(messagesTemp);
    }
}