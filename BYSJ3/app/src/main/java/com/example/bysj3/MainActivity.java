package com.example.bysj3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.example.bysj3.Home.HomeFragment;
import com.example.bysj3.Message.MessageFragment;
import com.example.bysj3.Message.MessageFragmentOne;
import com.example.bysj3.Mine.Login.LoginActivity;
import com.example.bysj3.Mine.MineFragment;
import com.example.bysj3.Plus.PlusActivity;
import com.example.bysj3.Raiders.RaidersFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {



    private HomeFragment homeFragment;
    private RaidersFragment raidersFragment;
    private MessageFragmentOne messageFragment;
    private MineFragment mineFragment;

    private TextView tv_main_home;
    private TextView tv_main_raiders;
    private TextView tv_main_plus;
    private TextView tv_main_message;
    private TextView tv_main_mine;
    String variable;
    private int RequestCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //沉浸式状态栏
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        setContentView(R.layout.activity_main);

        //加号切换活动事件
        tv_main_plus = findViewById(R.id.tv_main_plus);
        tv_main_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PlusActivity.class);
                startActivityForResult(intent,RequestCode);
            }
        });



isregister();
        //初始化控件
        bindView();

        //HomeFragment加入MainActivity
        homeFragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fl_main, homeFragment).commitAllowingStateLoss();
        tv_main_home.setSelected(true);
    }



    private void isregister() {
        if (MyAPPlication.getGlobalvariable() == null){
        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
        startActivity(intent);
        }else {

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 2){

        }
    }

    private void bindView() {
        //找到控件
        tv_main_home = findViewById(R.id.tv_main_home);
        tv_main_raiders = findViewById(R.id.tv_main_raiders);
        tv_main_message = findViewById(R.id.tv_main_message);
        tv_main_mine = findViewById(R.id.tv_main_mine);

        //设置监听事件
        tv_main_home.setOnClickListener(this);
        tv_main_raiders.setOnClickListener(this);
        tv_main_message.setOnClickListener(this);
        tv_main_mine.setOnClickListener(this);
    }

    private void setSelected(){
        tv_main_home.setSelected(false);
        tv_main_raiders.setSelected(false);
        tv_main_message.setSelected(false);
        tv_main_mine.setSelected(false);
    }

    public void onClick(View v) {
        if (homeFragment != null) {
            setSelected();
            getSupportFragmentManager().beginTransaction().hide(homeFragment).commitAllowingStateLoss();
        }
        if (raidersFragment != null) {
            setSelected();
            getSupportFragmentManager().beginTransaction().hide(raidersFragment).commitAllowingStateLoss();
        }
        if (messageFragment != null) {
            setSelected();
            getSupportFragmentManager().beginTransaction().hide(messageFragment).commitAllowingStateLoss();
        }
        if (mineFragment != null) {
            setSelected();
            getSupportFragmentManager().beginTransaction().hide(mineFragment).commitAllowingStateLoss();
        }
        switch (v.getId()) {
            //点击 首页 事件
            case R.id.tv_main_home:
                tv_main_home.setSelected(true);
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    getSupportFragmentManager().beginTransaction().add(R.id.fl_main, homeFragment).commitAllowingStateLoss();
                } else {
                    getSupportFragmentManager().beginTransaction().show(homeFragment).commitAllowingStateLoss();
                }
                break;

            //点击 攻略 事件
            case R.id.tv_main_raiders:
                tv_main_raiders.setSelected(true);
                if (raidersFragment == null) {
                    raidersFragment = new RaidersFragment();
                    getSupportFragmentManager().beginTransaction().add(R.id.fl_main, raidersFragment).commitAllowingStateLoss();
                } else {
                    getSupportFragmentManager().beginTransaction().show(raidersFragment).commitAllowingStateLoss();
                }
                break;

            //点击 消息 事件
            case R.id.tv_main_message:
                tv_main_message.setSelected(true);
                if (messageFragment == null) {
                    messageFragment = new MessageFragmentOne();
                    getSupportFragmentManager().beginTransaction().add(R.id.fl_main, messageFragment).commitAllowingStateLoss();
                } else {
                    getSupportFragmentManager().beginTransaction().show(messageFragment).commitAllowingStateLoss();
                }
                break;

            //点击 我的 事件
            case R.id.tv_main_mine:
                tv_main_mine.setSelected(true);
                if (mineFragment == null) {
                    mineFragment = new MineFragment();
                    getSupportFragmentManager().beginTransaction().add(R.id.fl_main, mineFragment).commitAllowingStateLoss();
                } else {
                    getSupportFragmentManager().beginTransaction().show(mineFragment).commitAllowingStateLoss();
                }
                break;

        }
    }
}
