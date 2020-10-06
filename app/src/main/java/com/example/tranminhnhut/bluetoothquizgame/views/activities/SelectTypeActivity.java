package com.example.tranminhnhut.bluetoothquizgame.views.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.tranminhnhut.bluetoothquizgame.R;
import com.example.tranminhnhut.bluetoothquizgame.presenters.SelectTypePresenter;
import com.example.tranminhnhut.bluetoothquizgame.views.interfaces.SelectTypeView;

import java.util.ArrayList;

public class SelectTypeActivity extends Activity implements SelectTypeView {
    //Presenter của selectType
    private SelectTypePresenter selectTypePresenter;
    ArrayList<CheckBox> checkBoxArrayList;
    Button ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void initialize(){
        setContentView(R.layout.select_type);
        checkBoxArrayList = new ArrayList<CheckBox>();

        CheckBox math = (CheckBox)findViewById(R.id.math);
        CheckBox chemistry = (CheckBox) findViewById(R.id.chemistry);
        CheckBox physics = (CheckBox) findViewById(R.id.physics);

        checkBoxArrayList.add(math);
        checkBoxArrayList.add(chemistry);
        checkBoxArrayList.add(physics);

        ok = (Button) findViewById(R.id.bttnOK);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxArrayList.size() == 0){
                    Toast.makeText(getApplicationContext(), "You need choose at least 1 type", Toast.LENGTH_LONG);
                } else {
                    //Đưa danh sách thể loại cho select presenter
                    for (int i = 0; i < checkBoxArrayList.size(); i++) {
                        if (checkBoxArrayList.get(i).isChecked()) {
                            selectTypePresenter.addType(checkBoxArrayList.get(i).getText());
                        }
                        checkBoxArrayList.get(i).setEnabled(false);
                    }

                    selectTypePresenter.notifyDialog();
                    ok.setEnabled(false);
                }
            }
        });

        selectTypePresenter = new SelectTypePresenter(this);
    }

    @Override
    public Context getContext() {
        return this;
    }
}
