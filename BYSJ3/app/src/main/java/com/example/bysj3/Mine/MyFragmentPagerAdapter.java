package com.example.bysj3.Mine;

import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    private final int PAGER_COUNT = 3;
    private MineFragmentOne mineFragmentOne = null;
    private MineFragmentTwo mineFragmentTwo = null;
    private MineFragmentThree mineFragmentThree = null;


    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        mineFragmentOne = new MineFragmentOne();
        mineFragmentTwo = new MineFragmentTwo();
        mineFragmentThree = new MineFragmentThree();
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
            case MineFragment.PAGE_ONE:
                fragment = mineFragmentOne;
                break;
            case MineFragment.PAGE_TWO:
                fragment = mineFragmentTwo;
                break;
            case MineFragment.PAGE_THREE:
                fragment = mineFragmentThree;
                break;
        }
        return fragment;
    }

}