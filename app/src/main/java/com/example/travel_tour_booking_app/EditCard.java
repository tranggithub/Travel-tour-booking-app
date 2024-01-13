package com.example.travel_tour_booking_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditCard extends AppCompatActivity {

    private EditText editTextCardNumber, editTextCardName, editTextExpiryDate;
    private Button btnSaveChanges;
    private DatabaseReference databaseReference;
    private String cardId;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_card);

        editTextCardNumber = findViewById(R.id.editTextCardNumber);
        editTextCardName = findViewById(R.id.editTextCardName);
        editTextExpiryDate = findViewById(R.id.editTextExpiryDate);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        if (intent != null) {
            cardId = intent.getStringExtra("cardId");
            if (cardId != null) {
                // Load thông tin thẻ từ Firebase và hiển thị trong EditText
                databaseReference = FirebaseDatabase.getInstance().getReference("bankCards").child(cardId);
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        BankCard card = dataSnapshot.getValue(BankCard.class);
                        if (card != null) {
                            editTextCardNumber.setText(card.getCardNumber());
                            editTextCardName.setText(card.getCardName());
                            editTextExpiryDate.setText(card.getExpiryDate());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Xử lý khi có lỗi đọc dữ liệu
                    }
                });
            }
        }

        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCard();
            }
        });
    }

    private void updateCard() {
        // Lấy thông tin từ EditText
        String updatedCardNumber = editTextCardNumber.getText().toString().trim();
        String updatedCardName = editTextCardName.getText().toString().trim();
        String updatedExpiryDate = editTextExpiryDate.getText().toString().trim();

        // Kiểm tra xem các trường có được nhập đầy đủ không
        if (TextUtils.isEmpty(updatedCardNumber) || TextUtils.isEmpty(updatedCardName) || TextUtils.isEmpty(updatedExpiryDate)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Cập nhật thông tin thẻ vào Firebase
        BankCard updatedCard = new BankCard(cardId, updatedCardNumber, updatedCardName, updatedExpiryDate);
        databaseReference.setValue(updatedCard);

        // Quay lại màn hình chính
        finish();
    }

}