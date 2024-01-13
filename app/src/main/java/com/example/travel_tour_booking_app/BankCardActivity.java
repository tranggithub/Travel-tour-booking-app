package com.example.travel_tour_booking_app;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BankCardActivity extends AppCompatActivity {

    private List<BankCard> bankCardList;
    private BankCardAdapter bankCardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_bank_card);

        // Khởi tạo Firebase
        FirebaseApp.initializeApp(this);

        // Tham chiếu đến RecyclerView và khởi tạo Adapter
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) RecyclerView recyclerViewBankCards = findViewById(R.id.recyclerViewBankCards);
        bankCardList = new ArrayList<>();
        bankCardAdapter = new BankCardAdapter(this, bankCardList);
        recyclerViewBankCards.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewBankCards.setAdapter(bankCardAdapter);

        // Tham chiếu đến Firebase Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("bankCards");

        // Tham chiếu đến FloatingActionButton
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(BankCardActivity.this, AddCard.class);
            startActivity(intent);
        });

        // Lắng nghe sự thay đổi trong dữ liệu Firebase Realtime Database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bankCardList.clear();

                for (DataSnapshot cardSnapshot : dataSnapshot.getChildren()) {
                    BankCard bankCard = cardSnapshot.getValue(BankCard.class);
                    if (bankCard != null) {
                        // Lấy cardId từ Firebase Key (tên nút)
                        String cardId = cardSnapshot.getKey();
                        bankCard.getCardId();

                        bankCardList.add(bankCard);
                    }
                }

                bankCardAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BankCardActivity.this, "Failed to read data from Firebase.", Toast.LENGTH_SHORT).show();
            }
        });

        // Thêm sự kiện xóa vào Adapter
        bankCardAdapter.setOnItemClickListener(new BankCardAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Xử lý khi nhấn vào item (nếu cần)
            }

            @Override
            public void onEditClick(int position) {
                // Xử lý khi nhấn vào nút "Sửa" (nếu cần)
                Intent intent = new Intent(BankCardActivity.this, EditCard.class);
                intent.putExtra("cardId", bankCardList.get(position).getCardId());
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(int position) {
                // Xử lý khi nhấn vào nút "Xóa"
                showDeleteDialog(position);
            }
        });
    }

    private void showDeleteDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Delete");
        builder.setMessage("Are you sure you want to delete this bank card?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            // Xử lý xóa thẻ ngân hàng
            deleteBankCard(position);
        });
        builder.setNegativeButton("No", (dialog, which) -> {
            // Đóng dialog
            dialog.dismiss();
        });
        builder.create().show();
    }

    private void deleteBankCard(int position) {
        // Xử lý xóa thẻ ngân hàng từ Firebase
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("bankCards");
        reference.child(bankCardList.get(position).getCardId()).removeValue();
    }
}

