package com.example.bysj3.Mine;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.bysj3.MainActivity;
import com.example.bysj3.Mine.Login.LoginActivity;
import com.example.bysj3.MyAPPlication;
import com.example.bysj3.R;
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

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.CropCircleWithBorderTransformation;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MineFragment extends Fragment implements RadioGroup.OnCheckedChangeListener,
        ViewPager.OnPageChangeListener {


    private MineFragmentOne mineFragmentOne;
    private MineFragmentTwo mineFragmentTwo;
    private MineFragmentThree mineFragmentThree;



    private Button bt_mine_setting;
    private TextView tv_mine_account;
    private ImageView iv_mine;
    private static final int UPDATA_ID = 7;
    private Context mContext;
    private String accountupdata;

    public String user;
    public String user_id;
    public String password;
    public String user_image;

    public List<Map<String, Object>> idlist = new ArrayList<>();

    private ViewPager vpager_three;
    private RadioGroup rg_mine;
    private RadioButton rb_mine_one;
    private RadioButton rb_mine_two;
    private RadioButton rb_mine_three;
    private SwipeRefreshLayout refresh_mine;

    //几个代表页面的常量
    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
    public static final int PAGE_THREE = 2;
    private MyFragmentPagerAdapter mAdapter;

    private String url = "http://39.97.251.81:8080/test5/GetIdServlet";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);

        tv_mine_account = view.findViewById(R.id.tv_mine_account);
        bt_mine_setting = view.findViewById(R.id.bt_mine_setting);
        iv_mine = view.findViewById(R.id.iv_mine);

        bt_mine_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getFragmentManager().addOnBackStackChangedListener(null);
                Intent intent = new Intent(getActivity(), MineSettingActivity.class);
                startActivity(intent);


            }
        });

        mAdapter = new MyFragmentPagerAdapter(getChildFragmentManager());

        rg_mine = view.findViewById(R.id.rg_mine);
        rb_mine_one = view.findViewById(R.id.rb_mine_one);
        rb_mine_two = view.findViewById(R.id.rb_mine_two);
        rb_mine_three = view.findViewById(R.id.rb_mine_three);
        rg_mine.setOnCheckedChangeListener(this);

        vpager_three = view.findViewById(R.id.vpager_three);
        vpager_three.setAdapter(mAdapter);
        vpager_three.setCurrentItem(0);
        vpager_three.addOnPageChangeListener(this);

        rb_mine_one.setChecked(true);



//        vpager_three = view.findViewById(R.id.vpager_three);
//        tv_mine_one = view.findViewById(R.id.tv_mine_one);
//        tv_mine_two = view.findViewById(R.id.tv_mine_two);
//        tv_mine_three = view.findViewById(R.id.tv_mine_three);
//        img_cursor_mine = view.findViewById(R.id.img_cursor_mine);
//        initViews();


        mContext = this.getActivity();
        Message message = new Message();
        message.what = UPDATA_ID;
        handler.sendMessage(message);


        okhttp();


        refresh_mine = view.findViewById(R.id.refresh_mine);
        refresh_mine.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                okhttp();
                                rb_mine_one.setChecked(true);
                                vpager_three.setAdapter(mAdapter);
                                refresh_mine.setRefreshing(false);
                            }
                        });
                    }
                }).start();
            }
        });



        return view;

    }



    @Override
    public void onStart() {

        super.onStart();
//        if(!isAdded()) {
            mineFragmentOne = new MineFragmentOne();
            getChildFragmentManager().beginTransaction().add(R.id.fl_mine, mineFragmentOne).commitAllowingStateLoss();
//        }
        Log.e("TAG", "onStart: "+"onstart" );

    }

    public Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case UPDATA_ID:
                    if (MyAPPlication.getGlobalvariable() == null) {
                        tv_mine_account.setText("请登录");
                    } else {
//                        tv_mine_account.setText(user);
//
                    }
                    Log.e(String.valueOf(90), "handleMessage: " + MyAPPlication.getGlobalvariable());
            }
            return false;
        }


    });

           private void okhttp() {
            String anydata = MyAPPlication.getGlobalvariable();
            HttpUtilSearch.sendOkHttpRequest(url, anydata, new Callback() {

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                    String responseData = response.body().string();
                    Log.e(String.valueOf(1), "onResponse: " + responseData);
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
                                user_image =object.getString("user_image");

                            }
                            Message msg = new Message();
                            msg.what = 5;
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
                            case 5:
                                tv_mine_account.setText(user);
                                Glide.with(getContext())
                                        .load(user_image)
                                        .apply(RequestOptions.bitmapTransform(new CropCircleWithBorderTransformation(0,0)))
                                        .into(iv_mine);
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
            case R.id.rb_mine_one:
                vpager_three.setCurrentItem(PAGE_ONE);
                break;
            case R.id.rb_mine_two:
                vpager_three.setCurrentItem(PAGE_TWO);
                break;
            case R.id.rb_mine_three:
                vpager_three.setCurrentItem(PAGE_THREE);
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
            switch (vpager_three.getCurrentItem()) {
                case PAGE_ONE:
                    rb_mine_one.setChecked(true);
                    break;
                case PAGE_TWO:
                    rb_mine_two.setChecked(true);
                    break;
                case PAGE_THREE:
                    rb_mine_three.setChecked(true);
                    break;
            }
        }
    }
}


