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
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.List;

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

    DatabaseReference databaseReference;
    String DatabaseUrl = "https://travel-tour-booking-app-default-rtdb.asia-southeast1.firebasedatabase.app";

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
                if(checkException()){
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
            }

        });
    }

    private boolean checkException() {
        String title = et_title.getText().toString();
        String Date = et_date.getText().toString();
        String Duration = et_duration.getText().toString();
        String Location = et_location.getText().toString();
        String text = et_text.getText().toString();

        String diaDiemKhoiHanh = et_planefrom.getText().toString();
        String thoiLuongChuyenBay = et_planeduration.getText().toString();
        String SoChang = et_segment.getText().toString();
        String ngayBay = et_planedate.getText().toString();
        String hangBay = et_planebrand.getText().toString();
        String thoiGianCatCanh = et_timetakeoff.getText().toString();
        String thoiGianHaCanh = et_timelanding.getText().toString();

        String loaiXe = et_cartype.getText().toString();

        String Price = et_price.getText().toString();

        List<String> checkEmtyList = new ArrayList<>();
        checkEmtyList.add(title);
        checkEmtyList.add(Date);
        checkEmtyList.add(Duration);
        checkEmtyList.add(Location);
        checkEmtyList.add(diaDiemKhoiHanh);
        checkEmtyList.add(thoiLuongChuyenBay);
        checkEmtyList.add(hangBay);
        checkEmtyList.add(thoiGianCatCanh);
        checkEmtyList.add(thoiGianHaCanh);
        checkEmtyList.add(loaiXe);
        checkEmtyList.add(Price);
        checkEmtyList.add(SoChang);
        checkEmtyList.add(ngayBay);

        if(Helper.hasEmptyElement(checkEmtyList)){
            Toast.makeText(getBaseContext(),"Hãy nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!Helper.isNaturalNumber(SoChang))
        {
            Toast.makeText(getBaseContext(),"Số chặng là một số tự nhiên. Vui lòng nhập lại", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(Helper.isValidCurrencyFormat(Price)){
            Toast.makeText(getBaseContext(),"Vui lòng nhập đúng định dạng giá", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
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
            String toursId = toursRef.push().getKey();
            Place place = new Place(title,Date,Location,Price,Duration,ThumbnailURL,
                    text,detailSchedule,hotelID,diaDiemKhoiHanh,thoiLuongChuyenBay,SoChang,ngayBay,hangBay,
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