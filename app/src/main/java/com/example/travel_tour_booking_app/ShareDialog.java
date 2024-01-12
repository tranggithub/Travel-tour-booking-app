package com.example.travel_tour_booking_app;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

public class ShareDialog extends Dialog implements View.OnClickListener {
    public Activity c;
    public Dialog d;
    public ImageView fb,email,copy, message, close;

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
        if (v.getId() == R.id.iv_share_fb){

        }

        if (v.getId() == R.id.iv_share_email){

        }

        if (v.getId() == R.id.iv_share_copy){

        }
        if (v.getId() == R.id.iv_share_message){

        }
        dismiss();
    }
}
