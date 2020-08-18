package com.example.bysj3.Mine;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bysj3.Data.SharedHelper;
import com.example.bysj3.Mine.Login.LoginActivity;
import com.example.bysj3.MyAPPlication;
import com.example.bysj3.R;

public class MineSettingActivity extends AppCompatActivity implements View.OnClickListener {

    private Button bt_mine_setting_back;
    private TextView tv_mine_setting_sign_out;
    private TextView tv_mine_setting_account_name;

    private SharedHelper sh;
    private Context mContext;
    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;
    private static final int UPDATA_ID = 8;

    public Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case UPDATA_ID:
                    if (MyAPPlication.getGlobalvariable() == null) {
                        tv_mine_setting_account_name.setText("请登录");
                    } else {
                        tv_mine_setting_account_name.setText(MyAPPlication.getGlobalvariable());
                    }
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //沉浸式状态栏
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.WHITE);
        }

        setContentView(R.layout.activity_mine_setting);



        bindView();
        updataid();


    }

    private void updataid() {
        mContext = MineSettingActivity.this;
        Message message = new Message();
        message.what = UPDATA_ID;
        handler.sendMessage(message);
    }

    private void bindView() {
        bt_mine_setting_back = findViewById(R.id.bt_mine_setting_back);
        tv_mine_setting_sign_out = findViewById(R.id.tv_mine_setting_sign_out);
        tv_mine_setting_account_name = findViewById(R.id.tv_mine_setting_account_name);

        bt_mine_setting_back.setOnClickListener(this);
        tv_mine_setting_sign_out.setOnClickListener(this);
        tv_mine_setting_account_name.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_mine_setting_back:
                finish();
                break;

            case R.id.tv_mine_setting_sign_out:
                alert = null;
                builder = new AlertDialog.Builder(mContext);
                alert = builder.setTitle("提示：")
                        .setMessage("是否要退出当前账号？")
                        .setCancelable(false)
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//
                               SharedPreferences sharedPreferences=mContext.getSharedPreferences("login",Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor=sharedPreferences.edit();
                                editor.clear();
                                editor.commit();
                                Intent intent = new Intent(MineSettingActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }).create();
                alert.show();
                break;

        }
    }


}

