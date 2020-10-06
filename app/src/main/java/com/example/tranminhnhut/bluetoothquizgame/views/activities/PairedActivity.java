package com.example.tranminhnhut.bluetoothquizgame.views.activities;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tranminhnhut.bluetoothquizgame.presenters.PairedPresenter;
import com.example.tranminhnhut.bluetoothquizgame.views.holder.ListBluetooth;
import com.example.tranminhnhut.bluetoothquizgame.models.Bluetooth;
import com.example.tranminhnhut.bluetoothquizgame.R;
import com.example.tranminhnhut.bluetoothquizgame.views.interfaces.PairedView;

import java.util.ArrayList;

//Các thiết bị đã pair
public class PairedActivity extends Activity implements PairedView {
    public static final String MAC_OF_DEVICE = "MAC";
    //List view
    ListView listView; // danh sách thiết bị đã pair
    //Text view
    TextView textView; // "Nothing" nếu không có thiết bị nào đã pair
    //Presenter
    PairedPresenter pairedPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Khởi tạo các variable và load device
        setContentView(R.layout.paired_device);
        setTitle("Paired device");
        textView = (TextView) findViewById(R.id.txtNothing);
        listView = (ListView) findViewById(R.id.listView);

        //Nếu user click vào tên một thiết bị bất kì có trong danh sách
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bluetooth activeDevice = (Bluetooth) parent.getItemAtPosition(position); // Lấy thiết bị tại vị trí đã click
                if (activeDevice == null)
                    return;
                //Gửi message activity result về Main Activity
                Intent intent = new Intent();
                intent.putExtra(MAC_OF_DEVICE, activeDevice.getMac());

                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
        pairedPresenter = new PairedPresenter(this);
        pairedPresenter.loadPairedDevice();
    }

    @Override
    public void displayPairedDevice(ArrayList<Bluetooth> bluetoothDeviceList) {
        if (bluetoothDeviceList.size() == 0){
            // Hiện "Nothing" nếu không có thiết bị đã pair
            textView.setVisibility(View.VISIBLE);
        } else {
            // Tạo adapter với ArrayList ở trên
            ListBluetooth btAdapteter = new ListBluetooth(getApplicationContext(), R.layout.adapter_bluetooth, bluetoothDeviceList);
            //Set adapter vào list view
            listView.setAdapter(btAdapteter);
            // Ẩn text "Nothing"
            textView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
