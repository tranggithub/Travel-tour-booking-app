package com.example.travel_tour_booking_app;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddCard extends AppCompatActivity {

    private EditText editTextCardNumber, editTextCardName, editTextExpiryDate;
    private DatabaseReference databaseReference;
    private String cardName;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_card_edit);

        // Tham chiếu đến Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference("bankCards");

        // Tham chiếu đến các View trong activity_add_card.xml
        editTextCardNumber = findViewById(R.id.editTextCardNumber);
        editTextCardName = findViewById(R.id.editTextCardName);
        editTextExpiryDate = findViewById(R.id.editTextExpiryDate);
        Button btnSave = findViewById(R.id.btnSave);

        // Nhận dữ liệu từ Intent nếu có
        Intent intent = getIntent();
        if (intent != null) {
            String cardId = intent.getStringExtra("cardId");
            if (cardId != null) {
                // Xử lý dữ liệu nếu cần thiết
                // Ví dụ: Bạn có thể muốn sử dụng cardId để thực hiện một tác vụ cụ thể trong AddCardActivity.
            }
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBankCard();
            }
        });
    }

    private void saveBankCard() {
        // Lấy thông tin từ EditText
        String cardNumber = editTextCardNumber.getText().toString().trim();
        String cardHolder = editTextCardName.getText().toString().trim();
        String expiryDate = editTextExpiryDate.getText().toString().trim();

        // Kiểm tra xem các trường có được nhập đầy đủ không
        if (TextUtils.isEmpty(cardNumber) || TextUtils.isEmpty(cardHolder) || TextUtils.isEmpty(expiryDate)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo đối tượng BankCard mới
        String cardId = databaseReference.push().getKey(); // Tạo cardId mới từ Firebase
        BankCard newCard = new BankCard(cardId, cardNumber, cardName, expiryDate);

        // Thêm mới thẻ ngân hàng vào Firebase
        if (cardId != null) {
            databaseReference.child(cardId).setValue(newCard);
        }

        // Quay lại màn hình chính
        finish();
    }
}
