package com.example.tranminhnhut.bluetoothquizgame.presenters;

import android.os.Bundle;
import android.os.Message;

import android.os.Handler;

public class ChatPresenter {
    private Handler handlerService;

    public ChatPresenter(Handler messService) {
        this.handlerService = messService;
    }

    //View gửi yêu cầu đến MessageService
    public void sendToHandlerService(int notice, Bundle bundle){
        if (bundle == null){
            Handler handler;
            handlerService.obtainMessage(notice).sendToTarget();
        } else{
            Message message = handlerService.obtainMessage(notice);
            message.setData(bundle);
            handlerService.sendMessage(message);
        }
    }
}
