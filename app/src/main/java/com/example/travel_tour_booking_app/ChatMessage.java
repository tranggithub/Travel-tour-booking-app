package com.example.travel_tour_booking_app;

import java.util.List;

public class ChatMessage {

    private String content;
    private int isBotMessage;
    List<String> listData;

    public ChatMessage() {
    }

    public ChatMessage(String content, int isBotMessage) {
        this.content = content;
        this.isBotMessage = isBotMessage;
        this.listData = null;
    }
    public ChatMessage(List<String> listData, int isBotMessage){
        this.listData = listData;
        this.isBotMessage = isBotMessage;
        this.content = null;
    }

    public String getContent() {
        return content;
    }
    public List<String> getListData() {
        return listData;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getIsBotMessage() {
        return isBotMessage;
    }

    public void setIsBotMessage(int isBotMessage) {
        this.isBotMessage = isBotMessage;
    }
}
