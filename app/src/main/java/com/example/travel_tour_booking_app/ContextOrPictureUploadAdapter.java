package com.example.travel_tour_booking_app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ContextOrPictureUploadAdapter extends RecyclerView.Adapter<ContextOrPictureUploadAdapter.MyViewHolder> {
    Context context;
    ArrayList<DetailNews> DetailNewsList;

    public ContextOrPictureUploadAdapter (Context context, ArrayList<DetailNews> DetailNewsList){
        this.context = context;
        this.DetailNewsList = DetailNewsList;
    }
    @Override
    public ContextOrPictureUploadAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(context).inflate(R.layout.item_upload_news_text, parent, false);
        ContextOrPictureUploadAdapter.MyViewHolder myViewHolder = new ContextOrPictureUploadAdapter.MyViewHolder(v);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ContextOrPictureUploadAdapter.MyViewHolder holder, int position) {
        DetailNews temp = DetailNewsList.get(position);
        if (temp.isImage())
        {
            holder.et_context.setVisibility(View.GONE);
            holder.ll_picture_upload_news.setVisibility(View.VISIBLE);
            temp.setDetailText(null);
            if(temp.getPicture() != null)
            {
                Glide.with(context).load(DetailNewsList.get(position).getPicture()).into(holder.iv_Picture);
            }
            holder.et_subtitle_Picture.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    temp.setSubtitleImage(s.toString());
                }
            });

        } else {
            holder.ll_picture_upload_news.setVisibility(View.GONE);
            holder.et_context.setVisibility(View.VISIBLE);
            temp.setPicture(null);
            temp.setSubtitleImage(null);

            holder.et_context.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    temp.setDetailText(s.toString());
                }
            });
        }
        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailNewsList.remove(position);
                notifyDataSetChanged();
            }
        });

        holder.iv_Picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                ((Activity) context).startActivityForResult(pickImageIntent, position);

            }
        });



    }

    @Override
    public int getItemCount() {
        return DetailNewsList.size();
    }

    public ArrayList<DetailNews> getDetailNewsList() {
        return DetailNewsList;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        LinearLayout ll_picture_upload_news;
        private EditText et_context;
        private ImageView iv_Picture;
        private EditText et_subtitle_Picture;
        private ImageView iv_delete;
        public MyViewHolder(View itemView){
            super(itemView);
            et_context = itemView.findViewById(R.id.edt_item_upload_news);
            iv_Picture = itemView.findViewById(R.id.iv_image_upload_news);
            et_subtitle_Picture = itemView.findViewById(R.id.edt_subtitle_picture_upload_news);
            ll_picture_upload_news = itemView.findViewById(R.id.ll_picture_upload_news);
            iv_delete = itemView.findViewById(R.id.iv_delete_upload_news);
        }
    }
}