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
import android.widget.Spinner;
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

public class UploadTourActivity extends AppCompatActivity {
    Button bt_UploadTour;
    EditText et_title, et_date,et_location, et_price, et_duration, et_text;
    EditText et_planefrom,et_planeduration, et_segment,et_planedate,et_planebrand,et_timetakeoff,et_timelanding;
    EditText et_cartype;
    ArrayList<TienIch> PlaneTienIch;
    ArrayList<TienIch> CarTienIch;
    Spinner spn_hotel;
    //Plane
    CheckBox ckb_maybaythanrong, ckb_cotivi_plane, ckb_food,ckb_plug;
    //Car
    CheckBox ckb_cotivi_car, ckb_airconditioner, ckb_ghethoaimai;
    CheckBox ckb_isActive;
    ImageView iv_thumbnail;
    String ThumbnailURL;
    Uri uri;
    ContextOrPictureUploadAdapter contextOrPictureUploadAdapter;
    ArrayList<DetailNews> detailSchedule;

    ArrayList<DetailNews> uploadDetailSchedule = new ArrayList<>();
    ArrayList<Hotel> hotels;
    HotelSpinnerAdapter hotelSpinnerAdapter;
    String DetailURL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_tour);
        //Xử lý thêm nội dung/hình ảnh
        detailSchedule = new ArrayList<>();
        contextOrPictureUploadAdapter = new ContextOrPictureUploadAdapter (this,detailSchedule);

        RecyclerView rv_DetailNews = findViewById(R.id.rv_upload_detail_tour);
        rv_DetailNews.setLayoutManager(new LinearLayoutManager(this));
        rv_DetailNews.setAdapter(contextOrPictureUploadAdapter);

        addContentOrPicture();

        //Xử lý upload lên Firebase
        //Tour
        bt_UploadTour = findViewById(R.id.btn_upload_tour);
        et_title = findViewById(R.id.edt_title_upload_tour);
        et_date = findViewById(R.id.edt_date_upload_tour);
        et_text = findViewById(R.id.edt_text_upload_tour);
        et_location =  findViewById(R.id.edt_place_upload_tour);
        et_price = findViewById(R.id.edt_price_upload_tour);
        et_duration = findViewById(R.id.edt_duration_upload_tour);

        //Hotel
        hotels = new ArrayList<>();
        hotels.add(new Hotel(null,"Đường 27, khu phố Wans, Canada","Borahae","7:00","12:00",null,"500 VND","1 tuổi"));
        hotelSpinnerAdapter = new HotelSpinnerAdapter(this,R.layout.item_drop_down_spinner, hotels);

        spn_hotel = findViewById(R.id.spn_hotel_upload_tour);
        spn_hotel.setAdapter(hotelSpinnerAdapter);


        //Plane
        et_planefrom= findViewById(R.id.edt_plan_info_upload_tour);
        et_planeduration= findViewById(R.id.edt_plan_duration_upload_tour);
        et_segment= findViewById(R.id.edt_plan_segment_upload_tour);
        et_planedate= findViewById(R.id.edt_plan_date_upload_tour);
        et_planebrand= findViewById(R.id.edt_plan_date_name_tour);
        et_timetakeoff= findViewById(R.id.edt_plan_start_upload_tour);
        et_timelanding= findViewById(R.id.edt_plan_end_name_tour);
        ckb_maybaythanrong = findViewById(R.id.ckb_plan_upload_tour);
        ckb_cotivi_plane = findViewById(R.id.ckb_tv_upload_tour);
        ckb_food = findViewById(R.id.ckb_food_upload_tour);
        ckb_plug = findViewById(R.id.ckb_electric_upload_tour);

        //Car
        et_cartype = findViewById(R.id.edt_car_type_upload_tour);
        ckb_cotivi_car = findViewById(R.id.ckb_car_tv_upload_tour);
        ckb_airconditioner = findViewById(R.id.ckb_airconditioner_upload_tour);
        ckb_ghethoaimai = findViewById(R.id.ckb_car_sit_upload_tour);

        ckb_isActive = findViewById(R.id.ckb_upload_tour_isActive);
        iv_thumbnail = findViewById(R.id.iv_thumbnail_upload_tour);

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
                            Toast.makeText(UploadTourActivity.this, "No Image Selected", Toast.LENGTH_SHORT);
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
        bt_UploadTour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                contextOrPictureUploadAdapter.notifyDataSetChanged();
                for (DetailNews item: contextOrPictureUploadAdapter.getDetailNewsList())
                {
                    if(item.isImage())
                    {
                        saveDetailPicture(Uri.parse(item.getPicture()));
                        uploadDetailSchedule.add(new DetailNews(DetailURL,item.getSubtitleImage(),null,true));
                    } else {
                        uploadDetailSchedule.add(item);
                    }
                }
                saveData();
            }
        });
    }

    private void addContentOrPicture() {
        Button btn_add_content = findViewById(R.id.btn_addText_upload_tour);
        Button btn_add_picture = findViewById(R.id.btn_addPicture_upload_tour);

        btn_add_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailNews temp = new DetailNews(false);
                detailSchedule.add(temp);
                contextOrPictureUploadAdapter.notifyItemInserted(detailSchedule.size());
                Toast.makeText(getBaseContext(),"add content",Toast.LENGTH_SHORT).show();
            }
        });
        btn_add_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailNews temp = new DetailNews(true);
                detailSchedule.add(temp);
                contextOrPictureUploadAdapter.notifyDataSetChanged();
                Toast.makeText(getBaseContext(),"add picture",Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Xử lý save picture của detail news
    public void saveDetailPicture(Uri TempUri)
    {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                .child("Android Detail Tour Image").child(TempUri.getLastPathSegment());

        AlertDialog.Builder builder = new AlertDialog.Builder(UploadTourActivity.this);
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
                .child("Android Tour Image").child(uri.getLastPathSegment());
        AlertDialog.Builder builder = new AlertDialog.Builder(UploadTourActivity.this);
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
        String Date = et_date.getText().toString();
        String Duration = et_duration.getText().toString();
        String Location = et_location.getText().toString();
        String text = et_text.getText().toString();
        Hotel hotel = (Hotel) spn_hotel.getSelectedItem();
        String diaDiemKhoiHanh = et_planefrom.getText().toString();
        String thoiLuongChuyenBay = et_planeduration.getText().toString();
        int SoChang = Integer.parseInt(et_segment.getText().toString());
        String ngayBay = et_planedate.getText().toString();
        String hangBay = et_planebrand.getText().toString();
        String thoiGianCatCanh = et_timetakeoff.getText().toString();
        String thoiGianHaCanh = et_timelanding.getText().toString();
        PlaneTienIch = new ArrayList<>();
        if(ckb_maybaythanrong.isChecked()){PlaneTienIch.add(TienIch.Plan);}
        if(ckb_cotivi_plane.isChecked()){PlaneTienIch.add(TienIch.Tivi);}
        if(ckb_food.isChecked()){PlaneTienIch.add(TienIch.Food);}
        if(ckb_plug.isChecked()){PlaneTienIch.add(TienIch.Electric);}

        String loaiXe = et_cartype.getText().toString();
        CarTienIch = new ArrayList<>();
        if(ckb_cotivi_car.isChecked()){CarTienIch.add(TienIch.Tivi);}
        if(ckb_airconditioner.isChecked()){CarTienIch.add(TienIch.Conditioner);}
        if(ckb_ghethoaimai.isChecked()){CarTienIch.add(TienIch.Sitting);}

        String Price = et_price.getText().toString();
        boolean isActive = ckb_isActive.isChecked();
        if (title!=null)
        {
            // Khởi tạo Firebase Realtime Database
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://travel-tour-booking-app-default-rtdb.asia-southeast1.firebasedatabase.app/");
            DatabaseReference toursRef = database.getReference("Android Tours");
            String toursId = toursRef.push().getKey();
            Place place = new Place(title,Date,Location,Price,Duration,ThumbnailURL,
                    text,detailSchedule,hotel,diaDiemKhoiHanh,thoiLuongChuyenBay,SoChang,ngayBay,hangBay,
                    thoiGianCatCanh,thoiGianHaCanh,PlaneTienIch,loaiXe,CarTienIch,isActive);
            toursRef.child(toursId).
                    setValue(place).
                    addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(UploadTourActivity.this, "Saved", Toast.LENGTH_SHORT);
                                finish();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UploadTourActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT);
                        }
                    });
        } else {
            Toast.makeText(this,"Hãy nhập đầy đủ thông tin",Toast.LENGTH_LONG);
        }
    }

    public void GoBack (View view){
        finish();
    }
}