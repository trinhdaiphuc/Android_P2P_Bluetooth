package com.example.tranminhnhut.bluetoothquizgame.views.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tranminhnhut.bluetoothquizgame.Constants;
import com.example.tranminhnhut.bluetoothquizgame.presenters.ChatPresenter;
import com.example.tranminhnhut.bluetoothquizgame.views.holder.ListMessage;
import com.example.tranminhnhut.bluetoothquizgame.models.MessageModel;
import com.example.tranminhnhut.bluetoothquizgame.R;

import java.util.ArrayList;

// Chat list
public class FragmentChat extends Fragment {
    //Presenter của chat
    private ChatPresenter chatPresenter;
    //Danh sách message
    private ArrayList<MessageModel> messageModels;
    private ListView messageList;

    ImageButton imageButton;
    EditText editText;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        messageModels = new ArrayList<MessageModel>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.message_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        editText = (EditText) view.findViewById(R.id.input_text);
        imageButton = (ImageButton) view.findViewById(R.id.bttnSend);
        messageList = (ListView) view.findViewById(R.id.message_list);
    }

    @Override
    public void onStart() {
        super.onStart();
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getView();
                if (view != null){
                    //Đưa xml view_user và nội dung message cho messageModels
                    TextView textView = (TextView) view.findViewById(R.id.input_text);
                    String message = textView.getText().toString();
                    editText.setText("");
                    MessageModel m = new MessageModel(message, R.layout.message_view_user);
                    messageModels.add(m);
                    ListMessage listMessage = new ListMessage(getContext(), -1, messageModels);
                    messageList.setAdapter(listMessage);
                    //Gửi nội dung tin nhắn cho máy bạn
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.WRITE_MESSAGE, message);
                    chatPresenter.sendToHandlerService(Constants.MESSAGE_WRITE_TO_SERVICE, bundle);

                }
            }
        });
    }

    public void insertMessage(String message){
        //Đưa xml view_friend và nhận nội dung message từ máy bạn đưa vào messageModels
        MessageModel m = new MessageModel(message, R.layout.message_view_friend);
        messageModels.add(m);
        ListMessage listMessage = new ListMessage(getContext(), -1, messageModels);
        messageList.setAdapter(listMessage);
    }
    //Lấy handler của message service đưa cho presenter
    public void setHandlerService(Handler messService) {
        chatPresenter = new ChatPresenter(messService);
    }

}
