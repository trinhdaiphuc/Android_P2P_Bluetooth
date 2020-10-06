package com.example.tranminhnhut.bluetoothquizgame.views.interfaces;

import android.content.Context;

import com.example.tranminhnhut.bluetoothquizgame.models.Bluetooth;

import java.util.ArrayList;

public interface ScanView {
    Context getContext();
    void displayPairDevice(ArrayList<Bluetooth> bluetoothArrayList);
    void notifySuccess();
}
