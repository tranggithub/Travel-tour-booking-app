package com.example.travel_tour_booking_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChatActivity extends AppCompatActivity {
    EditText edtUserType;
    ImageView ivSendButton, ivBackButton;
    NavigationView nvBottom;
    ChatFragment chatFragment;
    DatabaseReference databaseReference;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        chatFragment = new ChatFragment();
        replaceFragment(chatFragment);

        databaseReference = FirebaseDatabase.getInstance(UserInformationActivity.FIREBASE_REALTIME_DATABASE_URL).getReference("Messages");

        nvBottom = findViewById(R.id.nv_bottomNavigationView_chat);
        edtUserType = findViewById(R.id.edt_message_chat);
        ivSendButton = findViewById(R.id.iv_sendbutton_chat);
        ivBackButton = findViewById(R.id.iv_returnbutton_chat);

        nvBottom.setVisibility(View.INVISIBLE);

        ivBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chatFragment.messages != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
                    builder.setMessage("Nếu bạn thóat ra bây giờ, đoạn chat sẽ bị hủy. Bạn có chắc chắn muốn thoát?")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    databaseReference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            Messages messages = snapshot.getValue(Messages.class);
                                            messages.setStatus(Boolean.FALSE);
                                            databaseReference.child(user.getUid()).setValue(messages);
                                            finish();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            });

                    // Show the alert dialog
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else finish();
            }
        });
        ivSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the user's message from the EditText
                if (edtUserType.getText().toString() != null) {
                    String userTypeMsg = edtUserType.getText().toString();

                    // Call the function to add the user's message to the chat
                    chatFragment.addUserMessageToChatWithAdmin(userTypeMsg);
//                    chatFragment.addBotResponse(userTypeMsg);

                    // Clear the input field after sending
                    edtUserType.setText("");
                }
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_chat, fragment);
        fragmentTransaction.commit();
    }
}