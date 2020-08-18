package com.example.bysj3.User;

import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.bysj3.Mine.MineFragment;
import com.example.bysj3.Mine.MineFragmentOne;
import com.example.bysj3.Mine.MineFragmentThree;
import com.example.bysj3.Mine.MineFragmentTwo;

public class UserFragmentPagerAdapter extends FragmentPagerAdapter {
    private final int PAGER_COUNT = 3;
    private UserFragmentOne userFragmentOne = null;
    private UserFragmentTwo userFragmentTwo = null;
    private UserFragmentThree userFragmentThree = null;


    public UserFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        userFragmentOne = new UserFragmentOne();
        userFragmentTwo = new UserFragmentTwo();
        userFragmentThree = new UserFragmentThree();
    }


    @Override
    public int getCount() {
        return PAGER_COUNT;
    }

    @Override
    public Object instantiateItem(ViewGroup vg, int position) {
        return super.instantiateItem(vg, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        System.out.println("position Destory" + position);
        super.destroyItem(container, position, object);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case UserActivity.USER_PAGE_ONE:
                fragment = userFragmentOne;
                break;
            case UserActivity.USER_PAGE_TWO:
                fragment = userFragmentTwo;
                break;
            case UserActivity.USER_PAGE_THREE:
                fragment = userFragmentThree;
                break;
        }
        return fragment;
    }

}