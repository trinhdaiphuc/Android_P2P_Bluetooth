package com.example.tranminhnhut.bluetoothquizgame.models;

public class AnswerModel {
    private String ID_Question;
    private String ID_Answer;
    private String info;

    public AnswerModel(String ID_Question, String ID_Answer, String info) {
        this.ID_Question = ID_Question;
        this.ID_Answer = ID_Answer;
        this.info = info;
    }

    public AnswerModel() {
    }

    public void setID_Question(String ID_Question) {
        this.ID_Question = ID_Question;
    }

    public void setID_Answer(String ID_Answer) {
        this.ID_Answer = ID_Answer;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getID_Question() {
        return ID_Question;
    }

    public String getID_Answer() {
        return ID_Answer;
    }

    public String getInfo() {
        return info;
    }
}

