package com.example.travel_tour_booking_app;

import static com.example.travel_tour_booking_app.UserInformationActivity.FIREBASE_REALTIME_DATABASE_URL;

import android.content.Context;
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

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    private ArrayList<String> userList;
    private static OnUserClickListener onUserClickListener;

    public UsersAdapter(ArrayList<String> userList) {
        this.userList = userList;
    }

    public void searchUsers(ArrayList<String> searchList) {
        this.userList = searchList;
        notifyDataSetChanged();
    }

    public interface OnUserClickListener {
        void onUserClick(String userId);
    }
    public void setOnUserClickListener(OnUserClickListener listener) {
        this.onUserClickListener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        String userId = userList.get(position);

        DatabaseReference databaseReferenceUser = FirebaseDatabase.getInstance(FIREBASE_REALTIME_DATABASE_URL).getReference("users");
        databaseReferenceUser.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails userDetails = snapshot.getValue(ReadWriteUserDetails.class);
                String name = "Tên: " + (userDetails.getFullName() == null || userDetails.getFullName().equals("")? "Chưa xác định" : userDetails.getFullName());
                String email = "Email: " + (userDetails.getEmail() == null || userDetails.getEmail().equals("")? "Chưa xác định" : userDetails.getEmail());
                String phone = "SDT: " + (userDetails.getSdt() == null || userDetails.getSdt().equals("")? "Chưa xác định" : userDetails.getSdt());

                holder.nameTextView.setText(name);
                holder.emailTextView.setText(email);
                holder.phoneTextView.setText(phone);

                Picasso.get().load(userDetails.getUrlImage()).into(holder.avatarImageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public int getItemCount() {
        return userList.size();
    }
    public class UserViewHolder extends RecyclerView.ViewHolder {
        ImageView avatarImageView;
        TextView nameTextView;
        TextView emailTextView;
        TextView phoneTextView;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            avatarImageView = itemView.findViewById(R.id.iv_avatar_list_users);
            nameTextView = itemView.findViewById(R.id.tv_name_list_users);
            emailTextView = itemView.findViewById(R.id.tv_email_list_users);
            phoneTextView = itemView.findViewById(R.id.tv_phone_list_users);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onUserClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            String userId = userList.get(position);
                            onUserClickListener.onUserClick(userId);
                        }
                    }
                }
            });
        }
    }
}

