package com.example.tranminhnhut.bluetoothquizgame;

public interface Constants {
    public static final int IS_SERVER = -1;
    public static final int IS_CLIENT = 0;

    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_MAC = 4;
    public static final int MESSAGE_TOAST = 5;
    public static final int MESSAGE_WRITE_TO_SERVICE = 6;
    public static final int MESSAGE_ANSWER = 7;
    public static final int MESSAGE_READY = 8;
    public static final int MESSAGE_UNREADY = 9;


    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_MAC = "device_mac";
    public static final String TOAST = "toast";
    public static final String WRITE_MESSAGE = "message write";

    // Key for services
    public static final int SET_MESSAGE_CHAT = 101;
    public static final int SET_NOTICE_ANSWER = 102;
    public static final int SET_READY_CLIENT = 103;
    public static final int SET_UNREADY_CLIENT = 104;

    //Key for handler activity
    public static final int MESSAGE_SET_TITLE = 300;
    public static final int MESSAGE_READY_SUCCESS = 301;
    public static final int MESSAGE_READY_INTERRUPT = 302;
    public static final int GET_TYPE_SUCCESS = 303;
    public static final String SET_TITLE = "set title";

    //Key for Quiz
    public static final int NOTICE_ANSWER = 400;
    public static final String WANT_ANSWER = "want to answer from pair device";
    public static final String ANSWER = "TRUE";
    public static final String TYPE_QUESTION = "list_type";

}
