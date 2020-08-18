package com.example.bysj3.Data;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

public class SharedHelper {

    private Context mContext;

    public SharedHelper() {
    }

    public SharedHelper(Context mContext) {
        this.mContext = mContext;
    }


    //定义一个保存数据的方法
    public void save(String account, String password) {
        SharedPreferences sp = mContext.getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("user_id", account);
        editor.putString("password", password);
        editor.apply();
    }

    //定义一个读取SP文件的方法
    public Map<String, String> read() {
        Map<String, String> data = new HashMap<String, String>();
        SharedPreferences sp = mContext.getSharedPreferences("login", Context.MODE_PRIVATE);
        data.put("user_id", sp.getString("user_id", ""));
        data.put("password", sp.getString("password", ""));
        return data;
    }


    public void savemessage(String send_user_id, String send_user,String send,String received_id,String received_user,String received) {
        SharedPreferences sp = mContext.getSharedPreferences("message", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("send_user_id", send_user_id);
        editor.putString("send_user", send_user);
        editor.putString("send", send);
        editor.putString("received_id", received_id);
        editor.putString("received_user", received_user);
        editor.putString("received", received);
        editor.apply();
    }


    public Map<String, String> savemessage() {
        Map<String, String> data = new HashMap<String, String>();
        SharedPreferences sp = mContext.getSharedPreferences("message", Context.MODE_PRIVATE);
        data.put("send_user_id", sp.getString("send_user_id", ""));
        data.put("send_user", sp.getString("send_user", ""));
        data.put("send", sp.getString("send", ""));
        data.put("received_id", sp.getString("received_id", ""));
        data.put("received_user", sp.getString("received_user", ""));
        data.put("received", sp.getString("received", ""));
        return data;
    }

}
