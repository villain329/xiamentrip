package com.example.bysj3.Mine.Login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bysj3.Data.SharedHelper;
import com.example.bysj3.MainActivity;
import com.example.bysj3.Mine.MineFragment;
import com.example.bysj3.Mine.MineSettingActivity;
import com.example.bysj3.MyAPPlication;
import com.example.bysj3.R;
import com.example.bysj3.Util.HttpLogin;
import com.example.bysj3.Util.HttpUtil;
import com.example.bysj3.Util.HttpUtilSearch;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    public String user_id;
    public String password;
    public String user;
    String variable;
    public List<Map<String, Object>> idlist = new ArrayList<>();

    private Button bt_mine_login_login;
    private Button bt_mine_login_back;
    private TextView bt_mine_login_register;
    private EditText ed_mine_login_account;
    private EditText ed_mine_login_password;
    private SharedHelper sh;
    private Context mContext;
    private MineFragment mineFragment;

    private final static int LOGIN_JUDGE = 1;
    private int RequestCode = 1;


    private IntentFilter intentFilter;
    private NetworkChangeReceiver networkChangeReceiver;

    private String url = "http://39.97.251.81:8080/test5/GetIdServlet";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //沉浸式状态栏
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.WHITE);
        }

        setContentView(R.layout.activity_login);

        mContext = getApplicationContext();
        sh = new SharedHelper(mContext);

        bindView();

        //判断网络能不能用
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        networkChangeReceiver = new NetworkChangeReceiver();
        registerReceiver(networkChangeReceiver, intentFilter);


    }

    //判断网络能不能用
    class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable()) {
//如果可用执行
                for (int q = 0; q < 1; q++) {
                    Map<String, String> data = sh.read();
                    ed_mine_login_account.setText(data.get("user_id"));
                    ed_mine_login_password.setText(data.get("password"));
                    if (ed_mine_login_password.getText().toString().length() == 0
                            && ed_mine_login_account.getText().toString().length() == 0) {

                    } else {
                        onClick(bt_mine_login_login);
//                        Intent intent1 = new Intent(LoginActivity.this, MainActivity.class);
//                        startActivity(intent1);
                    }
                }
                } else{
                    Toast.makeText(context, "当前网络不可用", Toast.LENGTH_SHORT).show();
                }

        }
    }


//    @Override
//    protected void onStart() {
//        super.onStart();
//        Map<String, String> data = sh.read();
//        ed_mine_login_account.setText(data.get("user_id"));
//        ed_mine_login_password.setText(data.get("password"));
//        if (ed_mine_login_password.getText().toString().length() == 0
//                && ed_mine_login_account.getText().toString().length() == 0 ){
//        }
//        else {
//            onClick(bt_mine_login_login);
//            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
//            startActivity(intent);
//        }
//
//    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent home = new Intent(Intent.ACTION_MAIN);
                    home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    home.addCategory(Intent.CATEGORY_HOME);
                    startActivity(home);
                    return true;
                }

            return false;

    }

    private void bindView() {
//        bt_mine_login_back = findViewById(R.id.bt_mine_login_back);
        bt_mine_login_login = findViewById(R.id.bt_mine_login_login);
        bt_mine_login_register = findViewById(R.id.bt_mine_login_register);
        ed_mine_login_account = findViewById(R.id.ed_mine_login_account);
        ed_mine_login_password = findViewById(R.id.ed_mine_login_password);

//        bt_mine_login_back.setOnClickListener(this);
        bt_mine_login_login.setOnClickListener(this);
        bt_mine_login_register.setOnClickListener(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 2) {
            ed_mine_login_account.setText(data.getStringExtra("user_id"));
            ed_mine_login_password.setText(data.getStringExtra("password"));
        }
    }


    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case LOGIN_JUDGE: {
                    Bundle bundle = new Bundle();
                    bundle = msg.getData();
                    String result = bundle.getString("result");
                    try {
                        for (int i = 0;i<1;i++){
                        if (result.equals("success")) {
//                            Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
//                            okhttpID();
                            user_id = ed_mine_login_account.getText().toString();
                            password = ed_mine_login_password.getText().toString();
                            sh.save(user_id, password);
                            variable = user_id;
                            if (!"".equals(variable)) {
                                //将输入的值赋给全局变量variable
                                MyAPPlication.setGlobalvariable(variable);
                            }
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            getSupportFragmentManager().beginTransaction().replace(R.id.fl_main, mineFragment).commitAllowingStateLoss();
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "账号或密码错误！", Toast.LENGTH_SHORT).show();
                            SharedPreferences sharedPreferences=mContext.getSharedPreferences("login",Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.clear();
                        }
                        }
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
            return false;
        }

//        private void okhttpID() {
//            String anydata = ed_mine_login_account.getText().toString();
//            HttpUtilSearch.sendOkHttpRequest(url, anydata, new Callback() {
//
//                @Override
//                public void onFailure(@NotNull Call call, @NotNull IOException e) {
//
//                }
//
//                @Override
//                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//
//                    String responseData = response.body().string();
//                    Log.e(String.valueOf(1), "onResponse: " + responseData);
//                    jsonJXDate(responseData);
//                }
//
//                private void jsonJXDate(String responseData) {
//                    if (responseData != null) {
//                        try {
//                            JSONArray array = new JSONArray(responseData);
//                            for (int i = 0; i < array.length(); i++) {
//                                JSONObject object = array.getJSONObject(i);
//                                user_id = object.getString("user_id");
//                                password = object.getString("password");
//                                user = object.getString("user");
//                                Map<String, Object> map = new HashMap<>();
//                                map.put("user_id", user_id);
//                                map.put("password", password);
//                                map.put("user", user);
//                                idlist.add(map);
//
//                            }
//                            Message msg = new Message();
//                            msg.what = 5;
//                            handler.sendMessage(msg);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                }

//                public Handler handler = new Handler(new Handler.Callback() {
//                    @Override
//                    public boolean handleMessage(@NonNull Message msg) {
//                        switch (msg.what) {
//                            case 5:
//
//                                variable = user_id;
//                                if (!"".equals(variable)) {
//                                    //将输入的值赋给全局变量variable
//                                    MyAPPlication.setGlobalvariable(variable);
//                                }
//
//                                break;
//                        }
//                        return false;
//                    }
//                });
//            });
//        }
    });


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_mine_login_login:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String result = HttpLogin.LoginByPost(ed_mine_login_account.getText().toString(), ed_mine_login_password.getText().toString());
                        Bundle bundle = new Bundle();
                        bundle.putString("result", result);
                        Message message = new Message();
                        message.setData(bundle);
                        message.what = LOGIN_JUDGE;
                        handler.sendMessage(message);

                    }
                }).start();


                break;

//            case R.id.bt_mine_login_back:
//                finish();
//                Intent intent1 = new Intent(this, MainActivity.class);
//                variable = "请登录";
//                if (!"".equals(variable)) {
//                    //将输入的值赋给全局变量variable
//                    MyAPPlication.setGlobalvariable(variable);
//                }
//                startActivity(intent1);
//                break;

            case R.id.bt_mine_login_register:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivityForResult(intent, RequestCode);

        }

    }
}
