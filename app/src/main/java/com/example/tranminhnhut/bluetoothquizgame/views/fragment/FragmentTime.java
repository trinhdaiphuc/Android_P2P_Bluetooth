package com.example.tranminhnhut.bluetoothquizgame.views.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tranminhnhut.bluetoothquizgame.R;

//Bộ đếm
public class FragmentTime extends Fragment {

    private CountDownTimer countDownTimer;
    private int totalTime;
    private int waitTime;

    TextView timeView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        totalTime = 0;
        waitTime = 0;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.time_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        timeView = (TextView)view.findViewById(R.id.time);
    }

    public void initialize(){
        countDownTimer = new CountDownTimer(totalTime, waitTime) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeView.setText(String.valueOf(millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {
                timeView.setText("Time out!!!!");
            }
        };
    }

    public void start(){
        countDownTimer.start();
    }

    public void stop(){
        countDownTimer.cancel();
    }

    public void setTime(int total, int wait){
        this.totalTime = total;
        this.waitTime = wait;
        this.initialize();
    }
}
