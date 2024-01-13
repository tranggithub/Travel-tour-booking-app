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
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class UpdateTourActivity extends AppCompatActivity {
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

    DatabaseReference databaseReference;
    String DatabaseUrl = "https://travel-tour-booking-app-default-rtdb.asia-southeast1.firebasedatabase.app";
    Place tour;
    String Key, oldThumbnailURL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_tour);

        initID();

        //Lấy nội dung được gửi vào intent
        tour = (Place) getIntent().getSerializableExtra("Tour");
        if (tour != null) {
            et_title.setText(tour.getTitle());
            et_date.setText(tour.getDate());
            et_text.setText(tour.getText());
            et_location.setText(tour.getLocation());
            et_price.setText(tour.getPrice());
            et_duration.setText(tour.getDuration());

            //Hotel
            databaseReference = FirebaseDatabase.getInstance(DatabaseUrl).getReference("Android Hotel");
            //Progress layout
            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setView(R.layout.progress_layout);
            androidx.appcompat.app.AlertDialog alertDialog = builder.create();
            alertDialog.show();

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    hotels.clear();
                    int position = -1;
                    for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                        Hotel tempHotel = itemSnapshot.getValue(Hotel.class);
                        tempHotel.setKey(itemSnapshot.getKey());
                        hotels.add(tempHotel);
                        if (tempHotel.getKey().contains(tour.getHotel())) {
                            position = hotels.size() - 1;
                        }
                    }
                    hotelSpinnerAdapter.notifyDataSetChanged();
                    if (position != -1)
                        spn_hotel.setSelection(position);
                    alertDialog.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    alertDialog.dismiss();
                }
            });


            //Plane
            et_planefrom.setText(tour.getPlaneFrom());
            et_planeduration.setText(tour.getPlaneDuration());
            et_segment.setText(tour.getNumberOfSegment()+"");
            et_planedate.setText(tour.getPlaneDate());
            et_planebrand.setText(tour.getPlaneBrand());
            et_timetakeoff.setText(tour.getTimeTakeOff());
            et_timelanding.setText(tour.getTimeLanding());
            ArrayList<TienIch> PlaneTienIch = tour.getPlaneTienIch();
            if (PlaneTienIch.contains(TienIch.Plan))
                ckb_maybaythanrong.setChecked(true);
            if (PlaneTienIch.contains(TienIch.Tivi))
                ckb_cotivi_plane.setChecked(true);
            if (PlaneTienIch.contains(TienIch.Food))
                ckb_food.setChecked(true);
            if (PlaneTienIch.contains(TienIch.Electric))
                ckb_plug.setChecked(true);

            //Car
            et_cartype.setText(tour.getCarType());
            ArrayList<TienIch> CarTienIch = tour.getCarTienIch();
            if (CarTienIch.contains(TienIch.Tivi))
                ckb_cotivi_car.setChecked(true);
            if (CarTienIch.contains(TienIch.Conditioner))
                ckb_airconditioner.setChecked(true);
            if (CarTienIch.contains(TienIch.Sitting))
                ckb_ghethoaimai.setChecked(true);

            if (!tour.isActive())
                ckb_isActive.setChecked(false);
            oldThumbnailURL = tour.getThumbnail_Image();
            Key = tour.getKey();
            if (oldThumbnailURL != null) {
                Glide.with(this).load(oldThumbnailURL).into(iv_thumbnail);
            }

            contextOrPictureUploadAdapter.setDetailNewsList(tour.getSchedule());
            contextOrPictureUploadAdapter.notifyDataSetChanged();
        }
        addContentOrPicture();
        UploadImage();
        update();
    }
    private void initID(){
        //Xử lý thêm nội dung/hình ảnh
        detailSchedule = new ArrayList<>();
        contextOrPictureUploadAdapter = new ContextOrPictureUploadAdapter (this,detailSchedule);

        RecyclerView rv_DetailNews = findViewById(R.id.rv_upload_detail_tour);
        rv_DetailNews.setLayoutManager(new LinearLayoutManager(this));
        rv_DetailNews.setAdapter(contextOrPictureUploadAdapter);

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
        hotelSpinnerAdapter = new HotelSpinnerAdapter(this,R.layout.item_drop_down_spinner, hotels);
        //Firebase
        databaseReference = FirebaseDatabase.getInstance(DatabaseUrl).getReference("Android Hotel");
        //Progress layout
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        androidx.appcompat.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                hotels.clear();
                for (DataSnapshot itemSnapshot: snapshot.getChildren()){
                    Hotel tempHotel = itemSnapshot.getValue(Hotel.class);
                    tempHotel.setKey(itemSnapshot.getKey());
                    hotels.add(tempHotel);
                }
                hotelSpinnerAdapter.notifyDataSetChanged();
                alertDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                alertDialog.dismiss();
            }
        });



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
    }
    public void GoBack(View view){
        finish();
    }
    private void addContentOrPicture() {
        Button btn_add_content = findViewById(R.id.btn_addText_upload_tour);
        Button btn_add_picture = findViewById(R.id.btn_addPicture_upload_tour);
        detailSchedule = contextOrPictureUploadAdapter.getDetailNewsList();
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
    private void UploadImage(){
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
                            Toast.makeText(UpdateTourActivity.this, "No Image Selected", Toast.LENGTH_SHORT);
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
        bt_UploadTour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YesNoDialog dialog;
                dialog = new YesNoDialog(UpdateTourActivity.this,"Bạn có xác nhận cập nhật ?","Có", "Không");
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                dialog.btn_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                contextOrPictureUploadAdapter.notifyDataSetChanged();
                        for (DetailNews item: contextOrPictureUploadAdapter.getDetailNewsList())
                        {
                            if(item.isImage()) {}
                            else {
                                uploadDetailSchedule.add(item);
                            }
                        }
                        saveData();
                    }
                });

            }
        });
    }
    //Xử lý picture của thumbnail
    public void saveData()
    {
        if (uri != null) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                    .child("Android Tour Image").child(uri.getLastPathSegment());
            AlertDialog.Builder builder = new AlertDialog.Builder(UpdateTourActivity.this);
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
        }else
        {
            ThumbnailURL = oldThumbnailURL;
            updateData();
        }
    }
    public void updateData()
    {
        //Upload Data Firebase
        String title = et_title.getText().toString();
        String Date = et_date.getText().toString();
        String Duration = et_duration.getText().toString();
        String Location = et_location.getText().toString();
        String text = et_text.getText().toString();
        Hotel hotel = (Hotel) spn_hotel.getSelectedItem();
        String hotelID = hotel.getKey();
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
            Place tempPlace = tour;
            tempPlace.setTitle(title);
            tempPlace.setDate(Date);
            tempPlace.setLocation(Location);
            tempPlace.setPrice(Price);
            tempPlace.setDuration(Duration);
            tempPlace.setThumbnail_Image(ThumbnailURL);
            tempPlace.setText(text);
            tempPlace.setSchedule(detailSchedule);
            tempPlace.setHotel(hotelID);
            tempPlace.setPlaneFrom(diaDiemKhoiHanh);
            tempPlace.setDuration(thoiLuongChuyenBay);
            tempPlace.setNumberOfSegment(SoChang);
            tempPlace.setPlaneDate(ngayBay);
            tempPlace.setPlaneBrand(hangBay);
            tempPlace.setTimeTakeOff(thoiGianCatCanh);
            tempPlace.setTimeLanding(thoiGianHaCanh);
            tempPlace.setPlaneTienIch(PlaneTienIch);
            tempPlace.setCarType(loaiXe);
            tempPlace.setCarTienIch(CarTienIch);
            tempPlace.setActive(isActive);
            toursRef.child(tour.getKey()).
                    setValue(tempPlace).
                    addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                if (ThumbnailURL != oldThumbnailURL){
                                    StorageReference storageReference1 = FirebaseStorage.getInstance().getReferenceFromUrl(oldThumbnailURL);
                                    storageReference1.delete();
                                }
                                Toast.makeText(UpdateTourActivity.this, "Update", Toast.LENGTH_SHORT);
                                finish();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UpdateTourActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT);
                        }
                    });
        } else {
            Toast.makeText(this,"Hãy nhập đầy đủ thông tin",Toast.LENGTH_LONG);
        }
    }

}