package com.example.bysj3;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import org.litepal.LitePal;


public class MyAPPlication extends Application {
    private static Context context;
    private static MyAPPlication instance;
    //新建静态变量
    private static String Globalvariable;

    private static final int UPDATA_ID = 8;
    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);
        context = getApplicationContext();
        instance = this;
    }

    public static Context getContext(){
        return context;
    }

    public static MyAPPlication getInstance(){
        return instance;
    }

    public static void setInstance(MyAPPlication instance) {
        MyAPPlication.instance = instance;
    }

    public static void setContext(Context context) {
        MyAPPlication.context = context;
    }

    public static String getGlobalvariable() {
        return Globalvariable;
    }

    public static void setGlobalvariable(String globalvariable) {
        MyAPPlication.Globalvariable = globalvariable;
    }


}
