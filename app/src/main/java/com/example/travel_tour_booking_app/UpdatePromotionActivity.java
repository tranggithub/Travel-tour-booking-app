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

public class UpdatePromotionActivity extends AppCompatActivity {
    Button bt_UpdatePromotion;
    EditText et_title, et_date_start, et_date_end, et_text;
    ImageView iv_thumbnail;
    String ThumbnailURL;
    String Key, oldThumbnailURL;
    CheckBox ckbIsActive;
    Uri uri;
    ContextOrPictureUploadAdapter contextOrPictureUploadAdapter;
    ArrayList<DetailNews> detailPromtionList;

    ArrayList<DetailNews> uploadDetailPromtionList = new ArrayList<>();
    String DetailURL;
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://travel-tour-booking-app-default-rtdb.asia-southeast1.firebasedatabase.app/");
    DatabaseReference databaseReference;
    StorageReference storageReference;
    //Promtion chứa nội dung tương ứng với trang được chọn
    Promotion promotion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_promotion);

        //Xử lý thêm nội dung/hình ảnh
        detailPromtionList = new ArrayList<>();
        contextOrPictureUploadAdapter = new ContextOrPictureUploadAdapter (this,detailPromtionList);

        RecyclerView rv_DetailPromotion = findViewById(R.id.rv_update_detail_promotion);
        rv_DetailPromotion.setLayoutManager(new LinearLayoutManager(this));
        rv_DetailPromotion.setAdapter(contextOrPictureUploadAdapter);

        addContentOrPicture();

        //Xử lý update lên Firebase
        bt_UpdatePromotion = findViewById(R.id.btn_update_promotion);
        et_title = findViewById(R.id.edt_title_update_promotion);
        et_date_start = findViewById(R.id.edt_date_update_promotion_start);
        et_date_end = findViewById(R.id.edt_date_update_promotion_end);
        et_text = findViewById(R.id.edt_text_update_promotion);
        iv_thumbnail = findViewById(R.id.iv_thumbnail_update_promotion);
        ckbIsActive = findViewById(R.id.ckb_update_promotion_isActive);

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
                            Toast.makeText(UpdatePromotionActivity.this, "No Image Selected", Toast.LENGTH_SHORT);
                        }
                    }
                }
        );

        //Lấy nội dung được gửi vào intent
        promotion = (Promotion) getIntent().getSerializableExtra("DetailPromotion");
        if (promotion != null){
            et_title.setText(promotion.getTitle().toString());
            et_date_start.setText(promotion.getStartDateString().toString());
            et_date_end.setText(promotion.getEndDateString().toString());
            et_text.setText(promotion.getText().toString());
            oldThumbnailURL = promotion.getThumbnail();
            Key = promotion.getKey();
            if (oldThumbnailURL!=null){
                Glide.with(this).load(oldThumbnailURL).into(iv_thumbnail);
            }
            detailPromtionList = promotion.getDetailPromotionList();
            contextOrPictureUploadAdapter.setDetailNewsList(promotion.getDetailPromotionList());
            contextOrPictureUploadAdapter.notifyDataSetChanged();
            if(promotion.isActive()){
                ckbIsActive.setChecked(true);
            }
            else ckbIsActive.setChecked(false);
        }

        databaseReference = database.getReference("Android Promotion").child(promotion.getKey());

        iv_thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });

        //Lắng nghe nút Cập nhật
        bt_UpdatePromotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YesNoDialog dialog;
                dialog = new YesNoDialog(UpdatePromotionActivity.this,"Bạn có xác nhận cập nhật ?","Có", "Không");
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
                                uploadDetailPromtionList.add(new DetailNews(DetailURL,item.getSubtitleImage(),null,true));
                            } else {
                                uploadDetailPromtionList.add(item);
                            }
                        }
                        saveData();
                    }
                });
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
            DetailNews selectedItem = detailPromtionList.get(position);
            selectedItem.setPicture(selectedImageUri.toString());

            // Cập nhật RecyclerView
            contextOrPictureUploadAdapter.notifyItemChanged(position);
        }
    }
    private void addContentOrPicture() {
        Button btn_add_content = findViewById(R.id.btn_addText_update_promotion);
        Button btn_add_picture = findViewById(R.id.btn_addPicture_update_promotion);

        btn_add_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailNews temp = new DetailNews(false);
                detailPromtionList.add(temp);
                contextOrPictureUploadAdapter.notifyItemInserted(detailPromtionList.size());
                Toast.makeText(getBaseContext(),"add content",Toast.LENGTH_SHORT).show();
            }
        });
        btn_add_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailNews temp = new DetailNews(true);
                detailPromtionList.add(temp);
                contextOrPictureUploadAdapter.notifyDataSetChanged();
                Toast.makeText(getBaseContext(),"add picture",Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Xử lý save picture của detail news
    public void saveDetailPicture(Uri TempUri)
    {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                .child("Android Detail Promotion Image").child(TempUri.getLastPathSegment());

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(UpdatePromotionActivity.this);
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
                    .child("Android Promotion Image").child(uri.getLastPathSegment());
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(UpdatePromotionActivity.this);
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
        String date_start = et_date_start.getText().toString();
        String date_end = et_date_end.getText().toString();
        Boolean isActive = ckbIsActive.isChecked();
        if (title!=null)
        {
            Promotion promotion = new Promotion(title,ThumbnailURL,date_start,date_end,text,uploadDetailPromtionList,isActive);
            // Khởi tạo Firebase Realtime Database
            databaseReference.
                    setValue(promotion).
                    addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                if (ThumbnailURL != oldThumbnailURL){
                                    StorageReference storageReference1 = FirebaseStorage.getInstance().getReferenceFromUrl(oldThumbnailURL);
                                    storageReference1.delete();
                                }
                                Toast.makeText(UpdatePromotionActivity.this, "Update", Toast.LENGTH_SHORT);
                                finish();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UpdatePromotionActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT);
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