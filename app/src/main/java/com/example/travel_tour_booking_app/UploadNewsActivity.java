package com.example.travel_tour_booking_app;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UploadNewsActivity extends AppCompatActivity {
    Button bt_UploadNews;
    EditText et_title, et_date, et_text;
    ImageView iv_thumbnail;
    String ThumbnailURL;
    Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_news);

        bt_UploadNews = findViewById(R.id.btn_upload_news);
        et_title = findViewById(R.id.edt_title_upload_news);
        et_date = findViewById(R.id.edt_date_upload_news);
        et_text = findViewById(R.id.edt_text_upload_news);
        iv_thumbnail = findViewById(R.id.iv_thumbnail_upload_news);

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
                            Toast.makeText(UploadNewsActivity.this, "No Image Selected", Toast.LENGTH_SHORT);
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

        bt_UploadNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
                //uploadData();
            }
        });
    }

    public void saveData()
    {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                .child("Android News Image").child(uri.getLastPathSegment());
        AlertDialog.Builder builder = new AlertDialog.Builder(UploadNewsActivity.this);
        builder.setCancelable(true);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

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
         String title = et_title.getText().toString();
         String date = et_date.getText().toString();
         String text = et_text.getText().toString();

         SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
         Date date_real = null;

         try {
             date_real = dateFormat.parse(date); // Chuyển đổi chuỗi thành kiểu Date
         } catch (ParseException e) {
             e.printStackTrace();
         }

         if (date == null) {
             Toast.makeText(UploadNewsActivity.this, "Invalid Date", Toast.LENGTH_SHORT); // In ra kết quả dưới dạng kiểu Date
         } else {
             News news = new News(title, date, text, ThumbnailURL);

             FirebaseDatabase.getInstance("https://travel-tour-booking-app-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Android News").child(title).
                     setValue(news).
                     addOnCompleteListener(new OnCompleteListener<Void>() {
                         @Override
                         public void onComplete(@NonNull Task<Void> task) {
                             if (task.isSuccessful()){
                                 Toast.makeText(UploadNewsActivity.this, "Saved", Toast.LENGTH_SHORT);
                                 finish();
                             }
                         }
                     }).addOnFailureListener(new OnFailureListener() {
                         @Override
                         public void onFailure(@NonNull Exception e) {
                             Toast.makeText(UploadNewsActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT);
                         }
                     });
         }
     }
}