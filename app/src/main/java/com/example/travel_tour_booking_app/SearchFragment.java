package com.example.travel_tour_booking_app;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;


public class SearchFragment extends Fragment {

    SeekBar sb_price;
    TextView tv_price;
    Button btn_search;
    EditText et_appointment, et_country, et_date;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        // Inflate the layout for this fragment

        //Thiết lập ID
        initID(view);

        //Xử lý thanh chi phí cao nhất
        handlePriceSeekBar();

        //Tìm kiếm
        handleButtonSearch();
        return view;
    }

    private void initID(View view){
        sb_price = view.findViewById(R.id.seekBar);
        tv_price = view.findViewById(R.id.tv_search_price);
        btn_search = view.findViewById(R.id.btn_search_search);

        et_appointment = view.findViewById(R.id.et_diemden);
        et_country = view.findViewById(R.id.et_tp);
        et_date = view.findViewById(R.id.et_ngaydi);
    }
    private void handlePriceSeekBar(){
        // Đặt giá trị mặc định
        updatePrice(0);

        // Thiết lập sự kiện lắng nghe cho SeekBar
        sb_price.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Update giá trị khi người dùng kéo SeekBar
                updatePrice(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Không cần thực hiện gì khi bắt đầu kéo
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Không cần thực hiện gì khi kết thúc kéo
            }
        });
    }

    private void updatePrice(int progress) {
        String priceText;
        if(progress == 100){
            priceText = "Không giới hạn";
        } else priceText = progress+".000.000 VND";

        // Hiển thị giá trị vào TextView
        tv_price.setText(priceText);
    }
    private void handleButtonSearch(){
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),ListTourActivity.class);
                intent.putExtra("Place", et_country.getText().toString());
                intent.putExtra("Appointment", et_appointment.getText().toString());
                intent.putExtra("Date", et_date.getText().toString());
                intent.putExtra("Price",tv_price.getText().toString());
                startActivity(intent);
            }
        });
    }
}