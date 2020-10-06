package com.example.tranminhnhut.bluetoothquizgame.presenters;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.example.tranminhnhut.bluetoothquizgame.Constants;
import com.example.tranminhnhut.bluetoothquizgame.models.AnswerModel;
import com.example.tranminhnhut.bluetoothquizgame.models.CorrectAnswerModel;
import com.example.tranminhnhut.bluetoothquizgame.models.QuestionModel;
import com.example.tranminhnhut.bluetoothquizgame.presenters.interfaces.QuizListener;
import com.example.tranminhnhut.bluetoothquizgame.presenters.services.MessageService;
import com.example.tranminhnhut.bluetoothquizgame.views.interfaces.QuizView;

import java.util.ArrayList;

public class QuizPresenter implements QuizListener {
    private Boolean iBound = false;
    private QuizView quizView;
    private Context context;
    private Handler handlerService;
    private MessageService messageService;
    private ArrayList<String> typeQuestion;

    //Service Connection với Message Service
    private ServiceConnection messageConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder)
        {
            messageService = ((MessageService.LocalBinder)iBinder).getInstance();
            //set handler cua quiz cho message service
            messageService.setHandlerQuiz(handler);
            //Get handler cua message service
            handlerService = messageService.getHandler();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName)
        {
            messageService = null;
        }
    };

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Constants.NOTICE_ANSWER:
                   quizView.receiverNoticyAnswer();
                   break;
            }
        }
    };

    public QuizPresenter(QuizView quizView) {
        this.quizView = quizView;
        context = quizView.getContext();
    }

    public void initialize(){
        context.bindService(new Intent(context,
                MessageService.class), messageConnection, Context.BIND_AUTO_CREATE);
        this.iBound = true;
    }

    public void unRegister(){
        if (iBound){
            context.unbindService(messageConnection);
            iBound = false;
        }
    }

    public void sendToHandlerService(int notice, Bundle bundle){
        if (bundle == null){
           handlerService.obtainMessage(notice).sendToTarget();
        } else{
            Message message = handlerService.obtainMessage(notice);
            message.setData(bundle);
            handlerService.sendMessage(message);
        }
    }

    public void setTypeQuestion(ArrayList<String> typeQuestion) {
        this.typeQuestion = new ArrayList<>(typeQuestion);
    }

    @Override
    public ArrayList<String> getListType() {
        return this.typeQuestion;
    }

    @Override
    public Context _getContext() {
        return context;
    }

    @Override
    public void onLoadAnswer(ArrayList<AnswerModel> answerModels) {

    }

    @Override
    public void onLoadCorrectAnswer(CorrectAnswerModel correctAnswerModel) {

    }

    @Override
    public void onLoadQuestion(QuestionModel questionModel) {

    }
}
