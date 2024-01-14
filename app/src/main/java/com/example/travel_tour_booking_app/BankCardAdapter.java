package com.example.travel_tour_booking_app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BankCardAdapter extends RecyclerView.Adapter<BankCardAdapter.ViewHolder> {

    private final Context context;
    private List<BankCard> bankCardList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);

        void onEditClick(int position);

        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public BankCardAdapter(Context context, List<BankCard> bankCardList) {
        this.context = context;
        this.bankCardList = bankCardList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bank_card, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BankCard bankCard = bankCardList.get(position);

        if (bankCard != null) {
            holder.textViewCardNumber.setText("Card Number: " + bankCard.getCardNumber());
            holder.textViewCardName.setText("Card Name: " + bankCard.getCardName());
            holder.textViewExpiryDate.setText("Expiry Date: " + bankCard.getExpiryDate());
        }

        holder.btnEdit.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onEditClick(position);
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onDeleteClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bankCardList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public Comment textViewCardName;
        TextView textViewCardNumber, textViewCardHolder, textViewExpiryDate;
        ImageButton btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCardNumber = itemView.findViewById(R.id.textViewCardNumber);
            textViewCardHolder = itemView.findViewById(R.id.textViewCardName);
            textViewExpiryDate = itemView.findViewById(R.id.textViewExpiryDate);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
