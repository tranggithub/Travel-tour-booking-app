package com.example.travel_tour_booking_app;

import static com.example.travel_tour_booking_app.UserInformationActivity.FIREBASE_REALTIME_DATABASE_URL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.Normalizer;
import java.util.ArrayList;

public class ListUserAdminActivity extends AppCompatActivity {
    ArrayList<String> users;
    RecyclerView rcvListUsers;
    DatabaseReference databaseReference;
    ArrayList<Pagination> paginationArrayList;
    PaginationAdapter paginationAdapter;
    EditText edt_search;
    UsersAdapter usersAdapter;
    NestedScrollView scrollView;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_user_admin);

        findViewByIds();

        users = new ArrayList<>();
        usersAdapter = new UsersAdapter(users);
        rcvListUsers.setLayoutManager(new LinearLayoutManager(this));
        rcvListUsers.setAdapter(usersAdapter);

        databaseReference = FirebaseDatabase.getInstance(FIREBASE_REALTIME_DATABASE_URL).getReference("users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    String user = itemSnapshot.getKey();
                    users.add(user);
                }
                usersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        usersAdapter.setOnUserClickListener(new UsersAdapter.OnUserClickListener() {
            @Override
            public void onUserClick(String userId) {
                // Handle the click event, for example, open a dialog
                showUserDetailsDialog(userId);
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

        RecyclerView recyclerViewPagination = findViewById(R.id.rv_pigination_list_user);
        recyclerViewPagination.setAdapter(paginationAdapter);
    }

    public void GoBack(View view) {
        finish();
    }

    private void findViewByIds() {
        rcvListUsers = findViewById(R.id.rv_list_users);
        scrollView = findViewById(R.id.sv_list_users);
        button = findViewById(R.id.btn_up_list_user);
    }

    private void showUserDetailsDialog(String userId) {
        DatabaseReference databaseReferenceUser = FirebaseDatabase.getInstance(FIREBASE_REALTIME_DATABASE_URL).getReference("users");
        databaseReferenceUser.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails userDetails = snapshot.getValue(ReadWriteUserDetails.class);
                String name = "Tên: " + (userDetails.getFullName() == null || userDetails.getFullName().equals("")? "Chưa xác định" : userDetails.getFullName());
                String email = "Email: " + (userDetails.getEmail() == null || userDetails.getEmail().equals("")? "Chưa xác định" : userDetails.getEmail());
                String phone = "SDT: " + (userDetails.getSdt() == null || userDetails.getSdt().equals("")? "Chưa xác định" : userDetails.getSdt());

                AlertDialog.Builder builder = new AlertDialog.Builder(ListUserAdminActivity.this);
                builder.setTitle("Chi tiết người dùng")
                        .setMessage(name +
                                "\n" + email +
                                "\n" + phone +
                                "\nRole: " + userDetails.getRole() +
                                "\nTrạng thái: " + (userDetails.getDelected() == 1? "Đã xóa" : "Còn hoạt động"))
                        .setPositiveButton("OK", null)
                        .setNegativeButton("Xóa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (userDetails.getDelected() == 1) {
                                    Toast.makeText(ListUserAdminActivity.this, "Người dùng đã bị xóa, không còn hoạt động", Toast.LENGTH_SHORT).show();
                                    showUserDetailsDialog(userId);
                                } else if (userDetails.getRole().equals("admin")) {
                                    Toast.makeText(ListUserAdminActivity.this, "Không thể xóa admin", Toast.LENGTH_SHORT).show();
                                    showUserDetailsDialog(userId);
                                } else {
                                    AlertDialog.Builder builderDelete = new AlertDialog.Builder(ListUserAdminActivity.this);
                                    builderDelete.setTitle("Xác nhận xóa người dùng")
                                            .setMessage("Bạn có chắc chắn muốn xóa người dùng này?")
                                            .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    userDetails.setDelected(1);
                                                    databaseReferenceUser.child(userId).setValue(userDetails).addOnCompleteListener(task -> {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(ListUserAdminActivity.this, "Đã xóa người dùng thành công", Toast.LENGTH_SHORT).show();
                                                            showUserDetailsDialog(userId);
                                                        } else {
                                                            Toast.makeText(ListUserAdminActivity.this, "Xóa người dùng thất bại", Toast.LENGTH_SHORT).show();
                                                            showUserDetailsDialog(userId);
                                                        }
                                                    });
                                                }
                                            })
                                            .setNegativeButton("Hủy", null) // Nút "Hủy" để người dùng có thể hủy bỏ xóa
                                            .show();
                                }
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Search Function
        edt_search = findViewById(R.id.edt_users_search);

        edt_search.addTextChangedListener(new TextWatcher() {
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

        ScrollToTop();
    }

    private void performSearch(String toString) {
        ArrayList<String> searchList = new ArrayList<>();
        for (String userId : users) {
            DatabaseReference databaseReferenceUser = FirebaseDatabase.getInstance(FIREBASE_REALTIME_DATABASE_URL).getReference("users");
            databaseReferenceUser.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ReadWriteUserDetails userDetails = snapshot.getValue(ReadWriteUserDetails.class);
                    if (normalizeString(userDetails.getFullName().toLowerCase()).contains(toString.toLowerCase())){
                        searchList.add(userId);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        usersAdapter.searchUsers(searchList);
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
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        });

    }
}