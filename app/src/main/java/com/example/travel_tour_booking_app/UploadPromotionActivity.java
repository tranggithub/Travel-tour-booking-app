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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class UploadPromotionActivity extends AppCompatActivity {
    Button bt_UploadPromotion;
    EditText et_title, et_start_date, et_end_date, et_text;
    CheckBox ckb_isActive;
    ImageView iv_thumbnail;
    String ThumbnailURL;
    Uri uri;
    ContextOrPictureUploadAdapter contextOrPictureUploadAdapter;
    ArrayList<DetailNews> detailPromotionList;

    ArrayList<DetailNews> uploadDetailNewsList = new ArrayList<>();
    String DetailURL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_promotion);

        //Xử lý thêm nội dung/hình ảnh
        detailPromotionList = new ArrayList<>();
        contextOrPictureUploadAdapter = new ContextOrPictureUploadAdapter (this,detailPromotionList);

        RecyclerView rv_DetailPromotion = findViewById(R.id.rv_upload_detail_promotion);
        rv_DetailPromotion.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false));
        rv_DetailPromotion.setAdapter(contextOrPictureUploadAdapter);

        addContentOrPicture();

        //Xử lý upload lên Firebase
        bt_UploadPromotion = findViewById(R.id.btn_upload_promotion);
        et_title = findViewById(R.id.edt_title_upload_promotion);
        et_start_date = findViewById(R.id.edt_date_upload_promotion_start);
        et_end_date = findViewById(R.id.edt_date_upload_promotion_end);
        et_text = findViewById(R.id.edt_text_upload_promotion);
        iv_thumbnail = findViewById(R.id.iv_thumbnail_upload_promotion);
        ckb_isActive = findViewById(R.id.ckb_upload_promotion_isActive);

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
                            Toast.makeText(UploadPromotionActivity.this, "No Image Selected", Toast.LENGTH_SHORT);
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
        bt_UploadPromotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CheckException()){
                    contextOrPictureUploadAdapter.notifyDataSetChanged();
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
                }
            }
        });
    }

    private boolean CheckException() {
        String title = et_title.getText().toString();
        String text = et_text.getText().toString();
        String dateStart = et_start_date.getText().toString();
        String dateEnd = et_end_date.getText().toString();

        List<String> CheckNullStrList = new ArrayList<>();
        CheckNullStrList.add(title);
        CheckNullStrList.add(text);
        CheckNullStrList.add(dateStart);
        CheckNullStrList.add(dateEnd);

        if(Helper.hasEmptyElement(CheckNullStrList))
        {
            Toast.makeText(getBaseContext(),"Hãy nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!Helper.isValidDate(dateStart) || !Helper.isValidDate(dateEnd)){
            Toast.makeText(getBaseContext(),"Hãy nhập đúng định dạng của ngày", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
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
            DetailNews selectedItem = detailPromotionList.get(position);
            selectedItem.setPicture(selectedImageUri.toString());

            // Cập nhật RecyclerView
            contextOrPictureUploadAdapter.notifyItemChanged(position);
        }
    }
    private void addContentOrPicture() {
        Button btn_add_content = findViewById(R.id.btn_addText_upload_promotion);
        Button btn_add_picture = findViewById(R.id.btn_addPicture_upload_promotion);

        btn_add_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailNews temp = new DetailNews(false);
                detailPromotionList.add(temp);
                contextOrPictureUploadAdapter.notifyItemInserted(detailPromotionList.size());
                Toast.makeText(getBaseContext(),"Thêm nội dung",Toast.LENGTH_SHORT).show();
            }
        });
        btn_add_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailNews temp = new DetailNews(true);
                detailPromotionList.add(temp);
                contextOrPictureUploadAdapter.notifyDataSetChanged();
                Toast.makeText(getBaseContext(),"Thêm hình ảnh",Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Xử lý save picture của detail promotion
    public void saveDetailPicture(Uri TempUri)
    {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                .child("Android Detail Promotion Image").child(TempUri.getLastPathSegment());

        AlertDialog.Builder builder = new AlertDialog.Builder(UploadPromotionActivity.this);
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
                .child("Android Promotion Image").child(uri.getLastPathSegment());
        AlertDialog.Builder builder = new AlertDialog.Builder(UploadPromotionActivity.this);
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
    public void uploadData() {
        //Upload Data Firebase
        String title = et_title.getText().toString();
        String text = et_text.getText().toString();
        String dateStart = et_start_date.getText().toString();
        String dateEnd = et_end_date.getText().toString();
        if (title != null && text != null && dateStart != null && dateEnd != null) {
            Promotion promotion = new Promotion(title,ThumbnailURL,dateStart,dateEnd,text,uploadDetailNewsList,ckb_isActive.isChecked());
            // Khởi tạo Firebase Realtime Database
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://travel-tour-booking-app-default-rtdb.asia-southeast1.firebasedatabase.app/");
            DatabaseReference promotionRef = database.getReference("Android Promotion");
            String promotionId = promotionRef.push().getKey();
            promotionRef.child(promotionId).
                    setValue(promotion).
                    addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(UploadPromotionActivity.this, "Saved", Toast.LENGTH_SHORT);
                                finish();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UploadPromotionActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT);
                        }
                    });
        } else {
            Toast.makeText(this, "Hãy nhập đầy đủ thông tin", Toast.LENGTH_LONG);
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private static boolean isValidDate(String date) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate.parse(date, formatter);
            return true;
        } catch (DateTimeParseException e) {
            Log.e("Upload Promotion",e.getMessage().toString());
            return false;
        }
    }
}
