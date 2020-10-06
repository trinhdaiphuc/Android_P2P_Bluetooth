package com.example.tranminhnhut.bluetoothquizgame.models.handlesModels;

import com.example.tranminhnhut.bluetoothquizgame.models.QuestionModel;
import com.example.tranminhnhut.bluetoothquizgame.models.databases.QuestionDatabase;
import com.example.tranminhnhut.bluetoothquizgame.presenters.interfaces.QuizListener;

import java.util.ArrayList;
import java.util.Random;

public class QuizHandle {
    //Presenter của quiz
    private QuizListener quizListener;
    //Database
    private QuestionDatabase questionDatabase;
    //Danh sách thể loại mà thiết bị server chọn
    private ArrayList<String> listType;
    //Question đã xuất hiện
    private ArrayList<String> ID_Question;

    public QuizHandle(QuizListener quizListener) {
        this.quizListener = quizListener;
        questionDatabase = new QuestionDatabase(quizListener._getContext());
        listType = new ArrayList<>(quizListener.getListType());
        ID_Question = new ArrayList<String>();
    }

    //Get random question
    public void getQuestion(){
        //Random question and type
        Random random = new Random();
        //Question
        int question = random.nextInt(QuestionDatabase.TOTAL_QUESTION + 1);
        //type
        int type = random.nextInt(QuestionDatabase.TOTAL_TYPE);

        QuestionModel questionModel = questionDatabase.getQuestion(question, listType.get(type));
        while (ID_Question.contains(questionModel.getID_Question())){
            question = random.nextInt(QuestionDatabase.TOTAL_QUESTION + 1);
            type = random.nextInt(QuestionDatabase.TOTAL_TYPE);
            questionModel = questionDatabase.getQuestion(question, listType.get(type));
        }
        ID_Question.add(questionModel.getID_Question());

        quizListener.onLoadQuestion(questionModel);
    }
}
