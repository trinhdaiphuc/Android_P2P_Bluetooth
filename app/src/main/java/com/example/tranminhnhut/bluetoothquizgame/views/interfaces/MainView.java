package com.example.tranminhnhut.bluetoothquizgame.views.interfaces;

import android.content.Context;

public interface MainView {
    Context getContext();
    Boolean isHostready();
    Boolean isClientready();
    void setHost(Boolean isReady);
    void setClient(Boolean isReady);
    void setsubTitle(String title);
    void setState(int state);
    void setVisibleTextview(int isVisible);
    void bothDevice_ready();
    void bothDevice_notready();
}
