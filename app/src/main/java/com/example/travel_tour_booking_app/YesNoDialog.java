package com.example.travel_tour_booking_app;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class YesNoDialog extends Dialog implements View.OnClickListener {
    private Activity activity;
    private TextView tv_question;
    private ImageView close;
    public TextView btn_yes, btn_no;
    String question, yes_statement, no_statement;

    public YesNoDialog(Activity a, String question, String yes_statement, String no_statement) {
        super(a);
        // TODO Auto-generated constructor stub
        this.activity = a;
        this.question = question;
        this.yes_statement = yes_statement;
        this.no_statement = no_statement;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.yesno_layout);

        btn_no = findViewById(R.id.btn_no);
        btn_no.setText(no_statement);

        btn_yes = findViewById(R.id.btn_yes);
        btn_yes.setText(yes_statement);

        tv_question = (TextView) findViewById(R.id.tv_question);
        tv_question.setText(question);
        close = (ImageView) findViewById(R.id.iv_yesno_close);

        btn_no.setOnClickListener(this);
        close.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_yesno_close){
            dismiss();
        }
        if (v.getId() == R.id.btn_no) {
            dismiss();
        }
        dismiss();
    }
}