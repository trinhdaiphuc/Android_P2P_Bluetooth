package com.example.tranminhnhut.bluetoothquizgame.presenters;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;

import com.example.tranminhnhut.bluetoothquizgame.Constants;
import com.example.tranminhnhut.bluetoothquizgame.models.databases.QuestionDatabase;
import com.example.tranminhnhut.bluetoothquizgame.presenters.interfaces.MainListener;
import com.example.tranminhnhut.bluetoothquizgame.presenters.services.BluetoothService;
import com.example.tranminhnhut.bluetoothquizgame.presenters.services.MessageService;
import com.example.tranminhnhut.bluetoothquizgame.views.activities.MainActivity;
import com.example.tranminhnhut.bluetoothquizgame.views.activities.PairedActivity;
import com.example.tranminhnhut.bluetoothquizgame.views.activities.QuizActivity;
import com.example.tranminhnhut.bluetoothquizgame.views.activities.ScanActivity;
import com.example.tranminhnhut.bluetoothquizgame.views.activities.SelectTypeActivity;
import com.example.tranminhnhut.bluetoothquizgame.views.interfaces.MainView;

import java.util.ArrayList;

public class MainPresenter implements MainListener {
    //View
    private MainView mainView;
    private Context context;
    //BluetoothService
    private BluetoothService bluetoothService = null;
    //MessageService
    private MessageService messageService = null;
    //Bluetooth Adapter
    BluetoothAdapter bluetoothAdapter;
    //Database
    private QuestionDatabase questionDatabase = null;
    //flag
    Boolean ibound = false;
    //Handler cua message service
    private Handler handlerService = new Handler();
    //List type from SelectedType activity
    private ArrayList<CharSequence> listType;


    //Service Connection với Bluetooth Service
    private ServiceConnection bluetoothConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder)
        {
            //Lấy instance của bluetooth service
            bluetoothService = ((BluetoothService.LocalBinder)iBinder).getInstance();
            if (bluetoothService.getHandler() == null) {
                bluetoothService.setHandler(handlerService);
                if (messageService != null){
                    messageService.setBluetoothService(bluetoothService);
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName)
        {
            bluetoothService = null;
        }
    };

    //Service Connection với Message Service
    private ServiceConnection messageConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder)
        {
            messageService = ((MessageService.LocalBinder)iBinder).getInstance();
            //set handler cua main cho message service
            messageService.setHandlerMain(handlerMain);
            //Get handler cua message service
            handlerService = messageService.getHandler();
            //Set instance bluetoothservice cho message service
            if (bluetoothService != null) {
                bluetoothService.setHandler(handlerService);
                messageService.setBluetoothService(bluetoothService);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName)
        {
            messageService = null;
        }
    };

    //Handler cua main activity
    private final Handler handlerMain = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Constants.MESSAGE_SET_TITLE:{ // Nhận thông báo đổi title
                    String title = msg.getData().getString(Constants.SET_TITLE);
                    mainView.setsubTitle(title);
                    int cur_state = (int) msg.obj;
                    mainView.setState(cur_state);
                    if (cur_state != MainActivity.STATE_CONNECTED) {
                        mainView.setHost(false);
                        mainView.setClient(false);
                    }
                    //Nếu 2 thiết bị đã pair chuyển sang view select type
                    if (cur_state == MainActivity.STATE_CONNECTED) {
                        // Nếu là máy server thì chuyển view
                        if (bluetoothService.getService_type() == Constants.IS_SERVER)
                            selectType();
                        else {
                            // Nếu là máy khách thì gửi thông báo sẵn sàng cho server
                            mainView.setHost(true);
                            mainView.setVisibleTextview(View.VISIBLE);
                            bluetoothService.receiveMessage(Constants.SET_READY_CLIENT, "ready");
                        }
                    }
                }
                break;
                case Constants.MESSAGE_READY:{ // Nhận thông báo là máy bạn đã sẵn sàng
                    mainView.setClient(true);
                    if (mainView.isHostready())
                        handlerMain.obtainMessage(Constants.MESSAGE_READY_SUCCESS).sendToTarget();
                }
                break;
                case Constants.MESSAGE_UNREADY:{ // Nhận thông báo máy bạn chưa sẵn sàng
                    mainView.setClient(false);
                }
                break;
                case Constants.MESSAGE_READY_SUCCESS:{ // Nhận thông báo cả 2 máy đã sẵn sàng và chuyển view
                    mainView.bothDevice_ready();
                    Intent intent = new Intent(context, QuizActivity.class);
                    intent.putExtra(Constants.TYPE_QUESTION, listType);
                    context.startActivity(intent);
                }
                break;
                case Constants.MESSAGE_READY_INTERRUPT:{ // Nhận thông báo cả 2 máy đều chưa sẵn sàng
                    mainView.bothDevice_notready();
                }
                break;
            }
        }
    };

    public MainPresenter(MainView mainView) {
        this.mainView = mainView;
        context = this.mainView.getContext();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public void initialize(){
        if (questionDatabase == null) {
            questionDatabase = new QuestionDatabase(context);
        }
        //Start message service
        Intent intentMessage = new Intent(context, MessageService.class);

        context.startService(intentMessage);

        context.bindService(new Intent(context,
                MessageService.class), messageConnection, Context.BIND_AUTO_CREATE);

        //Start bluetooth service
        Intent intentBluetooth = new Intent(context, BluetoothService.class);

        context.startService(intentBluetooth);

        context.bindService(new Intent(context,
                BluetoothService.class), bluetoothConnection, Context.BIND_AUTO_CREATE);

        ibound = true;
    }

    //View scan thiết bị xung quanh
    public void scan(){
        Intent intent = new Intent(context, ScanActivity.class);
        context.startActivity(intent);
    }

    public void unRegister(){
        // unbind các service và dừng
        if (ibound)
        {
            // Detach our existing connection.
            context.unbindService(bluetoothConnection);
            context.unbindService(messageConnection);
            ibound = false;
        }

        context.stopService(new Intent(context, BluetoothService.class));
        context.stopService(new Intent(context, MessageService.class));
    }

    public void backpressed(){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    // View yêu cầu kết nối với các thiết bị
    public void paired(){
        Intent intent = new Intent(context, PairedActivity.class);
        ((Activity) context).startActivityForResult(intent, 1);
    }

    //View hiện bảng chọn chủ đề
    public void selectType(){
        Intent intent = new Intent(context, SelectTypeActivity.class);
        ((Activity) context).startActivityForResult(intent, 1);
    }

    //Gửi message đến BluetoothService
    public void mess_btService(int iMess, String info){
        bluetoothService.receiveMessage(iMess, info);
    }

    public Handler getHandlerMain() {
        return this.handlerMain;
    }

    //Nhận yêu cầu kết nối từ thiết bị khác
    public void connect2device(Intent data){
        String mac = data.getExtras().getString(ScanActivity.MAC_OF_DEVICE);

        Intent intent = new Intent(context, BluetoothService.class);
        intent.putExtra(MainActivity.MAC, mac);
        context.startService(intent);
    }

    //View gửi yêu cầu đến presenter xử lí
    public void sendToHandlerMain(int notice, Bundle bundle){
        if (bundle == null){
            handlerMain.obtainMessage(notice).sendToTarget();
        } else{
            Message message = handlerMain.obtainMessage(notice);
            message.setData(bundle);
            handlerMain.sendMessage(message);
        }
    }
    //Nhận list thể loại được chọn từ view selectType
    public void setListType(ArrayList<CharSequence> listType) {
        this.listType = new ArrayList<>(listType);
    }
}
