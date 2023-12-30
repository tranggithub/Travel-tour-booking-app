package com.example.travel_tour_booking_app;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {
    Context context;
    List<Search> searches;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public SearchAdapter (Context context, List<Search> searches){
        this.context = context;
        this.searches = searches;
    }
    @Override
    public SearchAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.item_search, parent, false);
        SearchAdapter.MyViewHolder myViewHolder = new SearchAdapter.MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.MyViewHolder holder, int position) {
        holder.ivIcon.setImageResource(searches.get(position).getIcon());
        holder.tvText.setText(searches.get(position).getText());


        holder.clSearchItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(searches.get(holder.getAdapterPosition()).getIcon() == R.drawable.icon_bin){
                    removeElements(searches,R.drawable.icon_history);
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://travel-tour-booking-app-default-rtdb.asia-southeast1.firebasedatabase.app/")
                            .getReference("users");
                    databaseReference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                ReadWriteUserDetails userDetails = snapshot.getValue(ReadWriteUserDetails.class);
                                userDetails.setSearchHistory(null);
                                updateSearchHistory(userDetails);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Xử lý lỗi ở đây
                        }
                    });

                    notifyDataSetChanged();
                } else {
                    Intent intent = new Intent(context, SearchResultActivity.class);
                    intent.putExtra("Text", searches.get(holder.getAdapterPosition()).getText());
                    context.startActivity(intent);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return searches.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView tvText;
        private ImageView ivIcon;
        ConstraintLayout clSearchItem;

        public MyViewHolder(View itemView){
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_search_icon);
            tvText = itemView.findViewById(R.id.tv_search_text);
            clSearchItem = itemView.findViewById(R.id.cl_search_item);

        }
    }
    public static void removeElements(List<Search> list, int valueToRemove) {
        // Sử dụng Iterator để loại bỏ các phần tử trong quá trình lặp
        Iterator<Search> iterator = list.iterator();
        while (iterator.hasNext()) {
            Search currentValue = iterator.next();
            if (currentValue.getIcon() == valueToRemove) {
                iterator.remove();
            }
        }
    }
    private void updateSearchHistory(ReadWriteUserDetails userDetails) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://travel-tour-booking-app-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("users");

        // Update lịch sử tìm kiếm trên Firebase
        databaseReference.child(user.getUid()).setValue(userDetails)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Lịch sử tìm kiếm đã được cập nhật thành công
                    } else {
                        // Lỗi khi cập nhật lịch sử tìm kiếm
                    }
                });
    }

}