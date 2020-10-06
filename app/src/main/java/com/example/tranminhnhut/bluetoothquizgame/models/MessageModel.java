package com.example.tranminhnhut.bluetoothquizgame.models;

import android.text.Layout;

public class MessageModel {
    private String message;
    private int layout;

    public MessageModel() {
    }

    public MessageModel(String message, int layout) {
        this.message = message;
        this.layout = layout;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getLayout() {
        return layout;
    }

    public void setLayout(int layout) {
        this.layout = layout;
    }
}
