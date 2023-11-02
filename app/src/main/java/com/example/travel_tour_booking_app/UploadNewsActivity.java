package com.example.travel_tour_booking_app;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class UploadNewsActivity extends AppCompatActivity {
    Button bt_UploadNews;
    EditText et_title, et_date, et_text;
    String ThumbnailURL;
    Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_news);

        bt_UploadNews = findViewById(R.id.btn_upload_news);
        et_title = findViewById(R.id.edt_title_upload_news);
        et_date = findViewById(R.id.edt_date_upload_news);
        et_text = findViewById(R.id.edt_text_upload_news);
    }
}