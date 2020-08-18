package com.example.bysj3.Message;

import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MessageFragmentPagerAdapter extends FragmentPagerAdapter {
    private final int PAGER_COUNT = 2;
    private MessageFragmentOne messageFragmentOne = null;
    private MessageFragmentTwo messageFragmentTwo = null;


    public MessageFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        messageFragmentOne = new MessageFragmentOne();
        messageFragmentTwo = new MessageFragmentTwo();
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
            case MessageFragment.MESSAGE_PAGE_ONE:
                fragment = messageFragmentOne;
                break;
            case MessageFragment.MESSAGE_PAGE_TWO:
                fragment = messageFragmentTwo;
                break;
        }
        return fragment;
    }
}
