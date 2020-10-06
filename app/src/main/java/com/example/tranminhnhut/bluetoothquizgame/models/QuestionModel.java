package com.example.tranminhnhut.bluetoothquizgame.models;

public class QuestionModel {
    private String ID_Question;
    private String info;
    private int pos;
    private String type;

    public QuestionModel(String ID_Question, String info, int pos, String type) {
        this.ID_Question = ID_Question;
        this.info = info;
        this.pos = pos;
        this.type = type;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public String getID_Question() {
        return ID_Question;
    }

    public String getInfo() {
        return info;
    }

    public void setID_Question(String ID_Question) {
        this.ID_Question = ID_Question;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public QuestionModel() {
    }
}

