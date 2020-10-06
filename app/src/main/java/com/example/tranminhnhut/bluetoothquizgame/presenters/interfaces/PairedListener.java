package com.example.tranminhnhut.bluetoothquizgame.presenters.interfaces;

import android.bluetooth.BluetoothDevice;

import java.util.Set;

public interface PairedListener {
    void loadPairedDevice();
    void onLoadPairedDevice(Set<BluetoothDevice> bluetoothDevices);
}
