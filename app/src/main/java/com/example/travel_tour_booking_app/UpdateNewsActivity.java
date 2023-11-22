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

public class UpdateNewsActivity extends AppCompatActivity {
    Button bt_UpdateNews;
    EditText et_title, et_date, et_text;
    ImageView iv_thumbnail;
    String ThumbnailURL;
    String Key, oldThumbnailURL;
    CheckBox ckbIsActive;
    Uri uri;
    ContextOrPictureUploadAdapter contextOrPictureUploadAdapter;
    ArrayList<DetailNews> detailNewsList;

    ArrayList<DetailNews> uploadDetailNewsList = new ArrayList<>();
    String DetailURL;
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://travel-tour-booking-app-default-rtdb.asia-southeast1.firebasedatabase.app/");
    DatabaseReference databaseReference;
    StorageReference storageReference;
    //News chứa nội dung tương ứng với trang được chọn
    News news;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_news);

        //Xử lý thêm nội dung/hình ảnh
        detailNewsList = new ArrayList<>();
        contextOrPictureUploadAdapter = new ContextOrPictureUploadAdapter (this,detailNewsList);

        RecyclerView rv_DetailNews = findViewById(R.id.rv_update_detail_news);
        rv_DetailNews.setLayoutManager(new LinearLayoutManager(this));
        rv_DetailNews.setAdapter(contextOrPictureUploadAdapter);

        addContentOrPicture();

        //Xử lý update lên Firebase
        bt_UpdateNews = findViewById(R.id.btn_update_news);
        et_title = findViewById(R.id.edt_title_update_news);
        et_date = findViewById(R.id.edt_date_update_news);
        et_text = findViewById(R.id.edt_text_update_news);
        iv_thumbnail = findViewById(R.id.iv_thumbnail_update_news);
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
                            Toast.makeText(UpdateNewsActivity.this, "No Image Selected", Toast.LENGTH_SHORT);
                        }
                    }
                }
        );

        //Lấy nội dung được gửi vào intent
        news = (News) getIntent().getSerializableExtra("DetailNewsList");
        if (news != null){
            et_title.setText(news.getTitle().toString());
            et_date.setText(news.getUploadDate().toString());
            et_text.setText(news.getText().toString());
            oldThumbnailURL = news.getThumbnail();
            Key = news.getKey();
            if (oldThumbnailURL!=null){
                Glide.with(this).load(oldThumbnailURL).into(iv_thumbnail);
            }
            detailNewsList = news.getDetailNewsArrayList();
            contextOrPictureUploadAdapter.setDetailNewsList(news.getDetailNewsArrayList());
            contextOrPictureUploadAdapter.notifyDataSetChanged();
            if(news.isActive()){
                ckbIsActive.setChecked(true);
            }
            else ckbIsActive.setChecked(false);
        }

        databaseReference = database.getReference("Android News").child(news.getKey());

        iv_thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });

        //Lắng nghe nút Cập nhật
        bt_UpdateNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //contextOrPictureUploadAdapter.notifyDataSetChanged();
                for (DetailNews item: contextOrPictureUploadAdapter.getDetailNewsList())
                {
                    if(item.isImage())
                    {
                        saveDetailPicture(Uri.parse(item.getPicture()));
                        uploadDetailNewsList.add(new DetailNews(DetailURL,item.getSubtitleImage(),null,true));
                    } else {
                        uploadDetailNewsList.add(item);
                    }
                }
                saveData();
                Intent intent = new Intent(UpdateNewsActivity.this, ListNewsAdminActivity.class);
                startActivity(intent);
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
            DetailNews selectedItem = detailNewsList.get(position);
            selectedItem.setPicture(selectedImageUri.toString());

            // Cập nhật RecyclerView
            contextOrPictureUploadAdapter.notifyItemChanged(position);
        }
    }
    private void addContentOrPicture() {
        Button btn_add_content = findViewById(R.id.btn_addText_update_news);
        Button btn_add_picture = findViewById(R.id.btn_addPicture_update_news);

        btn_add_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailNews temp = new DetailNews(false);
                detailNewsList.add(temp);
                contextOrPictureUploadAdapter.notifyItemInserted(detailNewsList.size());
                Toast.makeText(getBaseContext(),"add content",Toast.LENGTH_SHORT).show();
            }
        });
        btn_add_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailNews temp = new DetailNews(true);
                detailNewsList.add(temp);
                contextOrPictureUploadAdapter.notifyDataSetChanged();
                Toast.makeText(getBaseContext(),"add picture",Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Xử lý save picture của detail news
    public void saveDetailPicture(Uri TempUri)
    {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                .child("Android Detail News Image").child(TempUri.getLastPathSegment());

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(UpdateNewsActivity.this);
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
                    .child("Android News Image").child(uri.getLastPathSegment());
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(UpdateNewsActivity.this);
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
            News news = new News(title,News.getCurrentDate(),text,ThumbnailURL,Key,isActive,uploadDetailNewsList);
            // Khởi tạo Firebase Realtime Database
            databaseReference.
                    setValue(news).
                    addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                if (ThumbnailURL != oldThumbnailURL){
                                    StorageReference storageReference1 = FirebaseStorage.getInstance().getReferenceFromUrl(oldThumbnailURL);
                                    storageReference1.delete();
                                }
                                Toast.makeText(UpdateNewsActivity.this, "Update", Toast.LENGTH_SHORT);
                                finish();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UpdateNewsActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT);
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