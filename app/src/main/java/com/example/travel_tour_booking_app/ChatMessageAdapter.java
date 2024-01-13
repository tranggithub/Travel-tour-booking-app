package com.example.travel_tour_booking_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.ChatViewHolder> {
    private List<ChatMessage> chatList;
    private static final int USER_MESSAGE = 0;
    private static final int BOT_MESSAGE = 1;
    private static final int BOT_LIST_MESSAGE = 2;

    private OnItemClickListener itemClickListener;

    public ChatMessageAdapter(List<ChatMessage> chatList) {
        this.chatList = chatList;
    }

    public ChatMessageAdapter(List<ChatMessage> chatList, OnItemClickListener itemClickListener) {
        this.chatList = chatList;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == BOT_MESSAGE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bot_msg, parent, false);
        } else if (viewType == BOT_LIST_MESSAGE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bot_list_msg, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_msg, parent, false);
        }
        return new ChatViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatMessage chatMessage = chatList.get(position);

        if (chatMessage.getIsBotMessage() == BOT_LIST_MESSAGE) {
            List<String> listData = chatMessage.getListData();
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(holder.itemView.getContext(), R.layout.list_item_message, listData);
            holder.messageListView.setAdapter(arrayAdapter);

            // Set an OnClickListener for items of type BOT_LIST_MESSAGE
            holder.messageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int itemPosition, long itemId) {
                    if (itemClickListener != null) {
                        if (itemPosition == 0) {
                            return;
                        } else {
                            itemClickListener.onItemClick(chatMessage.getListData().get(itemPosition));
                        }
                    }
                }
            });
        } else {
            holder.messageTextView.setText(chatMessage.getContent());
        }
    }

    @Override
    public int getItemViewType(int position) {
        // Determine the message type based on the ChatMessage object
        ChatMessage chatMessage = chatList.get(position);
        int messageType = chatMessage.getIsBotMessage();

        if (messageType == USER_MESSAGE) {
            return USER_MESSAGE;
        } else if (messageType == BOT_MESSAGE) {
            return BOT_MESSAGE;
        } else if (messageType == BOT_LIST_MESSAGE) {
            return BOT_LIST_MESSAGE;
        }

        return -1; // Invalid message type
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;
        MyListView messageListView;

        public ChatViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);

            if (viewType == BOT_MESSAGE) {
                messageTextView = itemView.findViewById(R.id.tv_botmsg_chat);
            } else if (viewType == USER_MESSAGE) {
                messageTextView = itemView.findViewById(R.id.tv_usermsg_chat);
            } else if (viewType == BOT_LIST_MESSAGE) {
                messageListView = itemView.findViewById(R.id.lv_botselectmsg_chat);
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String selectItem);
    }
}


