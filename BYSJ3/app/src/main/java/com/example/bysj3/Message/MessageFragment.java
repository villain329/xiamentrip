package com.example.bysj3.Message;

import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.bysj3.R;

import java.util.ArrayList;

public class MessageFragment extends Fragment  implements RadioGroup.OnCheckedChangeListener,
        ViewPager.OnPageChangeListener{

        private ViewPager vpager_two;
    private RadioGroup rg_message;
    private RadioButton rb_message_one;
    private RadioButton rb_message_two;

    //几个代表页面的常量
    public static final int MESSAGE_PAGE_ONE = 0;
    public static final int MESSAGE_PAGE_TWO = 1;
    private MessageFragmentPagerAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);


        mAdapter = new MessageFragmentPagerAdapter(getChildFragmentManager());

        rg_message = view.findViewById(R.id.rg_message);
        rb_message_one = view.findViewById(R.id.rb_message_one);
        rb_message_two = view.findViewById(R.id.rb_message_two);
        rg_message.setOnCheckedChangeListener(this);

        vpager_two = view.findViewById(R.id.vpager_two);;
        vpager_two.setAdapter(mAdapter);
        vpager_two.setCurrentItem(0);
        vpager_two.addOnPageChangeListener(this);

        rb_message_one.setChecked(true);


        return view;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_message_one:
                vpager_two.setCurrentItem(MESSAGE_PAGE_ONE);
                break;
            case R.id.rb_message_two:
                vpager_two.setCurrentItem(MESSAGE_PAGE_TWO);
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
            switch (vpager_two.getCurrentItem()) {
                case MESSAGE_PAGE_ONE:
                    rb_message_one.setChecked(true);
                    break;
                case MESSAGE_PAGE_TWO:
                    rb_message_two.setChecked(true);
                    break;
            }
        }
    }
}






