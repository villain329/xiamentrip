package com.example.bysj3;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bysj3.Util.HttpComment;
import com.example.bysj3.Util.HttpUtilSearch;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CommentRaidersAcyivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_comment;
    private EditText ed_comment;

    public String user;
    public String user_id;
    public String user_image;
    private String url2 = "http://39.97.251.81:8080/test5/GetIdServlet";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        okhttpuser();
        bindview();


    }

    private void bindview() {
        ed_comment = findViewById(R.id.ed_comment);
        tv_comment = findViewById(R.id.tv_comment);

        ed_comment.setOnClickListener(this);
        tv_comment.setOnClickListener(this);

    }

    private void okhttpuser() {
        String anydata = MyAPPlication.getGlobalvariable();
        Log.e("TAG", "commentactivity: "+anydata );
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
                case 4:
                    Bundle bundle;
                    bundle = msg.getData();
                    String result = bundle.getString("result");
                    Toast.makeText(CommentRaidersAcyivity.this, result, Toast.LENGTH_SHORT).show();
                        finish();

                    break;
            }
            return false;
        }
    });


//    //点击其他地方隐藏软键盘
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//            View v = getCurrentFocus();
//            if (isShouldHideInput(v, ev)) {
//
//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                if (imm != null) {
//                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//                }
//            }
//            return super.dispatchTouchEvent(ev);
//        }
//        // 必不可少，否则所有的组件都不会有TouchEvent了
//        if (getWindow().superDispatchTouchEvent(ev)) {
//            return true;
//        }
//        return onTouchEvent(ev);
//    }
//
//    public boolean isShouldHideInput(View v, MotionEvent event) {
//        if (v != null && (v instanceof EditText)) {
//            int[] leftTop = {0, 0};
//            //获取输入框当前的location位置
//            v.getLocationInWindow(leftTop);
//            int left = leftTop[0];
//            int top = leftTop[1];
//            int bottom = top + v.getHeight();
//            int right = left + v.getWidth();
//            if (event.getX() > left && event.getX() < right
//                    && event.getY() > top && event.getY() < bottom) {
//                // 点击的是输入框区域，保留点击EditText的事件
//                return false;
//            } else {
//                finish();
//                return true;
//            }
//        }
//        return false;
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_comment:
                String comment = ed_comment.getText().toString().trim();
                if (comment.length() == 0) {
                    Toast.makeText(CommentRaidersAcyivity.this, "请输入内容 ", Toast.LENGTH_SHORT).show();
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String result = HttpComment.AddRaidersData(
                                    getIntent().getStringExtra("article_id_second").trim(),
                                    user_id.trim(),
                                    user.trim(),
                                    ed_comment.getText().toString().trim(),
                                    user_image.trim()
                            );
                            Bundle bundle = new Bundle();
                            bundle.putString("result", result);
                            Log.e("4", "commentactivity: "+bundle );
                            Message msg = new Message();
                            msg.what = 4;
                            msg.setData(bundle);
                            handler.sendMessage(msg);
                        }
                    }).start();
                }
                break;
        }
    }
}