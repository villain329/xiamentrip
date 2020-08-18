package com.example.bysj3.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.bysj3.ChatActivity;
import com.example.bysj3.Mine.MyFragmentPagerAdapter;
import com.example.bysj3.MyAPPlication;
import com.example.bysj3.R;
import com.example.bysj3.Util.HttpUtilSearch;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jp.wasabeef.glide.transformations.CropCircleWithBorderTransformation;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class UserActivity extends AppCompatActivity
        implements RadioGroup.OnCheckedChangeListener,
        ViewPager.OnPageChangeListener
{

    public String user;
    public String user_id;
    public String password;
    public String user_image;

    public List<Map<String, Object>> idlist = new ArrayList<>();

    private Button bt_user_back;
    private TextView tv_user_account;
    private ImageView iv_user;
    private ImageView iv_user_chat;
    private ViewPager vpager_three_user;
    private RadioGroup rg_user;
    private RadioButton rb_user_one;
    private RadioButton rb_user_two;
    private RadioButton rb_user_three;
    private SwipeRefreshLayout refresh_user;

    //几个代表页面的常量
    public static final int USER_PAGE_ONE = 0;
    public static final int USER_PAGE_TWO = 1;
    public static final int USER_PAGE_THREE = 2;
    public static final int USER_IMAGE = 5;
    private UserFragmentPagerAdapter mAdapter;

    private String url = "http://39.97.251.81:8080/test5/GetIdServlet";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        okhttp();

//        if (getIntent().getStringExtra("user_id") == MyAPPlication.getGlobalvariable()){
//            iv_user_chat.setVisibility(View.GONE);
//        }

        tv_user_account = findViewById(R.id.tv_user_account);
        iv_user = findViewById(R.id.iv_user);
//        iv_user_chat = findViewById(R.id.iv_user_chat);
//        iv_user_chat.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(UserActivity.this, ChatActivity.class);
//                intent.putExtra("user_id",getIntent().getStringExtra("user_id"));
//                startActivity(intent);
//            }
//        });

        bt_user_back = findViewById(R.id.bt_user_back);
        bt_user_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        mAdapter = new UserFragmentPagerAdapter(getSupportFragmentManager());

        vpager_three_user = findViewById(R.id.vpager_three_user);
        rg_user = findViewById(R.id.rg_user);
        rb_user_one = findViewById(R.id.rb_user_one);
        rb_user_two = findViewById(R.id.rb_user_two);
        rb_user_three = findViewById(R.id.rb_user_three);
        rg_user.setOnCheckedChangeListener(this);

        vpager_three_user = findViewById(R.id.vpager_three_user);
        vpager_three_user.setAdapter(mAdapter);
        vpager_three_user.setCurrentItem(0);
        vpager_three_user.addOnPageChangeListener(this);

        rb_user_one.setChecked(true);


        refresh_user = findViewById(R.id.refresh_user);
        refresh_user.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }

            public void refresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                okhttp();
                                rb_user_one.setChecked(true);
                                vpager_three_user.setAdapter(mAdapter);
                                refresh_user.setRefreshing(false);
                            }
                        });
                    }
                }).start();
            }
        });




    }



    private void okhttp() {
        String anydata = getIntent().getStringExtra("user_id");
        Log.e("anydata", "okhttp: "+anydata );
        HttpUtilSearch.sendOkHttpRequest(url, anydata, new Callback() {

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
                            password = object.getString("password");
                            user = object.getString("user");
                            user_image = object.getString("user_image");

                        }
                        Message msg = new Message();
                        msg.what = USER_IMAGE;
                        handler.sendMessage(msg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            public Handler handler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(@NonNull Message msg) {
                    switch (msg.what) {
                        case USER_IMAGE:
                            tv_user_account.setText(user);
                            Glide.with(UserActivity.this)
                                    .load(user_image)
                                    .apply(RequestOptions.bitmapTransform(new CropCircleWithBorderTransformation(0, 0)))
                                    .into(iv_user);
                            break;
                    }
                    return false;
                }
            });
        });
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_user_one:
                vpager_three_user.setCurrentItem(USER_PAGE_ONE);
                break;
            case R.id.rb_user_two:
                vpager_three_user.setCurrentItem(USER_PAGE_TWO);
                break;
            case R.id.rb_user_three:
                vpager_three_user.setCurrentItem(USER_PAGE_THREE);
                break;
        }
    }


    //重写ViewPager页面切换的处理方法
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //state的状态有三个，0表示什么都没做，1正在滑动，2滑动完毕
        if (state == 2) {
            switch (vpager_three_user.getCurrentItem()) {
                case USER_PAGE_ONE:
                    rb_user_one.setChecked(true);
                    break;
                case USER_PAGE_TWO:
                    rb_user_two.setChecked(true);
                    break;
                case USER_PAGE_THREE:
                    rb_user_three.setChecked(true);
                    break;
            }
        }
    }
}


