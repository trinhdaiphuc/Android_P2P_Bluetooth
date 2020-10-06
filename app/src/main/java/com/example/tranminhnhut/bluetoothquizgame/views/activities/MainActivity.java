package com.example.tranminhnhut.bluetoothquizgame.views.activities;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tranminhnhut.bluetoothquizgame.Constants;
import com.example.tranminhnhut.bluetoothquizgame.R;
import com.example.tranminhnhut.bluetoothquizgame.presenters.MainPresenter;
import com.example.tranminhnhut.bluetoothquizgame.presenters.services.BluetoothService;
import com.example.tranminhnhut.bluetoothquizgame.views.interfaces.MainView;

public class MainActivity extends AppCompatActivity implements MainView {
    //Presenter
    MainPresenter mainPresenter;
    //bluetooth adapter
    BluetoothAdapter bluetoothAdapter;
    //Button
    Button Paired;
    //Text view cho máy client
    TextView textView;
    //flag
    Boolean isInitialize = false;
    //Kiểm tra 2 máy đã sẵn sàng vào game
    Boolean host_ready = false;
    Boolean client_ready = false;
    //state
    private static int cur_state = 0;
    public static final int STATE_NONE = 0;
    public static final int STATE_CONECTING = 1;
    public static final int STATE_CONNECTED = 2;
    public static final int STATE_LISTEN = 3;
    //Services code
    public static String RESTORE = "READY_ACCEPT";
    public static String MAC = "MAC";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //Kiểm tra máy có bluetooth
        if (bluetoothAdapter == null) {
            warningDialog();
            finish();
        }

        //Kiểm tra thiết bị đã enable
       if (bluetoothAdapter.isEnabled()) {
           isInitialize = true;
           initializeState();
       } else{
            getSupportActionBar().setSubtitle("Bluetooth isn't enable");
            Toast.makeText(getApplicationContext(), "Please open bluetooth", Toast.LENGTH_SHORT).show();
       }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.scan_device:
                if (!bluetoothAdapter.isEnabled()){
                    Toast.makeText(getApplicationContext(),"Bluetooth is turn off", Toast.LENGTH_SHORT).show();
                    return false;
                } else
                    mainPresenter.scan();
                return true;
            case R.id.open_bluetooth:
                if (!bluetoothAdapter.isEnabled()){
                    bluetoothAdapter.enable();
                    if (!isInitialize){
                        isInitialize = true;
                        initializeState();
                    } else{
                        getSupportActionBar().setSubtitle("Not connected");
                    }
                } else{
                    Toast.makeText(getApplicationContext(),"Bluetooth already enable", Toast.LENGTH_SHORT).show();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Sẵn sàng kết nối
        if (cur_state != MainActivity.STATE_CONNECTED && bluetoothAdapter.isEnabled()) {
            host_ready = false;
            client_ready = false;
            Intent intent = new Intent(MainActivity.this, BluetoothService.class);
            intent.putExtra(RESTORE, "Accept");
            this.startService(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Nhận result từ view Paired
        if (resultCode == Activity.RESULT_OK){
           /* if (fragmentChat == null){
            fragmentChat = new FragmentChat();
            fragmentChat.setMainHandler(handlerService);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.frame, fragmentChat).commit();

            messageService.setFragmentChat(fragmentChat);
        }*/
            mainPresenter.connect2device(data);
        }
        //Nhận result từ View SelectType (chỉ máy server)
        if (resultCode == Constants.GET_TYPE_SUCCESS){
            //Gửi thông báo cho máy client là đã chọn xong và đưa danh sách thể loại cho presenter
            if (cur_state != STATE_CONNECTED)
                Toast.makeText(getApplicationContext(),"You are not connect any device", Toast.LENGTH_SHORT).show();
            else {
                mainPresenter.setListType(data.getCharSequenceArrayListExtra(Constants.TYPE_QUESTION));
                host_ready = true;
                mainPresenter.mess_btService(Constants.SET_READY_CLIENT, "ready");
                if (client_ready)
                    mainPresenter.sendToHandlerMain(Constants.MESSAGE_READY_SUCCESS, null);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainPresenter.unRegister();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!bluetoothAdapter.isEnabled()){
            getSupportActionBar().setSubtitle("Bluetooth isn't enable");
            Toast.makeText(getApplicationContext(),"Bluetooth is turn off", Toast.LENGTH_SHORT).show();
            cur_state = STATE_NONE;
            host_ready = false;
            client_ready = false;
        } else
            if (bluetoothAdapter.isEnabled() && cur_state == STATE_NONE){
                getSupportActionBar().setSubtitle("Not connected");
                host_ready = false;
                client_ready = false;
            }
    }

    public void onBackPressed() {
        super.onBackPressed();
        mainPresenter.backpressed();
    }

    public void warningDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Bluetooth isn't support");
        builder.setTitle("Error");
        builder.setCancelable(false);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "Please check your device", Toast.LENGTH_LONG);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    public void initializeState(){
        mainPresenter = new MainPresenter(this);
        mainPresenter.initialize();

        getSupportActionBar().setSubtitle("Not connected");

        Paired = (Button) findViewById(R.id.bttnPaired);
        Paired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!bluetoothAdapter.isEnabled()) {
                    Toast.makeText(getApplicationContext(), "Bluetooth is turn off", Toast.LENGTH_SHORT).show();
                } else
                    mainPresenter.paired();
            }
        });

        textView = (TextView) findViewById(R.id.textView);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public Boolean isHostready() {
        return this.host_ready;
    }

    @Override
    public Boolean isClientready() {
        return this.client_ready;
    }

    @Override
    public void setHost(Boolean isReady) {
        this.host_ready = isReady;
    }

    @Override
    public void setClient(Boolean isReady) {
        this.client_ready = isReady;
    }

    @Override
    public void setsubTitle(String title) {
        getSupportActionBar().setSubtitle(title);
    }

    @Override
    public void setState(int state) {
        this.cur_state = state;
    }

    @Override
    public void bothDevice_ready() {
        host_ready = false;
        client_ready = false;
    }

    @Override
    public void bothDevice_notready() {
        Toast.makeText(getApplicationContext(),"Device connect was lost", Toast.LENGTH_SHORT).show();
        host_ready = false;
        client_ready = false;
    }

    //Textview cho máy client
    @Override
    public void setVisibleTextview(int isVisible) {
        textView.setVisibility(isVisible);
    }
}
