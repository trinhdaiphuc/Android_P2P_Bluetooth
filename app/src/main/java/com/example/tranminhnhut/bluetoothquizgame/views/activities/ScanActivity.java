package com.example.tranminhnhut.bluetoothquizgame.views.activities;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tranminhnhut.bluetoothquizgame.presenters.ScanPresenter;
import com.example.tranminhnhut.bluetoothquizgame.views.holder.ListBluetooth;
import com.example.tranminhnhut.bluetoothquizgame.models.Bluetooth;
import com.example.tranminhnhut.bluetoothquizgame.R;
import com.example.tranminhnhut.bluetoothquizgame.views.interfaces.ScanView;

import java.util.ArrayList;

//Quét các thiết bị có bluetooth xung quanh
public class ScanActivity extends Activity implements ScanView {
    public static final String MAC_OF_DEVICE = "MAC";
    //Presenter
    ScanPresenter scanPresenter;
    //Text view
    TextView textView; // Text "Nothing" nếu không quét được thiết bị nào
    // list bluetooth show
    ListView mListview; // Danh sách bluetooth quét được
    //device connect
    Bluetooth activeDevice = null; //Tên và MAC của bluetooth mà user chọn
    BluetoothDevice inActivity = null; // Device của bluetooth user yêu cầu pair

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Khởi tạo variable
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.listview);

        textView = (TextView)findViewById(R.id.txtNothing);
        mListview = (ListView) findViewById(R.id.listView);

        setProgressBarIndeterminateVisibility(true);
        setTitle("Scanning for device...");

        // gửi yêu cầu pair đến thiết bị user chọn
        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                activeDevice = (Bluetooth) parent.getItemAtPosition(position);
                if (activeDevice == null)
                    return;

                BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                inActivity = bluetoothAdapter.getRemoteDevice(activeDevice.getMac());

                inActivity.createBond();
            }
        });


        mListview.setEnabled(false);
        scanPresenter = new ScanPresenter(this);
        scanPresenter.getArroundDevie();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        scanPresenter.unRegister();
    }

    //show danh sách thiết bị có thể pair
    @Override
    public void displayPairDevice(ArrayList<Bluetooth> bluetoothArrayList) {
        ListBluetooth btAdapteter = new ListBluetooth(getApplicationContext(), R.layout.adapter_bluetooth, bluetoothArrayList);

        mListview.setAdapter(btAdapteter);
        }

    //Quet thanh cong
    @Override
    public void notifySuccess() {
        setProgressBarIndeterminateVisibility(false);
        setTitle("Select device to connect");
        mListview.setEnabled(true);
         if (mListview.getCount() == 0)
            textView.setVisibility(View.VISIBLE);
         else
             textView.setVisibility(View.INVISIBLE);

         Toast.makeText(ScanActivity.this, "Scan successfully", Toast.LENGTH_LONG).show();
    }

    @Override
    public Context getContext() {
        return  this;
    }
}
