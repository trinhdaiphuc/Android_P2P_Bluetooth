package com.example.tranminhnhut.bluetoothquizgame.presenters.services;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;


import com.example.tranminhnhut.bluetoothquizgame.Constants;
import com.example.tranminhnhut.bluetoothquizgame.views.activities.MainActivity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

// Service kết nối và gửi nhận tin nhắn giữa 2 thiết bị đã pair
public class BluetoothService extends Service {
    private final IBinder mIBinder = new LocalBinder();

    BluetoothAdapter bluetoothAdapter;
    BluetoothDevice bluetoothDevice = null;
    private int service_type;

    private AcceptThread acceptThread;
    private ConnectThread connectThread;
    private ConnectedThread connectedThread;
    //Type message from another activity
    private int message;

    private Handler handlerService;
    private static int state;
    private static final UUID uuid = UUID.fromString("531A481B-826B-462D-9335-E71F5CEAAE29");
    private String mac = null;
    private boolean stopThread;

    public BluetoothService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        state = MainActivity.STATE_NONE;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        stopThread = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //kiểm tra có phải yêu cầu mở chấp nhận kết nôi
        String accept = intent.getStringExtra("Accept");
        if (accept == null) {
            // Lấy địa chỉ mac của thiết bị yêu cầu kết nối
            mac = intent.getStringExtra("MAC");
            if (mac == null) {
                return super.onStartCommand(intent, flags, startId);
            }
            bluetoothDevice = bluetoothAdapter.getRemoteDevice(mac);
            if (bluetoothDevice != null) {
                connect(bluetoothDevice);
                return super.onStartCommand(intent, flags, startId);
            }
        } else{
            startThread();
        }

        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.stop();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mIBinder;
    }

    public synchronized void startThread() {
        // dừng connect nếu connect thread đang chạy
        if (connectThread != null) {
            connectThread.cancel();
            connectThread = null;
        }

        // // dừng connect nếu connect thread đang chạy
        if (connectedThread != null) {
            connectedThread.cancel();
            connectedThread = null;
        }

        // Bắt đầu chờ yêu cầu connect
        if (acceptThread == null) {
            acceptThread = new AcceptThread();
            acceptThread.start();
        }

        state = MainActivity.STATE_LISTEN;
    }

    public synchronized void stop() {
        if (connectThread != null) {
            connectThread.cancel();
            connectThread = null;
        }

        if (connectedThread != null) {
            connectedThread.cancel();
            connectedThread = null;
        }

        if (acceptThread != null) {
            acceptThread.cancel();
            acceptThread = null;
        }
        state = MainActivity.STATE_NONE;
        // Cập nhật lại title của Main Activity
        updateUserInterfaceTitle();
    }

    private class AcceptThread extends Thread {
        // Sever socket local
        private final BluetoothServerSocket bluetoothServerSocket;

        public AcceptThread() {
            BluetoothServerSocket tmp = null;
            // Tạo nghe kết nối
            try {
                tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord("Game",
                        uuid);
            } catch (IOException e) {
            }
            bluetoothServerSocket = tmp;
        }

        public void run() {
            BluetoothSocket socket = null;

            // sever nghe yêu cầu kết nối
            try {
                socket = bluetoothServerSocket.accept();
            } catch (IOException e) {
            }

            // Nếu sever chấp nhận kết nối
            if (socket != null) {
                service_type = Constants.IS_SERVER;
                switch (state) {
                    case MainActivity.STATE_LISTEN:
                    case MainActivity.STATE_CONECTING:
                        connected(socket, socket.getRemoteDevice());
                        break;
                    case MainActivity.STATE_NONE:
                    case MainActivity.STATE_CONNECTED:
                        try {
                            socket.close();
                        } catch (IOException e) { }
                }
            }
        }

        public void cancel() {
            try {
                bluetoothServerSocket.close();
            } catch (IOException e) {
            }
        }
    }

    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            mmDevice = device;
            BluetoothSocket tmp = null;

            try {
                tmp = mmDevice.createRfcommSocketToServiceRecord(uuid);
            } catch (IOException e) { }
            mmSocket = tmp;
        }

        @Override
        public void run() {
            try {
                mmSocket.connect();
            } catch (IOException e) {
                try {
                    mmSocket.close();
                } catch (IOException e2) { }
                connectionFailed();
                return;
            }

            // Kết nối thành công
            service_type = Constants.IS_CLIENT;
            synchronized (BluetoothService.this) {
                connectThread = null;
            }

            // Bắt đầu luồng kết nối
            connected(mmSocket, mmDevice);
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }

    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final DataInputStream mmInStream;
        private final DataOutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // input và output stream gửi nhận tin nhắn
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = new DataInputStream(tmpIn);
            mmOutStream = new DataOutputStream(tmpOut);

        }

        public void run() {
            byte[] buffer = new byte[2048];
            int bytes;

            // Trong khi vẫn connect
            while (state == MainActivity.STATE_CONNECTED) {
                try {

                    // Đọc tin nhắn
                    bytes = mmInStream.read(buffer);
                    //3 byte đầu là message
                    String mMess = new String(buffer, 0, 3);
                    message = Integer.parseInt(mMess);
                    // Nội dung chính của message
                    byte[] message_info = new byte[bytes-3];
                    System.arraycopy(buffer, 3, message_info, 0, message_info.length);
                    //Kiem tra message
                    switch (message) {
                        case Constants.SET_MESSAGE_CHAT:
                            handlerService.obtainMessage(Constants.MESSAGE_READ, message_info.length, -1, message_info)
                                    .sendToTarget();
                            break;
                        case Constants.SET_NOTICE_ANSWER:
                            handlerService.obtainMessage(Constants.MESSAGE_ANSWER, message_info.length, -1, message_info).sendToTarget();
                            break;
                        case Constants.SET_READY_CLIENT:
                            handlerService.obtainMessage(Constants.MESSAGE_READY).sendToTarget();
                            break;
                        case Constants.SET_UNREADY_CLIENT:
                            handlerService.obtainMessage(Constants.MESSAGE_UNREADY).sendToTarget();
                            break;
                    }
                } catch (IOException e) {
                    connectionLost();
                    break;
                }
            }
        }

        /**
         * Write to the connected OutStream.
         *
         * @param buffer The bytes to write
         */
        public void write(byte[] buffer) {
            try {
                mmOutStream.write(buffer);

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }

    public synchronized void connect(BluetoothDevice device) {
        if (state == MainActivity.STATE_CONECTING) {
            if (connectThread != null) {
                connectThread.cancel();
                connectThread = null;
            }
        }

        connectThread = new ConnectThread(device);
        connectThread.start();

        state = MainActivity.STATE_CONECTING;

        updateUserInterfaceTitle();
    }

    public synchronized void connected(BluetoothSocket socket, BluetoothDevice device) {

        if (connectThread != null) {
            connectThread.cancel();
            connectThread = null;
        }
        if (connectedThread != null) {
            connectedThread.cancel();
            connectedThread = null;
        }

        if (acceptThread != null) {
            acceptThread.cancel();
            acceptThread = null;
        }

        state = MainActivity.STATE_CONNECTED;
        connectedThread = new ConnectedThread(socket);
        connectedThread.start();


        Message msg = handlerService.obtainMessage(Constants.MESSAGE_DEVICE_MAC);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.DEVICE_MAC, device.getAddress());
        msg.setData(bundle);
        handlerService.sendMessage(msg);
        updateUserInterfaceTitle();
    }


    private void connectionLost() {
        Message msg = handlerService.obtainMessage(Constants.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.TOAST, "Device connection was lost");
        msg.setData(bundle);
        handlerService.sendMessage(msg);

        state = MainActivity.STATE_NONE;
        updateUserInterfaceTitle();

        BluetoothService.this.startThread();
    }

    private void connectionFailed() {
        Message msg = handlerService.obtainMessage(Constants.MESSAGE_TOAST);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.TOAST, "Unable to connect device");
        msg.setData(bundle);
        handlerService.sendMessage(msg);

        state = MainActivity.STATE_NONE;
        updateUserInterfaceTitle();

        BluetoothService.this.startThread();
    }

    private synchronized void updateUserInterfaceTitle() {
        state = getState();


        handlerService.obtainMessage(Constants.MESSAGE_STATE_CHANGE, state, -1).sendToTarget();
    }

    public void write(byte[] out) {
        ConnectedThread r;
        synchronized (this) {
            if (state != MainActivity.STATE_CONNECTED) return;
            r = connectedThread;
        }

        String mess = String.valueOf(message);
        byte[] bMess = mess.getBytes();

        byte[] destination = new byte[out.length + bMess.length];
        System.arraycopy(bMess,0,destination,0,bMess.length);
        System.arraycopy(out,0,destination,bMess.length,out.length);
        r.write(destination);
    }

    public synchronized int getState() {
        return state;
    }

    private void sendMessage(String message) {
        if (state != MainActivity.STATE_CONNECTED) {
            Message msg = handlerService.obtainMessage(Constants.MESSAGE_TOAST);
            Bundle bundle = new Bundle();
            bundle.putString(Constants.TOAST, "Device connection was lost");
            msg.setData(bundle);
            handlerService.sendMessage(msg);
            return;
        }

        if (message.length() > 0) {
            byte[] send = message.getBytes();
            write(send);
        }
    }

    public class LocalBinder extends Binder
    {
        public BluetoothService getInstance()
        {
            return BluetoothService.this;
        }
    }

    public void setHandler(Handler handlerService)
    {
        this.handlerService = handlerService;
    }

    public Handler getHandler(){
        return this.handlerService;
    }

    public void receiveMessage(int message, String info) {
        this.message = message;
        switch (message) {
            case Constants.SET_MESSAGE_CHAT:
                write(info.getBytes());
                break;
            case Constants.SET_NOTICE_ANSWER:
                write(info.getBytes());
                break;
            case Constants.SET_READY_CLIENT:
                write(info.getBytes());
                break;
            case Constants.SET_UNREADY_CLIENT:
                write(info.getBytes());
                break;
        }
    }

    public int getService_type() {
        return service_type;
    }

    public void setService_type(int service_type) {
        this.service_type = service_type;
    }
}
