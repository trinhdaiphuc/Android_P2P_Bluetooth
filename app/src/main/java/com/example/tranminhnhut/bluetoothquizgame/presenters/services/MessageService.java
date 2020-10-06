package com.example.tranminhnhut.bluetoothquizgame.presenters.services;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

import com.example.tranminhnhut.bluetoothquizgame.Constants;
import com.example.tranminhnhut.bluetoothquizgame.views.fragment.FragmentChat;
import com.example.tranminhnhut.bluetoothquizgame.views.activities.MainActivity;

//Service quản lý các tin nhắn giữa các service khác và activity
public class MessageService extends Service {
    //Mange thread service
    private Boolean isReadyReceived;
    //Binder
    private final IBinder mIBinder = new LocalBinder();

    public MessageService() {
    }

    //Handler cua cac Activity
    private Handler handlerMain;
    private Handler handlerQuiz;
    private Handler handlerSelect;

    //Noi dung message
    private String message_info = "";
    //Ten device connect
    private String nDevice = "";
    //Type message
    private int message;
    //Chating instance
    private FragmentChat fragmentChat;

    //Bluetooth Service
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothService bluetoothService;

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE: // Gửi trạng thái hiện tại tới Main
                    switch (msg.arg1) {
                        case MainActivity.STATE_CONNECTED: {
                            Message message = handlerMain.obtainMessage(Constants.MESSAGE_SET_TITLE, MainActivity.STATE_CONNECTED);
                            Bundle bundle = new Bundle();
                            bundle.putString(Constants.SET_TITLE, "Connected to " + nDevice);
                            message.setData(bundle);
                            handlerMain.sendMessage(message);
                        }
                            break;
                        case MainActivity.STATE_CONECTING:{
                            Message message = handlerMain.obtainMessage(Constants.MESSAGE_SET_TITLE, MainActivity.STATE_CONECTING);
                            Bundle bundle = new Bundle();
                            bundle.putString(Constants.SET_TITLE, "Connecting...");
                            message.setData(bundle);
                            handlerMain.sendMessage(message);
                        }
                            break;
                        case MainActivity.STATE_NONE:{
                            Message message = handlerMain.obtainMessage(Constants.MESSAGE_SET_TITLE, MainActivity.STATE_NONE);
                            Bundle bundle = new Bundle();
                            bundle.putString(Constants.SET_TITLE, "Not connected");
                            message.setData(bundle);
                            handlerMain.sendMessage(message);
                        }
                            break;
                    }
                    break;
                case Constants.MESSAGE_WRITE_TO_SERVICE: // Gửi tin nhắn chat qua máy kết nối
                    message = Constants.SET_MESSAGE_CHAT;
                    message_info = msg.getData().getString(Constants.WRITE_MESSAGE);
                    bluetoothService.receiveMessage(message, message_info);
                    break;
                case Constants.MESSAGE_READ: //Nhận tin nhắn chat
                    byte[] readBuf = (byte[]) msg.obj;
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    fragmentChat.insertMessage(readMessage);
                    break;
                case Constants.MESSAGE_DEVICE_MAC: // Nhận thiết bị kết nối bluetooth
                    String mac = msg.getData().getString(Constants.DEVICE_MAC);
                    BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(mac);
                    nDevice = bluetoothDevice.getName();
                    if (null != getApplicationContext()) {
                        Toast.makeText(getApplicationContext(), "Connected to "
                                + nDevice, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Constants.MESSAGE_TOAST: // Thông báo kết nối
                    if (null != getApplicationContext()) {
                        Toast.makeText(getApplicationContext(), msg.getData().getString(Constants.TOAST),
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Constants.NOTICE_ANSWER: // Gui yeu cau muon tra loi
                    message = Constants.SET_NOTICE_ANSWER;
                    message_info = msg.getData().getString(Constants.WANT_ANSWER);
                    bluetoothService.receiveMessage(message, message_info);
                    break;
                case Constants.MESSAGE_ANSWER: // Nhận thông báo máy pair giành quyền trả lời
                {
                    Message message = handlerQuiz.obtainMessage(Constants.NOTICE_ANSWER);
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.WANT_ANSWER, Constants.ANSWER);
                    message.setData(bundle);
                    handlerQuiz.sendMessage(message);
                }
                break;
                case Constants.MESSAGE_READY: // Máy bạn gửi thông báo đã sẵn sàng
                {
                    handlerMain.obtainMessage(Constants.MESSAGE_READY).sendToTarget();
                }
                break;
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        isReadyReceived = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public IBinder onBind(Intent intent) {
        return this.mIBinder;
    }

    public Handler getHandler() {
        return this.handler;
    }

    // set handler cua cac activity
    public void setHandlerMain(Handler handlerMain){
        this.handlerMain = handlerMain;
    }

    public void setHandlerQuiz(Handler handlerQuiz) {
        this.handlerQuiz = handlerQuiz;
    }

    public void setHandlerSelect(Handler handlerSelect){this.handlerSelect = handlerSelect;}

    // instance cua fragment chating

    public void setFragmentChat(FragmentChat fragmentChat) {
        this.fragmentChat = fragmentChat;
    }

    //instance cua bluetooth service
    public void setBluetoothService(BluetoothService bluetoothService) {
        this.bluetoothService = bluetoothService;
    }

    public int getTypeService(){
        return bluetoothService.getService_type();
    }

    //Local binder de activity truy xuat service
    public class LocalBinder extends Binder
    {
        public MessageService getInstance()
        {
            return MessageService.this;
        }
    }
}
