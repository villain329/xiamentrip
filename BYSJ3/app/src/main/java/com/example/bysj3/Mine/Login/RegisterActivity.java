package com.example.bysj3.Mine.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bysj3.R;
import com.example.bysj3.Util.HttpLogin;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private Button bt_mine_register_back;
    private EditText ed_mine_register_account;
    private EditText ed_mine_register_password;
    private EditText ed_mine_register_user;
    private Button bt_mine_register_complete;

    private int ResultCode = 2;
    private final static int REGISTER_JUDGE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //沉浸式状态栏
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.WHITE);
        }

        setContentView(R.layout.activity_register);

        bindView();
    }

    private void bindView() {
        bt_mine_register_back = findViewById(R.id.bt_mine_register_back);
        bt_mine_register_complete = findViewById(R.id.bt_mine_register_complete);
        ed_mine_register_account = findViewById(R.id.ed_mine_register_account);
        ed_mine_register_password = findViewById(R.id.ed_mine_register_password);
        ed_mine_register_user = findViewById(R.id.ed_mine_register_user);

        bt_mine_register_back.setOnClickListener(this);
        bt_mine_register_complete.setOnClickListener(this);
    }


    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {

            switch (msg.what) {
                case REGISTER_JUDGE: {
                    Bundle bundle = new Bundle();
                    bundle = msg.getData();
                    String result = bundle.getString("result");
                    //Toast.makeText(MainActivity.this,result,Toast.LENGTH_SHORT).show();
                    try {
                        if (result.equals("success")) {
                            Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            intent.putExtra("user_id", ed_mine_register_account.getText().toString());
                            intent.putExtra("password", ed_mine_register_password.getText().toString());
                            setResult(ResultCode, intent);//向上一级发送数据
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, "账号已被注册！", Toast.LENGTH_SHORT).show();

                        }
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
                break;
                case 1:
                    Toast.makeText(RegisterActivity.this, "请输入完整信息！", Toast.LENGTH_SHORT).show();

                    break;
            }
            return false;
        }
    });

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_mine_register_back:
                finish();
                break;

            case R.id.bt_mine_register_complete:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (ed_mine_register_account.getText().toString().equals("")
                                || ed_mine_register_password.getText().toString().equals("")
                                || ed_mine_register_user.getText().toString().equals("")) {
                            Message msg = new Message();
                            msg.what = 1;
                            handler.sendMessage(msg);
                        } else {
                            String result = HttpLogin.RegisterByPost(ed_mine_register_account.getText().toString(),
                                    ed_mine_register_password.getText().toString(),
                                    ed_mine_register_user.getText().toString(),
                                    "http://39.97.251.81:8080/picture/IMG_DB91182E5541ABF37C78A7A6B98B32.jpeg".trim()
                            );
                            Bundle bundle = new Bundle();
                            bundle.putString("result", result);
                            Message msg = new Message();
                            msg.what = REGISTER_JUDGE;
                            msg.setData(bundle);
                            handler.sendMessage(msg);
                        }
                    }
                }).start();
                break;
        }
    }
}
