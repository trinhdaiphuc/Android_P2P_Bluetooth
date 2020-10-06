package com.example.tranminhnhut.bluetoothquizgame.models;

public class CorrectAnswerModel {
    private String ID_Question;
    private String ID_Correct;

    public CorrectAnswerModel() {
    }

    public CorrectAnswerModel(String ID_Question, String ID_Correct) {
        this.ID_Question = ID_Question;
        this.ID_Correct = ID_Correct;
    }

    public void setID_Question(String ID_Question) {
        this.ID_Question = ID_Question;
    }

    public void setID_Correct(String ID_Correct) {
        this.ID_Correct = ID_Correct;
    }

    public String getID_Question() {

        return ID_Question;
    }

    public String getID_Correct() {
        return ID_Correct;
    }
}
