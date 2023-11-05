package com.example.travel_tour_booking_app;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class LoginFragment extends Fragment {

    LoginActivity binding;
    ArrayList<Seclection> selections;
    SelectionAdapter selectionAdapter;
    VideoView videoView;

    EditText edtMail, edtPassword;
    //FirebaseAuth
    FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getActivity(),HomeActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        mAuth = FirebaseAuth.getInstance();

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

        edtMail = view.findViewById(R.id.edt_Email);
        edtPassword = view.findViewById(R.id.edt_Psw);

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

                String email, psw;
                email = String.valueOf(edtMail.getText());
                psw = String.valueOf(edtPassword.getText());

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(LoginFragment.this.getContext(), "Nhập email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(psw)){
                    Toast.makeText(LoginFragment.this.getContext(), "Nhập password", Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.signInWithEmailAndPassword(email, psw)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getActivity(), "Đăng nhập thành công.",
                                            Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getActivity(),HomeActivity.class);
                                    startActivity(intent);
                                    getActivity().finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(LoginFragment.this.getContext(), "Đăng nhập thất bại.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}