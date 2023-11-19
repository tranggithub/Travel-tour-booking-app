package com.example.travel_tour_booking_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class ChatActivity extends AppCompatActivity {
    EditText edtUserType;
    ImageView ivSendButton, ivBackButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ChatFragment chatFragment = new ChatFragment();
        replaceFragment(chatFragment);

        edtUserType = findViewById(R.id.edt_message_chat);
        ivSendButton = findViewById(R.id.iv_sendbutton_chat);
        ivBackButton = findViewById(R.id.iv_returnbutton_chat);

        ivBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
        ivSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the user's message from the EditText
                if (edtUserType.getText().toString()!=null)
                {
                    String userTypeMsg = edtUserType.getText().toString();

                    // Call the function to add the user's message to the chat
                    chatFragment.addUserMessageToChat(userTypeMsg);
                    chatFragment.addBotResponse(userTypeMsg);

                    // Clear the input field after sending
                    edtUserType.setText("");
                }
            }
        });
    }
    private void replaceFragment(Fragment fragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_chat, fragment);
        fragmentTransaction.commit();
    }
}