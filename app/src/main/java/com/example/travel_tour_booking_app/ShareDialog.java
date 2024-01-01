package com.example.travel_tour_booking_app;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ShareDialog extends Dialog implements View.OnClickListener {
    public Activity c;
    public Dialog d;
    public ImageView fb,email,copy, message, close;
    private CharSequence link;

    public ShareDialog(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.share_layout);
        fb = (ImageView) findViewById(R.id.iv_share_fb);
        email = (ImageView) findViewById(R.id.iv_share_email);
        copy = (ImageView) findViewById(R.id.iv_share_copy);
        message = (ImageView) findViewById(R.id.iv_share_message);
        close = (ImageView) findViewById(R.id.iv_share_close);

        fb.setOnClickListener(this);
        email.setOnClickListener(this);
        copy.setOnClickListener(this);
        message.setOnClickListener(this);
        close.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_share_close){
            dismiss();
        }

        if (v.getId() == R.id.iv_share_fb) {
            Intent myIntent = new Intent(Intent.ACTION_SEND);
            myIntent.setType("text/plain");
            myIntent.setPackage("com.facebook.katana");
            String body = "Chuyến đi ";
            String sub = "Your Subject";
            myIntent.putExtra(Intent.EXTRA_SUBJECT, sub);
            myIntent.putExtra(Intent.EXTRA_TEXT, body);
            c.startActivity(myIntent);
        }

        if (v.getId() == R.id.iv_share_email) {
            DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Android Tours");

            databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String tourId = "Android Tours"; // thay thế bằng id chuyến đi cụ thể
                    String title = snapshot.child(tourId).child("title").getValue(String.class);

                    String body = "Check out this tour: " + title + "\n";
                    String sub = "Your Subject";

                    Intent myIntent = new Intent(Intent.ACTION_SENDTO);
                    myIntent.setData(Uri.parse("mailto:")); // Chỉ định địa chỉ email ở đây
                    myIntent.putExtra(Intent.EXTRA_SUBJECT, sub);
                    myIntent.putExtra(Intent.EXTRA_TEXT, body);

                    c.startActivity(Intent.createChooser(myIntent, "Share Using"));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(c, "Lỗi truy cập dữ liệu", Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (v.getId() == R.id.iv_share_copy) {
            Intent myIntent = new Intent(Intent.ACTION_SEND);
            myIntent.setType("text/plain");
            String body = "Chuyến đi ";
            String sub = "Your Subject";
            myIntent.putExtra(Intent.EXTRA_SUBJECT,sub);
            myIntent.putExtra(Intent.EXTRA_TEXT,body);
            c.startActivity(Intent.createChooser(myIntent, "Share Using"));
        }

        if (v.getId() == R.id.iv_share_message){
            Intent myIntent = new Intent(Intent.ACTION_SEND);
            myIntent.setType("text/plain");
            String body = "Chuyến đi ";
            String sub = "Your Subject";
            myIntent.putExtra(Intent.EXTRA_SUBJECT,sub);
            myIntent.putExtra(Intent.EXTRA_TEXT,body);
            c.startActivity(Intent.createChooser(myIntent, "Share Using"));
        }
        dismiss();
    }
}
