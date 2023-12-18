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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

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

public class UploadHotelActivity extends AppCompatActivity {
    Button bt_UploadHotel;
    EditText et_title, et_address, et_text, et_check_in, et_check_out, et_age_free, et_age_addition_fee, et_addition_fee;
    RadioGroup rg_breakfast;
    CheckBox ckb_wifi, ckb_airconditioner,ckb_bed,ckb_bathtub,ckb_tv,ckb_carpark,ckb_exercise,ckb_wine,ckb_isActive;
    ImageView iv_thumbnail;
    String ThumbnailURL;
    Uri uri;
    ContextOrPictureUploadAdapter contextOrPictureUploadAdapter;
    ArrayList<DetailNews> detailNewsList;

    ArrayList<DetailNews> uploadOtherPictureURL;
    int check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_hotel);

        //Khởi tạo
        initID();
        uploadOtherPictureURL = new ArrayList<>();
        check = 0;

        //Xử lý thêm nội dung/hình ảnh
        detailNewsList = new ArrayList<>();
        contextOrPictureUploadAdapter = new ContextOrPictureUploadAdapter (this,detailNewsList);

        RecyclerView rv_DetailNews = findViewById(R.id.rv_upload_detail_hotel);
        rv_DetailNews.setLayoutManager(new LinearLayoutManager(this));
        rv_DetailNews.setAdapter(contextOrPictureUploadAdapter);

        addContentOrPicture();




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
                            Toast.makeText(UploadHotelActivity.this, "No Image Selected", Toast.LENGTH_SHORT);
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
                contextOrPictureUploadAdapter.notifyDataSetChanged();
                Toast.makeText(getBaseContext(),"add picture",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void initID(){
        bt_UploadHotel = findViewById(R.id.btn_upload_hotel);

        et_title = findViewById(R.id.edt_title_upload_hotel);
        et_address = findViewById(R.id.edt_address_hotel);
        et_text = findViewById(R.id.edt_text_upload_hotel);
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
    }

    //Xử lý save picture của detail hotel
    public void saveDetailPicture(Uri TempUri)
    {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                .child("Android Detail Hotel Image").child(TempUri.getLastPathSegment()+System.currentTimeMillis());

        AlertDialog.Builder builder = new AlertDialog.Builder(UploadHotelActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        Log.e("path", TempUri.getLastPathSegment()+System.currentTimeMillis());

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
                while (!uriTask.isComplete());
                Uri urlImage = uriTask.getResult();
                uploadOtherPictureURL.add(new DetailNews(urlImage.toString(),null,null,true));
                Log.e("PictureURL",urlImage.toString());
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

    //Xử lý picture của thumbnail
    public void saveData()
    {
        if (check!=contextOrPictureUploadAdapter.getItemCount())
            return;
        StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                .child("Android Hotel Image").child(uri.getLastPathSegment());
        AlertDialog.Builder builder = new AlertDialog.Builder(UploadHotelActivity.this);
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
            String hotelId = hotelRef.push().getKey();
            Hotel hotel = new Hotel(ThumbnailURL,address,title,text,check_in,check_out,tienNghiChungs,addition_fee,age_free,age_addition_fee,breakfast,uploadOtherPictureURL);
            hotelRef.child(hotelId).
                    setValue(hotel).
                    addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(UploadHotelActivity.this, "Saved", Toast.LENGTH_SHORT);
                                finish();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UploadHotelActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT);
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