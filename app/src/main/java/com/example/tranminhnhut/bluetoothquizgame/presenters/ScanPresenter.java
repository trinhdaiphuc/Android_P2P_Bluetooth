package com.example.tranminhnhut.bluetoothquizgame.presenters;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.example.tranminhnhut.bluetoothquizgame.models.Bluetooth;
import com.example.tranminhnhut.bluetoothquizgame.views.interfaces.ScanView;

import java.util.ArrayList;

public class ScanPresenter {
    //View
    private ScanView scanView;
    //Context
    private Context context;
    // list bluetooth device can connect
    ArrayList<BluetoothDevice> btDevice; // Danh sách tên và Mac bluetooth có thể pair
    //Name and mac of bluetooth
    ArrayList<Bluetooth> bt;
    //Adapter bluetooth
    BluetoothAdapter bluetoothAdapter;

    public ScanPresenter(ScanView scanView){
        this.scanView = scanView;
        btDevice = new ArrayList<BluetoothDevice>();
        bt = new ArrayList<Bluetooth>();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        context = this.scanView.getContext();
    }

    //Broadcase receiver chạy background nhận action tìm thiết bị có thể pair
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //
            if (action.equals("android.bluetooth.device.action.NAME_CHANGED")) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                for (BluetoothDevice bt : btDevice) {
                    if (bt.getAddress().equals(device.getAddress()))
                        return;
                }
                btDevice.add(device);
                tranform();
                scanView.displayPairDevice(bt);
            }
            else
            if (action.equals("android.bluetooth.adapter.action.DISCOVERY_FINISHED")){
                scanView.notifySuccess();
            }
        }
    };

    public void getArroundDevie(){
        // Intent của broadcast receiver
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothDevice.ACTION_NAME_CHANGED);
        intentFilter.addAction(bluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        btDevice.clear();
        //Đăng kí các intent cho broadcastReceiver
        context.registerReceiver(broadcastReceiver, intentFilter);
        //Run broadcast receiver
        if (bluetoothAdapter.isDiscovering())
            bluetoothAdapter.cancelDiscovery();
        bluetoothAdapter.startDiscovery();
        while (true) {
            if (!bluetoothAdapter.isDiscovering())
                break;
        }

        tranform();
        //View nhận list device
        scanView.displayPairDevice(bt);
    }

    //Tranform bluetoothDevice to buletooth info
    public void tranform(){
        bt.clear();
        for (BluetoothDevice iDevice : btDevice) {
            Bluetooth b = new Bluetooth(iDevice.getName(), iDevice.getAddress());
            bt.add(b);
        }
    }

    public void unRegister(){
        context.unregisterReceiver(broadcastReceiver);
        bluetoothAdapter.cancelDiscovery();
    }
}
