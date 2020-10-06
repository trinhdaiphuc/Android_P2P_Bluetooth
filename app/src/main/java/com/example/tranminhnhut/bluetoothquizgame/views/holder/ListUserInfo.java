package com.example.tranminhnhut.bluetoothquizgame.views.holder;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.tranminhnhut.bluetoothquizgame.models.UserModel;
import com.example.tranminhnhut.bluetoothquizgame.R;

import java.util.ArrayList;

public class ListUserInfo extends ArrayAdapter<UserModel> {

    private static String TAG = "Message adapter";
    private Context mcontext;
    private int mresource;

    public ListUserInfo(Context context, int resource, ArrayList<UserModel> objects) {
        super(context, resource, objects);
        mcontext = context;
        mresource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String Name = getItem(position).getComponentName();
        String Result = getItem(position).getResult();

        LayoutInflater inflater = LayoutInflater.from(mcontext);
        convertView = inflater.inflate(mresource,parent,false);

        TextView tv1 = (TextView) convertView.findViewById(R.id.textView1);
        TextView tv2 = (TextView) convertView.findViewById(R.id.textView2);

        tv1.setText(Name);
        tv2.setText(Result);

        tv1.setTextColor(Color.rgb(255, 255,255));
        tv2.setTextColor(Color.rgb(255, 255,255));

        return convertView;
    }
}
