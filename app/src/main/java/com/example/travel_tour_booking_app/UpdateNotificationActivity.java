package com.example.travel_tour_booking_app;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class UpdateNotificationActivity extends AppCompatActivity {
    Button bt_UpdateNotification;
    EditText et_title, et_date, et_text;
    ImageView iv_thumbnail;
    String ThumbnailURL;
    String Key, oldThumbnailURL;
    CheckBox ckbIsActive;
    Uri uri;
    ContextOrPictureUploadAdapter contextOrPictureUploadAdapter;
    ArrayList<DetailNews> detailNotificationList;

    ArrayList<DetailNews> uploadDetailNotificationList = new ArrayList<>();
    String DetailURL;
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://travel-tour-booking-app-default-rtdb.asia-southeast1.firebasedatabase.app/");
    DatabaseReference databaseReference;
    StorageReference storageReference;
    //Notification chứa nội dung tương ứng với trang được chọn
    Notification notification;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_notification);

        //Xử lý thêm nội dung/hình ảnh
        detailNotificationList = new ArrayList<>();
        contextOrPictureUploadAdapter = new ContextOrPictureUploadAdapter (this,detailNotificationList);

        RecyclerView rv_DetailNotification = findViewById(R.id.rv_update_detail_notification);
        rv_DetailNotification.setLayoutManager(new LinearLayoutManager(this));
        rv_DetailNotification.setAdapter(contextOrPictureUploadAdapter);

        addContentOrPicture();

        //Xử lý update lên Firebase
        bt_UpdateNotification = findViewById(R.id.btn_update_notification);
        et_title = findViewById(R.id.edt_title_update_notification);
        et_date = findViewById(R.id.edt_date_update_notification);
        et_text = findViewById(R.id.edt_text_update_notification);
        iv_thumbnail = findViewById(R.id.iv_thumbnail_update_notification);
        ckbIsActive = findViewById(R.id.ckb_update_isActive);

        //Thiết lập URL cho Thumbnail
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK)
                        {
                            Intent data = result.getData();
                            uri = data.getData();
                            iv_thumbnail.setImageURI(uri);
                        } else {
                            Toast.makeText(UpdateNotificationActivity.this, "No Image Selected", Toast.LENGTH_SHORT);
                        }
                    }
                }
        );

        //Lấy nội dung được gửi vào intent
        notification = (Notification) getIntent().getSerializableExtra("DetailNotificationList");
        if (notification != null){
            et_title.setText(notification.getTitle().toString());
            et_date.setText(notification.getUploadDate().toString());
            et_text.setText(notification.getText().toString());
            oldThumbnailURL = notification.getThumbnail();
            Key = notification.getKey();
            if (oldThumbnailURL!=null){
                Glide.with(this).load(oldThumbnailURL).into(iv_thumbnail);
            }
            detailNotificationList = notification.getDetailNotificationArrayList();
            contextOrPictureUploadAdapter.setDetailNewsList(notification.getDetailNotificationArrayList());
            contextOrPictureUploadAdapter.notifyDataSetChanged();
            if(notification.isActive()){
                ckbIsActive.setChecked(true);
            }
            else ckbIsActive.setChecked(false);
        }

        databaseReference = database.getReference("Android Notification").child(notification.getKey());

        iv_thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });

        //Lắng nghe nút Cập nhật
        bt_UpdateNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YesNoDialog dialog;
                dialog = new YesNoDialog(UpdateNotificationActivity.this,"Bạn có xác nhận cập nhật ?","Có", "Không");
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                dialog.btn_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //contextOrPictureUploadAdapter.notifyDataSetChanged();
                        for (DetailNews item: contextOrPictureUploadAdapter.getDetailNewsList())
                        {
                            if(item.isImage())
                            {
                                saveDetailPicture(Uri.parse(item.getPicture()));
                                uploadDetailNotificationList.add(new DetailNews(DetailURL,item.getSubtitleImage(),null,true));
                            } else {
                                uploadDetailNotificationList.add(item);
                            }
                        }
                        saveData();
                    }
                });

            }
        });
    }
    //Thiết lập URL cho Detail Notification
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode < 10) {
            Uri selectedImageUri = data.getData();

            // Lấy vị trí của mục RecyclerView được chọn từ requestCode
            int position = requestCode;

            // Cập nhật URI của ảnh trong dữ liệu
            DetailNews selectedItem = detailNotificationList.get(position);
            selectedItem.setPicture(selectedImageUri.toString());

            // Cập nhật RecyclerView
            contextOrPictureUploadAdapter.notifyItemChanged(position);
        }
    }
    private void addContentOrPicture() {
        Button btn_add_content = findViewById(R.id.btn_addText_update_notification);
        Button btn_add_picture = findViewById(R.id.btn_addPicture_update_notification);
        btn_add_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailNews temp = new DetailNews(false);
                detailNotificationList.add(temp);
                contextOrPictureUploadAdapter.notifyItemInserted(detailNotificationList.size());
                Toast.makeText(getBaseContext(),"add content",Toast.LENGTH_SHORT).show();
            }
        });
        btn_add_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailNews temp = new DetailNews(true);
                detailNotificationList.add(temp);
                contextOrPictureUploadAdapter.notifyDataSetChanged();
                Toast.makeText(getBaseContext(),"add picture",Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Xử lý save picture của detail notification
    public void saveDetailPicture(Uri TempUri)
    {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                .child("Android Detail Notification Image").child(TempUri.getLastPathSegment());

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(UpdateNotificationActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        android.app.AlertDialog dialog = builder.create();
        dialog.show();


        //Xử lý Url detail picture
        storageReference.putFile(TempUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete());
                Uri urlImage = uriTask.getResult();
                DetailURL = urlImage.toString();
                if (dialog.isShowing())
                    dialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                if (dialog.isShowing())
                    dialog.dismiss();
            }
        });
    }

    //Xử lý picture của thumbnail
    public void saveData()
    {
        if (uri != null){
            StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                    .child("Android Notification Image").child(uri.getLastPathSegment());
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(UpdateNotificationActivity.this);
            builder.setCancelable(false);
            builder.setView(R.layout.progress_layout);
            AlertDialog dialog = builder.create();
            dialog.show();


            //Xử lý Url Thumbnail
            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isComplete());
                    Uri urlImage = uriTask.getResult();
                    ThumbnailURL = urlImage.toString();
                    updateData();
                    dialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                }
            });
        } else{
            ThumbnailURL = oldThumbnailURL;
            updateData();
        }

    }
    public void updateData()
    {
        //Upload Data Firebase
        String title = et_title.getText().toString();
        String text = et_text.getText().toString();
        Boolean isActive = ckbIsActive.isChecked();
        if (title!=null)
        {
            Notification notification = new Notification(title,Notification.getCurrentDate(),text,ThumbnailURL,Key,isActive,uploadDetailNotificationList);
            // Khởi tạo Firebase Realtime Database
            databaseReference.
                    setValue(notification).
                    addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                if (ThumbnailURL != oldThumbnailURL){
                                    StorageReference storageReference1 = FirebaseStorage.getInstance().getReferenceFromUrl(oldThumbnailURL);
                                    storageReference1.delete();
                                }
                                Toast.makeText(UpdateNotificationActivity.this, "Update", Toast.LENGTH_SHORT);
                                finish();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UpdateNotificationActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT);
                        }
                    });
        } else {
            Toast.makeText(this,"Hãy nhập đầy đủ thông tin",Toast.LENGTH_LONG);
        }
    }
    public void GoBack(View view){
        finish();
    }
}