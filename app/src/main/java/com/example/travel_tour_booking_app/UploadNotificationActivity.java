package com.example.travel_tour_booking_app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class UploadNotificationActivity extends AppCompatActivity {
    Button bt_UploadNotification;
    EditText et_title, et_date, et_text;
    CheckBox ckb_isActive;
    ImageView iv_thumbnail;
    String ThumbnailURL;
    Uri uri;
    ContextOrPictureUploadAdapter contextOrPictureUploadAdapter;
    ArrayList<DetailNews> detailNotificationList;

    ArrayList<DetailNews> uploadDetailNotificationList = new ArrayList<>();
    String DetailURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_notification);
        //Xử lý thêm nội dung/hình ảnh
        detailNotificationList = new ArrayList<>();
        contextOrPictureUploadAdapter = new ContextOrPictureUploadAdapter (this,detailNotificationList);

        RecyclerView rv_DetailNotification = findViewById(R.id.rv_upload_detail_notification);
        rv_DetailNotification.setLayoutManager(new LinearLayoutManager(this));
        rv_DetailNotification.setAdapter(contextOrPictureUploadAdapter);

        addContentOrPicture();

        //Xử lý upload lên Firebase
        bt_UploadNotification = findViewById(R.id.btn_upload_notification);
        et_title = findViewById(R.id.edt_title_upload_notification);
        et_date = findViewById(R.id.edt_date_upload_notification);
        et_text = findViewById(R.id.edt_text_upload_notification);
        ckb_isActive = findViewById(R.id.ckb_upload_isActive);
        iv_thumbnail = findViewById(R.id.iv_thumbnail_upload_notification);

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
                            Toast.makeText(UploadNotificationActivity.this, "No Image Selected", Toast.LENGTH_SHORT);
                        }
                    }
                }
        );


        iv_thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });

        //Lắng nghe nút Tải lên
        bt_UploadNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                contextOrPictureUploadAdapter.notifyDataSetChanged();
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
    //Thiết lập URL cho Detail News
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
        Button btn_add_content = findViewById(R.id.btn_addText_upload_notification);
        Button btn_add_picture = findViewById(R.id.btn_addPicture_upload_notification);

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

    //Xử lý save picture của detail news
    public void saveDetailPicture(Uri TempUri)
    {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                .child("Android Detail Notification Image").child(TempUri.getLastPathSegment());

        AlertDialog.Builder builder = new AlertDialog.Builder(UploadNotificationActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
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
        StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                .child("Android Notification Image").child(uri.getLastPathSegment());
        AlertDialog.Builder builder = new AlertDialog.Builder(UploadNotificationActivity.this);
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
                uploadData();
                dialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
            }
        });
    }
    public void uploadData()
    {
        //Upload Data Firebase
        String title = et_title.getText().toString();
        String text = et_text.getText().toString();
        boolean isActive = ckb_isActive.isChecked();
        if (title!=null)
        {
            // Khởi tạo Firebase Realtime Database
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://travel-tour-booking-app-default-rtdb.asia-southeast1.firebasedatabase.app/");
            DatabaseReference notificationRef = database.getReference("Android Notification");
            String notificationId = notificationRef.push().getKey();
            Notification notification = new Notification(title, Notification.getCurrentDate(),text,ThumbnailURL,notificationId,isActive,uploadDetailNotificationList);
            notificationRef.child(notificationId).
                    setValue(notification).
                    addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(UploadNotificationActivity.this, "Saved", Toast.LENGTH_SHORT);
                                finish();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UploadNotificationActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT);
                        }
                    });
        } else {
            Toast.makeText(this,"Hãy nhập đầy đủ thông tin",Toast.LENGTH_LONG);
        }
    }

    public void GoBack (View view){
        Intent intent = new Intent(this,ListNotificationAdminActivity.class);
        startActivity(intent);
        finish();
    }
}
