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
import com.example.tranminhnhut.bluetoothquizgame.R;

import com.example.tranminhnhut.bluetoothquizgame.models.Bluetooth;

import java.util.ArrayList;

public class ListBluetooth extends ArrayAdapter<Bluetooth> {
    private static String TAG = "Bluetooth adapter";
    private Context mcontext;
    int mresource;

    public ListBluetooth(Context context, int resource, ArrayList<Bluetooth> objects){
        super(context,resource,objects);
        mcontext = context;
        mresource = resource;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String name = getItem(position).getName();
        String Mac = getItem(position).getMac();

        Bluetooth bt = new Bluetooth(name,Mac);

        LayoutInflater inflater = LayoutInflater.from(mcontext);
        convertView = inflater.inflate(mresource,parent,false);

        TextView tv1 = (TextView) convertView.findViewById(R.id.textView1);
        TextView tv2 = (TextView) convertView.findViewById(R.id.textView2);

        tv1.setText(name);
        tv2.setText(Mac);

        tv1.setTextColor(Color.rgb(255, 255,255));
        tv2.setTextColor(Color.rgb(255, 255,255));

        return convertView;
    }
}
