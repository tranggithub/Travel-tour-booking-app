package com.example.travel_tour_booking_app;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class UpdateHotelActivity extends AppCompatActivity {
    Button bt_UploadHotel;
    EditText et_title, et_address, et_text, et_check_in, et_check_out, et_age_free, et_age_addition_fee, et_addition_fee;
    RadioGroup rg_breakfast;
    CheckBox ckb_wifi, ckb_airconditioner,ckb_bed,ckb_bathtub,ckb_tv,ckb_carpark,ckb_exercise,ckb_wine,ckb_isActive;
    ImageView iv_thumbnail;
    String ThumbnailURL, oldThumbnailURL;
    Uri uri;
    ContextOrPictureUploadAdapter contextOrPictureUploadAdapter;
    ArrayList<DetailNews> detailNewsList;

    ArrayList<DetailNews> uploadOtherPictureURL;
    ArrayList<String> newOtherPictureUrl, oldOtherPictureUrl;
    int check;
    Hotel hotel;
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_hotel);

        initID();

        hotel = (Hotel) getIntent().getSerializableExtra("Hotel", Hotel.class);
        if(hotel!=null){
            for (DetailNews item: hotel.getDetailPictureList()){
                oldOtherPictureUrl.add(item.getPicture());
            }
            et_title.setText(hotel.getName());
            et_address.setText(hotel.getAddress());
            et_text.setText(hotel.getIntroduction());
            et_check_in.setText(hotel.getTimeCheckIn());
            et_check_out.setText(hotel.getTimeCheckOut());
            et_age_free.setText(hotel.getChildrenAgeFree());
            et_age_addition_fee.setText(hotel.getChildrenAgeAdditionFee());
            et_addition_fee.setText(hotel.getChildenFee());

            int radioButtonCount = rg_breakfast.getChildCount();
            for (int i = 0; i < radioButtonCount; i++) {
                View view = rg_breakfast.getChildAt(i);
                if (view instanceof RadioButton) {
                    RadioButton radioButton = (RadioButton) view;
                    if (radioButton.getText().toString().equals(hotel.getBreakfast())) {
                        radioButton.setChecked(true);
                        break; // Nếu bạn chỉ muốn thiết lập một RadioButton, bạn có thể thoát vòng lặp sau khi thiết lập
                    }
                }
            }

            if(hotel.getTienNghiChungs().contains(TienNghiChung.Wifi))
                ckb_wifi.setChecked(true);
            if(hotel.getTienNghiChungs().contains(TienNghiChung.Conditioner))
                ckb_airconditioner.setChecked(true);
            if(hotel.getTienNghiChungs().contains(TienNghiChung.Bed))
                ckb_bed.setChecked(true);
            if(hotel.getTienNghiChungs().contains(TienNghiChung.Bathtub))
                ckb_bathtub.setChecked(true);
            if(hotel.getTienNghiChungs().contains(TienNghiChung.TV))
                ckb_tv.setChecked(true);
            if(hotel.getTienNghiChungs().contains(TienNghiChung.Parking))
                ckb_carpark.setChecked(true);
            if(hotel.getTienNghiChungs().contains(TienNghiChung.Exercise))
                ckb_exercise.setChecked(true);
            if(hotel.getTienNghiChungs().contains(TienNghiChung.Wine))
                ckb_wine.setChecked(true);
            oldThumbnailURL = hotel.getThumbnail();
            Glide.with(this).load(hotel.getThumbnail()).into(iv_thumbnail);

            contextOrPictureUploadAdapter.setDetailNewsList(hotel.getDetailPictureList());
        }

        addContentOrPicture();
        ThumbnailListener();
        update();

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
    private void initID(){
        bt_UploadHotel = findViewById(R.id.btn_upload_hotel);

        et_title = findViewById(R.id.edt_title_upload_hotel);
        et_address = findViewById(R.id.edt_address_hotel);
        et_text = findViewById(R.id.edt_introduction_hotel);
        et_check_in = findViewById(R.id.edt_check_in_time_upload_hotel);
        et_check_out = findViewById(R.id.edt_check_out_time_hotel);
        et_age_free = findViewById(R.id.edt_free_bed_age_upload_hotel);
        et_age_addition_fee = findViewById(R.id.edt_addition_fee_bed_age_hotel);
        et_addition_fee = findViewById(R.id.edt_addition_fee_hotel);

        rg_breakfast = findViewById(R.id.rg_upload_hotel);

        ckb_wifi = findViewById(R.id.ckb_wifi_upload_hotel);
        ckb_airconditioner = findViewById(R.id.ckb_airconditioner_upload_hotel);
        ckb_bed = findViewById(R.id.ckb_bed_upload_hotel);
        ckb_bathtub = findViewById(R.id.ckb_bathtub_upload_hotel);
        ckb_tv = findViewById(R.id.ckb_tv_upload_hotel);
        ckb_carpark = findViewById(R.id.ckb_carpark_upload_hotel);
        ckb_exercise = findViewById(R.id.ckb_exercise_upload_hotel);
        ckb_wine = findViewById(R.id.ckb_wine_upload_hotel);
        ckb_isActive = findViewById(R.id.ckb_upload_hotel_isActive);
        iv_thumbnail = findViewById(R.id.iv_thumbnail_upload_hotel);

        //Xử lý thêm nội dung/hình ảnh
        detailNewsList = new ArrayList<>();
        contextOrPictureUploadAdapter = new ContextOrPictureUploadAdapter (this,detailNewsList);

        RecyclerView rv_DetailNews = findViewById(R.id.rv_upload_detail_hotel);
        rv_DetailNews.setLayoutManager(new LinearLayoutManager(this));
        rv_DetailNews.setAdapter(contextOrPictureUploadAdapter);

        uploadOtherPictureURL = new ArrayList<>();
        newOtherPictureUrl = new ArrayList<>();
        oldOtherPictureUrl = new ArrayList<>();
    }
    private void addContentOrPicture() {
        detailNewsList = contextOrPictureUploadAdapter.getDetailNewsList();
        Button btn_add_content = findViewById(R.id.btn_addText_upload_hotel);
        Button btn_add_picture = findViewById(R.id.btn_addPicture_upload_hotel);

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
                contextOrPictureUploadAdapter.notifyItemInserted(detailNewsList.size());
                Toast.makeText(getBaseContext(),"add picture",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void ThumbnailListener(){
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
                            Toast.makeText(UpdateHotelActivity.this, "No Image Selected", Toast.LENGTH_SHORT);
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
    }
    private void update(){
        //Lắng nghe nút Tải lên
        bt_UploadHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                contextOrPictureUploadAdapter.notifyDataSetChanged();
                for (DetailNews item: contextOrPictureUploadAdapter.getDetailNewsList())
                {
                    if(item.isImage())
                    {
                        saveDetailPicture(Uri.parse(item.getPicture()));
                    } else {
                        uploadOtherPictureURL.add(item);
                    }
                }
                Log.e("Check",check+"");
            }
        });
    }
    //Xử lý save picture của detail hotel
    public void saveDetailPicture(Uri TempUri)
    {
        if(!TempUri.toString().contains("https://")) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                    .child("Android Detail Hotel Image").child(TempUri.getLastPathSegment() + System.currentTimeMillis());

            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateHotelActivity.this);
            builder.setCancelable(false);
            builder.setView(R.layout.progress_layout);
            AlertDialog dialog = builder.create();
            dialog.show();

            Log.e("path", TempUri.getLastPathSegment() + System.currentTimeMillis());

            //        storageReference.putFile(TempUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            //            @Override
            //            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
            //                if(task.isSuccessful()){
            //                    storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            //                        @Override
            //                        public void onComplete(@NonNull Task<Uri> uriTask) {
            //                            if(uriTask.isSuccessful()){
            //                                String imageURL = uriTask.getResult().toString();
            //                                uploadOtherPictureURL.add(imageURL);
            //                            }
            //                            else {
            //                                Log.e("Upload", "Upload failed: " + task.getException().getMessage());
            //                            }
            //                        }
            //                    });
            //                }
            //            }
            //        });

            //Xử lý Url detail picture
            storageReference.putFile(TempUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isComplete()) ;
                    Uri urlImage = uriTask.getResult();
                    uploadOtherPictureURL.add(new DetailNews(urlImage.toString(), null, null, true));
                    newOtherPictureUrl.add(urlImage.toString());
                    Log.e("PictureURL", urlImage.toString());
                    check = check + 1;
                    saveData();
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
        else {
            uploadOtherPictureURL.add(new DetailNews(TempUri.toString(), null, null, true));
            check = check + 1;
            newOtherPictureUrl.add(TempUri.toString());
            saveData();
        }
    }
    //Xử lý picture của thumbnail
    public void saveData()
    {
        if (check!=contextOrPictureUploadAdapter.getItemCount())
            return;
        if(uri!=null) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                    .child("Android Hotel Image").child(uri.getLastPathSegment());
            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateHotelActivity.this);
            builder.setCancelable(false);
            builder.setView(R.layout.progress_layout);
            AlertDialog dialog = builder.create();
            dialog.show();


            //Xử lý Url Thumbnail
            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isComplete()) ;
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
        } else {
            ThumbnailURL = oldThumbnailURL;
            updateData();
        }
    }
    public void updateData()
    {
        //Upload Data Firebase
        String title = et_title.getText().toString();
        String address = et_address.getText().toString();
        String text = et_text.getText().toString();
        String check_in = et_check_in.getText().toString();
        String check_out = et_check_out.getText().toString();
        String age_free = et_age_free.getText().toString();
        String age_addition_fee = et_age_addition_fee.getText().toString();
        String addition_fee = et_addition_fee.getText().toString();

        RadioButton selectedRadioButton = findViewById(rg_breakfast.getCheckedRadioButtonId());
        String breakfast = selectedRadioButton.getText().toString();

        ArrayList<TienNghiChung> tienNghiChungs = new ArrayList<>();

        if (ckb_wifi.isChecked()){
            tienNghiChungs.add(TienNghiChung.Wifi);
        }
        if(ckb_airconditioner.isChecked()){
            tienNghiChungs.add(TienNghiChung.Conditioner);
        }
        if (ckb_bed.isChecked()){
            tienNghiChungs.add(TienNghiChung.Bed);
        }
        if (ckb_bathtub.isChecked()){
            tienNghiChungs.add(TienNghiChung.Bathtub);
        }
        if(ckb_tv.isChecked()){
            tienNghiChungs.add(TienNghiChung.TV);
        }
        if (ckb_carpark.isChecked()){
            tienNghiChungs.add(TienNghiChung.Parking);
        }
        if(ckb_exercise.isChecked()){
            tienNghiChungs.add(TienNghiChung.Exercise);
        }
        if(ckb_wine.isChecked()){
            tienNghiChungs.add(TienNghiChung.Wine);
        }
        Log.e("uploadOtherPictureURL",uploadOtherPictureURL.size()+"");

        boolean isActive = ckb_isActive.isChecked();
        if (title!=null)
        {
            // Khởi tạo Firebase Realtime Database
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://travel-tour-booking-app-default-rtdb.asia-southeast1.firebasedatabase.app/");
            DatabaseReference hotelRef = database.getReference("Android Hotel");
            Hotel tempHotel = new Hotel(ThumbnailURL,address,title,text,check_in,check_out,tienNghiChungs,addition_fee,age_free,age_addition_fee,breakfast,uploadOtherPictureURL);
            hotelRef.child(hotel.getKey()).
                    setValue(tempHotel).
                    addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                if (ThumbnailURL != oldThumbnailURL){
                                    StorageReference storageReference1 = FirebaseStorage.getInstance().getReferenceFromUrl(oldThumbnailURL);
                                    storageReference1.delete();
                                }
                                for(String item:oldOtherPictureUrl){
                                    if(!newOtherPictureUrl.contains(item)){
                                        StorageReference storageReference1 = FirebaseStorage.getInstance().getReferenceFromUrl(item);
                                        storageReference1.delete();
                                    }
                                }
                                Toast.makeText(UpdateHotelActivity.this, "Saved", Toast.LENGTH_SHORT);
                                finish();
                                Intent intent = new Intent(UpdateHotelActivity.this,ListHotelAdminActivity.class);
                                startActivity(intent);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UpdateHotelActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT);
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