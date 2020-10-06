package com.example.tranminhnhut.bluetoothquizgame.models.handlesModels;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import com.example.tranminhnhut.bluetoothquizgame.models.Bluetooth;
import com.example.tranminhnhut.bluetoothquizgame.presenters.interfaces.PairedListener;

import java.util.ArrayList;
import java.util.Set;

public class PairedHandle {
    BluetoothAdapter bluetoothAdapter;
    // Presenter paired
    private PairedListener pairedListener;

    public PairedHandle(PairedListener pairedListener){
        this.pairedListener = pairedListener;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    //Lấy các thiết bị đã pair trả về cho presenter
    public void getPairedDevice(){
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Danh sách thiết bị
        ArrayList<Bluetooth> bt = new ArrayList<Bluetooth>();

        //Lấy danh sách đã pair
        Set<BluetoothDevice> bluetoothDevices = bluetoothAdapter.getBondedDevices();

        //Đưa danh sách thiết bị về cho presenter
        pairedListener.onLoadPairedDevice(bluetoothDevices);
    }
}
