package com.example.bysj3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.bysj3.Data.Msg;
import com.example.bysj3.Util.HttpUtilSearch;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleWithBorderTransformation;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    public String user;
    public String user_id;
    public String user_image;
    private String url2 = "http://39.97.251.81:8080/test5/GetIdServlet";


    private List<Msg> msgList = new ArrayList<Msg>();

    private Button bt_chat_back;

    private EditText ed_chat;
    private TextView tv_chat_user_two;

    private TextView tv_send;

    private RecyclerView msgRecyclerView;

    private MsgAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        bindview();
        okhttpuser_two();


        initMsgs(); // 初始化消息数据
        ed_chat = findViewById(R.id.ed_chat);
        tv_send = findViewById(R.id.tv_chat);
        msgRecyclerView = findViewById(R.id.rv_chat);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(layoutManager);
        adapter = new MsgAdapter(msgList);
        msgRecyclerView.setAdapter(adapter);
        tv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = ed_chat.getText().toString();
                if (!"".equals(content)) {
                    Msg msg = new Msg(content, Msg.TYPE_SENT);
                    msgList.add(msg);
                    adapter.notifyItemInserted(msgList.size() - 1); // 当有新消息时，刷新ListView中的显示
                    msgRecyclerView.scrollToPosition(msgList.size() - 1); // 将ListView定位到最后一行
                    ed_chat.setText(""); // 清空输入框中的内容
                }
            }
        });



    }


    private void okhttpuser_two() {
        String anydata = getIntent().getStringExtra("user_id");
        HttpUtilSearch.sendOkHttpRequest(url2, anydata, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseData = response.body().string();
                jsonJXDate(responseData);
            }

            private void jsonJXDate(String responseData) {
                if (responseData != null) {
                    try {
                        JSONArray array = new JSONArray(responseData);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            user_id = object.getString("user_id");
                            user = object.getString("user");
                            user_image = object.getString("user_image");
                        }
                        ;
                        Message msg = new Message();
                        msg.what = 1;
                        handler.sendMessage(msg);
                        Log.e("user", "chat_user: " + user);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    public Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    tv_chat_user_two.setText(user);
                    break;
            }
            return false;
        }
    });


    private void initMsgs() {

        msgList.add(new Msg("你好", Msg.TYPE_RECEIVED));
        Msg msg2 = new Msg("你好", Msg.TYPE_SENT);
        msgList.add(msg2);
        Msg msg3 = new Msg("谢谢 ", Msg.TYPE_RECEIVED);
        msgList.add(msg3);
    }

    private void bindview() {
        bt_chat_back = findViewById(R.id.bt_chat_back);
        tv_chat_user_two = findViewById(R.id.tv_chat_user_two);

        bt_chat_back.setOnClickListener(this);
        tv_chat_user_two.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_chat_back:
                finish();
                break;

        }
    }
}