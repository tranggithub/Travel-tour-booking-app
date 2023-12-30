package com.example.travel_tour_booking_app;

import android.app.Activity;
import android.hardware.camera2.CameraExtensionSession;
import android.net.Uri;
import android.provider.ContactsContract;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class ReadWriteUserDetails implements Serializable {
    StorageReference storageReference;
    private String ho;
    private String ten;
    private String urlImage;
    private String role;
    private String sdt;
    private String email;
    private int delected;
    private ArrayList<String> searchHistory = new ArrayList<>();
    ArrayList<String> listLikeTours = new ArrayList<>();
    ArrayList<String> listRecentlyViewedTours = new ArrayList<>();
    public ReadWriteUserDetails() {
        this.ho = null;
        this.ten = null;
        this.delected = 0;
        this.role = "user";
        getDefaultImageUrl();
    }

    ReadWriteUserDetails(String ho, String ten) {
        this.ho = ho;
        this.ten = ten;
        getDefaultImageUrl();
        this.role = "user";
        this.delected = 0;
    }

    ReadWriteUserDetails(String userDisplayname, Uri photo) {
        this.ho = null;
        this.ten = userDisplayname;
        if (photo != null) { this.urlImage=photo.toString();} else getDefaultImageUrl();
        this.role = "user";
        this.delected = 0;
    }

    private void getDefaultImageUrl() {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                .child("Android Users Image").child("default.png");
        Task<Uri> uriTask = storageReference.getDownloadUrl();
        while (!uriTask.isComplete()) ;
        Uri url_Image = uriTask.getResult();
        urlImage = url_Image.toString();
    }
    public String getHo() {
        return ho;
    }

    public void setHo(String ho) {
        this.ho = ho;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setDelected(int delected) {
        this.delected = delected;
    }

    public int getDelected() {
        return delected;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getSdt() {
        return sdt;
    }

    public ArrayList<String> getListLikeTours() {
        return listLikeTours;
    }

    public void setListLikeTours(ArrayList<String> listLikeTours) {
        this.listLikeTours = listLikeTours;
    }
    public String getFullName(){
        if (getHo() == null || getHo().equals("")) {
            return getTen();
        }
        return getHo() + " " + getTen();
    }

    public ArrayList<String> getSearchHistory() {
        return searchHistory;
    }

    public void setSearchHistory(ArrayList<String> searchHistory) {
        this.searchHistory = searchHistory;
    }

    public ArrayList<String> getListRecentlyViewedTours() {
        return listRecentlyViewedTours;
    }

    public void setListRecentlyViewedTours(ArrayList<String> listRecentlyViewedTours) {
        this.listRecentlyViewedTours = listRecentlyViewedTours;
    }
}
