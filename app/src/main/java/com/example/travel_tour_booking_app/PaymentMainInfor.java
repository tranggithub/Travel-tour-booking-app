package com.example.travel_tour_booking_app;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PaymentMainInfor extends AppCompatActivity {

    private EditText cardNumberEditText;
    private EditText nameEditText;
    private EditText phoneNumberEditText;

    // Tham chiếu đến node trong database
    private DatabaseReference databaseReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_payment_info);

        // Ánh xạ các thành phần trong layout
        cardNumberEditText = findViewById(R.id.pasenger_id);
        nameEditText = findViewById(R.id.editTextName);
        phoneNumberEditText = findViewById(R.id.phonenum);

        Button saveButton = findViewById(R.id.save_passenger);

        // Khởi tạo tham chiếu đến node "passengers" trong database
        databaseReference = FirebaseDatabase.getInstance().getReference("passengers");

        // Xử lý sự kiện khi nhấn vào nút "Lưu lại"
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy thông tin từ các EditText
                String cardNumber = cardNumberEditText.getText().toString();
                String name = nameEditText.getText().toString();
                String phoneNumber = phoneNumberEditText.getText().toString();

                // Tạo một đối tượng Passenger
                PaymentInfoModel passenger = new PaymentInfoModel(cardNumber, name, phoneNumber);

                // Lưu thông tin vào Firebase Realtime Database
                databaseReference.push().setValue(passenger);

                // Hiển thị thông báo hoặc thực hiện các thao tác cần thiết
                Toast.makeText(PaymentMainInfor.this, "Thông tin đã được lưu lại", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

