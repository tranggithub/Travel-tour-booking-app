package com.example.travel_tour_booking_app;

import static com.example.travel_tour_booking_app.UserInformationActivity.FIREBASE_REALTIME_DATABASE_URL;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessageViewHolder> {

    private ArrayList<Messages> messagesList;
    private static OnMessageClickListener onMessageClickListener;

    public MessagesAdapter(ArrayList<Messages> messageList) {
        this.messagesList = messageList;
    }


    public interface OnMessageClickListener {
        void onMessageClick(String userId);
    }
    public void setOnMessageClickListener(OnMessageClickListener listener) {
        this.onMessageClickListener = listener;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Messages messages = messagesList.get(position);
        String userId = messages.getSenderId();
        DatabaseReference databaseReferenceUser = FirebaseDatabase.getInstance(FIREBASE_REALTIME_DATABASE_URL).getReference("users");
        databaseReferenceUser.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails userDetails = snapshot.getValue(ReadWriteUserDetails.class);
                String name = (userDetails.getFullName() == null || userDetails.getFullName().equals("")? "Chưa xác định" : userDetails.getFullName());

                holder.nameTextView.setText(name);
                Picasso.get().load(userDetails.getUrlImage()).into(holder.avatarImageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference databaseReferenceMessages = FirebaseDatabase.getInstance(FIREBASE_REALTIME_DATABASE_URL).getReference("Messages");
        databaseReferenceMessages.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Messages messagesDetails = snapshot.getValue(Messages.class);
                ArrayList<ChatMessage> chatMessages = messagesDetails.getChatMessages();
                String lastMessage = chatMessages.get(chatMessages.size()-1).getContent();
                holder.contentTextView.setText(lastMessage);
                holder.dateTextView.setText(messagesDetails.getTimeDate());

                if (messagesDetails.getStatus() == false){
                    holder.contentTextView.setTypeface(null, Typeface.BOLD);
                    holder.dateTextView.setTypeface(null, Typeface.BOLD);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public int getItemCount() {
        return messagesList.size();
    }
    public class MessageViewHolder extends RecyclerView.ViewHolder {
        ImageView avatarImageView;
        TextView nameTextView;
        TextView contentTextView;
        TextView dateTextView;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            avatarImageView = itemView.findViewById(R.id.iv_avatar_list_users);
            nameTextView = itemView.findViewById(R.id.tv_name_list_users);
            contentTextView = itemView.findViewById(R.id.tv_email_list_users);
            dateTextView = itemView.findViewById(R.id.tv_phone_list_users);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onMessageClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            Messages messages = messagesList.get(position);
                            String userId = messages.getSenderId();
                            onMessageClickListener.onMessageClick(userId);
                        }
                    }
                }
            });
        }
    }
}

