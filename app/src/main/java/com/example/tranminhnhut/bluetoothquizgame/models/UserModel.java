package com.example.tranminhnhut.bluetoothquizgame.models;

public class UserModel {
    private String componentName;
    private String result;

    public UserModel() {
    }

    public UserModel(String componentName, String result) {
        this.componentName = componentName;
        this.result = result;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
