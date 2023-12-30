package com.example.travel_tour_booking_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class UserInformationActivity extends AppCompatActivity {
    private static String FIREBASE_STORAGE_URL = "gs://travel-tour-booking-app.appspot.com/";
    public static String FIREBASE_REALTIME_DATABASE_URL = "https://travel-tour-booking-app-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private static int MY_REQUEST_CODE = 10;
    Button btnConfirm;
    EditText edtFirstName, edtName, edtEmail, edtSdt;
    TextView tvThayDoiAvatar;
    ImageView ivAvatar;
    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference databaseReference;
    ImageView ivBack;
    ReadWriteUserDetails tmpUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);

        btnConfirm = findViewById(R.id.btn_XacNhan_userinformation);
        edtFirstName = findViewById(R.id.edt_Ho_userinformation);
        edtName = findViewById(R.id.edt_Ten_userinformation);
        edtEmail = findViewById(R.id.edt_Email_userinformation);
        edtSdt = findViewById(R.id.edt_SoDienThoai_userinformation);
        ivAvatar = findViewById(R.id.iv_avatar_userinformation);
        ivBack = findViewById(R.id.iv_returnbutton_userinformation);
        tvThayDoiAvatar = findViewById(R.id.tv_thaydoihinhanh_userinformation);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance(FIREBASE_REALTIME_DATABASE_URL).getReference("users");

        DisplayUser(user);

        // Disable EditText based on login method
        if (user.getEmail() != null) {
            edtEmail.setEnabled(false);
            edtSdt.setEnabled(true);
        } else if (user.getPhoneNumber() != null) {
            edtEmail.setEnabled(true);
            edtSdt.setEnabled(false);
        }

        //Thay đổi Avatar
        tvThayDoiAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickRequestPermission();
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserInformationActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null) {
                    // Retrieve values from EditText fields
                    String ho = edtFirstName.getText().toString();
                    String ten = edtName.getText().toString();
                    String email = edtEmail.getText().toString();
                    String sdt = edtSdt.getText().toString();

                    if (!email.contains("@")) {
                        Toast.makeText(UserInformationActivity.this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (ho != null) tmpUser.setHo(ho);
                    if (ten != null) tmpUser.setTen(ten);
                    if (email != null) tmpUser.setEmail(email);
                    if (sdt != null) tmpUser.setSdt(sdt);

                    // Update the user information in the database
                    databaseReference.child(user.getUid()).setValue(tmpUser)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(UserInformationActivity.this, "Thông tin người dùng đã được cập nhật.", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(UserInformationActivity.this, "Lỗi khi cập nhật thông tin người dùng.", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }

    private ReadWriteUserDetails GetTempDetailUser(FirebaseUser user) {
        ReadWriteUserDetails userDetails = new ReadWriteUserDetails();
        if (user != null) {
            databaseReference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ReadWriteUserDetails userDetails = snapshot.getValue(ReadWriteUserDetails.class);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    tmpUser = null;
                    Toast.makeText(UserInformationActivity.this, "tmpUser rỗng", Toast.LENGTH_SHORT).show();
                }
            });
        }
        return userDetails;
    }

    private void DisplayUser(FirebaseUser user) {
            databaseReference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        ReadWriteUserDetails userDetails = snapshot.getValue(ReadWriteUserDetails.class);
                        tmpUser = userDetails;
                        if (userDetails != null) {
                            if (userDetails.getHo() != null) {
                                edtFirstName.setText(userDetails.getHo());
                                edtName.setText(userDetails.getTen());
                            } else {
                                edtName.setText(userDetails.getTen());
                            }
                            if (user.getEmail() != null)
                                edtEmail.setText(user.getEmail());
                            else edtEmail.setText("");
                           if (userDetails.getSdt() != null) {
                                edtSdt.setText(userDetails.getSdt());
                            } else {
                                edtSdt.setText("");
                            }
                            Picasso.get().load(userDetails.getUrlImage()).into(ivAvatar);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle any errors that may occur
                    Toast.makeText(UserInformationActivity.this, "Lỗi khi đọc dữ liệu người dùng.", Toast.LENGTH_SHORT).show();
                }
            });

    }

    private void onClickRequestPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            openGallery();
            return;
        }
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        } else {
            String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permissions, MY_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), MY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MY_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Handle the selected image URI here
            Uri imageUri = data.getData();
            Picasso.get().load(imageUri).into(ivAvatar);
            uploadImageToFirebase(imageUri);
            tmpUser.setUrlImage(getImageUrl(user));
            databaseReference.child(user.getUid()).setValue(tmpUser)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(UserInformationActivity.this, "Ảnh đại diện người dùng đã được cập nhật.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UserInformationActivity.this, "Lỗi khi cập nhật ảnh đại diện người dùng.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        if (user != null) {
            // Define a reference to the Firebase Storage location
            StorageReference storageRef = FirebaseStorage.getInstance(FIREBASE_STORAGE_URL).getReference()
                    .child("Android Users Image")
                    .child(user.getUid() + "_avatar.jpg");

            // Upload the image to Firebase Storage
            storageRef.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Image uploaded successfully
                            // Now, get the download URL of the uploaded image
                            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri downloadUrl) {
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle the failure to upload the image
                            Toast.makeText(UserInformationActivity.this, "Lỗi uplpad ảnh lên Firebase Storage", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private String getImageUrl(FirebaseUser user) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                .child("Android Users Image").child(user.getUid() + "_avatar.jpg");
        Task<Uri> uriTask = storageReference.getDownloadUrl();
        while (!uriTask.isComplete()) ;
        Uri url_Image = uriTask.getResult();
        return url_Image.toString();
    }
}