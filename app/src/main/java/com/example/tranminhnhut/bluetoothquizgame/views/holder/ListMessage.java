package com.example.tranminhnhut.bluetoothquizgame.views.holder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.tranminhnhut.bluetoothquizgame.models.MessageModel;
import com.example.tranminhnhut.bluetoothquizgame.R;

import java.util.ArrayList;

public class ListMessage extends ArrayAdapter<MessageModel> {
    private static String TAG = "Message adapter";
    private Context mcontext;

    public ListMessage(Context context, int resource, ArrayList<MessageModel> objects){
        super(context,resource,objects);
        mcontext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String value = getItem(position).getMessage();
        int layout = getItem(position).getLayout();

        LayoutInflater inflater = LayoutInflater.from(mcontext);
        convertView = inflater.inflate(layout,parent,false);

        TextView tv;
        if (layout == R.layout.message_view_friend)
            tv = (TextView) convertView.findViewById(R.id.text_friend);
        else
            tv = (TextView) convertView.findViewById(R.id.text_user);

        tv.setText(value);

        return convertView;
    }
}
