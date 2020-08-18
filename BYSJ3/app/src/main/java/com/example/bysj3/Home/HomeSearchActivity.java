package com.example.bysj3.Home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bysj3.R;

public class HomeSearchActivity extends AppCompatActivity implements View.OnClickListener {

    private HomeSearchOne fragment_home_search_one;
    private HomeSearchTwo fragment_home_search_two;

    private TextView tv_home_search_back;
    private TextView tv_home_search_search;
    private EditText et_home_search_search;

    private final static int DATA_SEARCH = 4;
    int ResultCode = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //沉浸式状态栏
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_home_search);

        bindView();
//加入 fragment_home_search_one 布局
        fragment_home_search_one = new HomeSearchOne();
        getSupportFragmentManager().beginTransaction().add(R.id.fl_home_search, fragment_home_search_one).commitAllowingStateLoss();

    }


    private void bindView() {
        tv_home_search_back = findViewById(R.id.tv_home_search_back);
        tv_home_search_search = findViewById(R.id.tv_home_search_search);
        et_home_search_search = findViewById(R.id.et_home_search_search);

        tv_home_search_back.setOnClickListener(this);
        tv_home_search_search.setOnClickListener(this);
        et_home_search_search.setOnClickListener(this);

        et_home_search_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                onClick(tv_home_search_search);
                }
                    return false;

            }
        });
    }



    //点击其他地方隐藏软键盘
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }
    public  boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = { 0, 0 };
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {

                return true;
            }
        }
        return false;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_home_search_back:
                finish();
                break;
            case R.id.tv_home_search_search:
                if (fragment_home_search_two == null) {
                    fragment_home_search_two = new HomeSearchTwo();
                    getSupportFragmentManager().beginTransaction().add(R.id.fl_home_search, fragment_home_search_two).hide(fragment_home_search_one).commitAllowingStateLoss();

                } else {
                    getSupportFragmentManager().beginTransaction().show(fragment_home_search_two).commitAllowingStateLoss();
                }

                break;
        }
    }
}

