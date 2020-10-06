package com.example.tranminhnhut.bluetoothquizgame.views.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.tranminhnhut.bluetoothquizgame.Constants;
import com.example.tranminhnhut.bluetoothquizgame.R;
import com.example.tranminhnhut.bluetoothquizgame.presenters.QuizPresenter;
import com.example.tranminhnhut.bluetoothquizgame.views.interfaces.QuizView;

import java.util.ArrayList;

public class QuizActivity extends Activity implements QuizView {

    private QuizPresenter quizPresenter;
    private ImageButton answerButton;
    private ArrayList<CharSequence> typeList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Lấy danh sách thể loại
        typeList = getIntent().getCharSequenceArrayListExtra(Constants.TYPE_QUESTION);
        initializeQuiz();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //if (resultCode == Activity.RESULT_OK){

        //}
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        quizPresenter.unRegister();
    }

    public void initializeQuiz(){
        setContentView(R.layout.quiz_view);
        answerButton = (ImageButton)findViewById(R.id.answer);
        answerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answerButton.setClickable(false);
                Bundle bundle = new Bundle();
                bundle.putString(Constants.WANT_ANSWER, Constants.ANSWER);
                quizPresenter.sendToHandlerService(Constants.NOTICE_ANSWER, bundle);
            }
        });

        quizPresenter = new QuizPresenter(this);
        quizPresenter.initialize();
    }

    @Override
    public void receiverNoticyAnswer() {
        answerButton.setClickable(false);
        Toast.makeText(getApplicationContext(),"Yes yeah yeah", Toast.LENGTH_SHORT).show();
    }

    @Override
    public Context getContext() {
        return this;
    }
}
