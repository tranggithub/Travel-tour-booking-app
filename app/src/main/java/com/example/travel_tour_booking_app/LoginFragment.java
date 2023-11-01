package com.example.travel_tour_booking_app;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class LoginFragment extends Fragment {

    LoginActivity binding;
    ArrayList<Seclection> selections;
    SelectionAdapter selectionAdapter;
    VideoView videoView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        //Selector
        selections = new ArrayList<>();
        selections.add(Seclection.Facebook);
        selections.add(Seclection.Google);
        selections.add(Seclection.Phone);

        selectionAdapter =new SelectionAdapter(getContext(), R.layout.item_selection,selections);

        GridView SelectionGridview = view.findViewById(R.id.gv_DangNhapKhac);
        SelectionGridview.setAdapter(selectionAdapter);

        videoView = view.findViewById(R.id.vv_Background);
        String videoPath = "android.resource://" + getContext().getPackageName() + "/" + R.raw.ocean;
        Uri uri = Uri.parse(videoPath);

        MediaController mediaController = new MediaController(getContext());
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);

        videoView.setVideoURI(uri);
        videoView.start();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });

        //Xử lý DangNhap button
        DangNhap(view);
        return view;
    }

    public void DangNhap(View view)
    {

        Button button = view.findViewById(R.id.btn_DangNhap);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),HomeActivity.class);
                startActivity(intent);
            }
        });
    }
}