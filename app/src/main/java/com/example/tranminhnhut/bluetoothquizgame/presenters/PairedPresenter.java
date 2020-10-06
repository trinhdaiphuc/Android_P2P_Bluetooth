package com.example.tranminhnhut.bluetoothquizgame.presenters;

import android.bluetooth.BluetoothDevice;

import com.example.tranminhnhut.bluetoothquizgame.models.Bluetooth;
import com.example.tranminhnhut.bluetoothquizgame.models.handlesModels.PairedHandle;
import com.example.tranminhnhut.bluetoothquizgame.presenters.interfaces.PairedListener;
import com.example.tranminhnhut.bluetoothquizgame.views.interfaces.PairedView;

import java.util.ArrayList;
import java.util.Set;

public class PairedPresenter implements PairedListener {
    //Model
    private PairedHandle pairedHandle;
    //View
    private PairedView pairedView;
    public PairedPresenter(PairedView pairedView) {
        this.pairedView = pairedView;
        pairedHandle = new PairedHandle(this);
    }

    @Override
    public void loadPairedDevice() {
        pairedHandle.getPairedDevice();
    }

    @Override
    public void onLoadPairedDevice(Set<BluetoothDevice> bluetoothDevices) {
        ArrayList<Bluetooth> bluetoothList = new ArrayList<Bluetooth>();
        if (bluetoothDevices.size() > 0) {
            // Lấy tên và MAC thiết bị đã pair đưa vào Array List
            for (BluetoothDevice device : bluetoothDevices) {
                Bluetooth b = new Bluetooth(device.getName(), device.getAddress());
                bluetoothList.add(b);
            }
        }
        //Gửi danh sách thiết bị gồm Name và MAC về cho view
        pairedView.displayPairedDevice(bluetoothList);
    }
}
