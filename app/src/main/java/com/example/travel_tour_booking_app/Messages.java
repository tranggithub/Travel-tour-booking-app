package com.example.travel_tour_booking_app;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Messages {
    private String timeDate;
    private ArrayList<ChatMessage> chatMessages;
    private Boolean status, isRep;
    private String senderId;

    public Messages() {
        this.timeDate = getTimeDateToday();
        this.chatMessages = new ArrayList<>();
        this.status = true;
        this.isRep = false;
    }
    private String getTimeDateToday(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm, dd/MM/yyyy");

            // Chuyển đổi thành chuỗi
            String formattedDateTime = now.format(formatter);

            return formattedDateTime;
        }
        return null;
    }

    public String getTimeDate() {
        return timeDate;
    }

    public ArrayList<ChatMessage> getChatMessages() {
        return chatMessages;
    }


    public void setTimeDate(String timeDate) {
        this.timeDate = timeDate;
    }

    public void setChatMessages(ArrayList<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public Boolean getRep() {
        return isRep;
    }

    public void setRep(Boolean rep) {
        isRep = rep;
    }
}
