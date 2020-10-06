package com.example.tranminhnhut.bluetoothquizgame.presenters.interfaces;

import android.content.Context;

import com.example.tranminhnhut.bluetoothquizgame.models.AnswerModel;
import com.example.tranminhnhut.bluetoothquizgame.models.CorrectAnswerModel;
import com.example.tranminhnhut.bluetoothquizgame.models.QuestionModel;

import java.util.ArrayList;

public interface QuizListener {
    ArrayList<String> getListType();
    Context _getContext();
    void onLoadQuestion(QuestionModel questionModel);
    void onLoadAnswer(ArrayList<AnswerModel> answerModels);
    void onLoadCorrectAnswer(CorrectAnswerModel correctAnswerModel);
}
