package com.example.tranminhnhut.bluetoothquizgame.presenters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;


import com.example.tranminhnhut.bluetoothquizgame.Constants;
import com.example.tranminhnhut.bluetoothquizgame.views.interfaces.SelectTypeView;

import java.util.ArrayList;

public class SelectTypePresenter {
    private SelectTypeView selectTypeView;
    //Context của SelectTypeActivity
    private Context context;
    private ArrayList<CharSequence> type;

    public SelectTypePresenter(SelectTypeView selectTypeView){
        this.selectTypeView = selectTypeView;
        context = selectTypeView.getContext();
        type = new ArrayList<CharSequence>();
    }

    public void addType(CharSequence type){
        this.type.add(type);
    }

    public void notifyDialog(){
       gotoQuiz();
       /*    AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Bạn có chắc chắn chọn những thể loại này ?");
        builder.setTitle("Notify");
        builder.setCancelable(false);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Message message = handlerService.obtainMessage(Constants.SET_TYPE_QUESTION);
                Bundle bundle = new Bundle();
                bundle.putCharSequenceArrayList(Constants.TYPE_QUESTION, type);
                message.setData(bundle);
                handlerService.sendMessage(message);

                isSend = true;
                if (isReceived)
                    gotoQuiz();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isSend = false;
                isReceived = false;
                type.clear();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();*/
    }

    public void gotoQuiz(){
        Intent intent = new Intent();
        intent.putExtra(Constants.TYPE_QUESTION, type);
        ((Activity) context).setResult(Constants.GET_TYPE_SUCCESS, intent);
        ((Activity)context).finish();
    }
}
