package com.example.bysj3.Util;

import android.util.Log;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

//发送表单，查询功能

public class HttpUtilSearch {

    public static void sendOkHttpRequest(String address, String anydata, Callback callback) {
        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("data", String.valueOf(anydata))
                .build();
        Request request = new Request.Builder()
                .url(address)
                .post(formBody)
                .build();
        client.newCall(request).enqueue(callback);
    }
}