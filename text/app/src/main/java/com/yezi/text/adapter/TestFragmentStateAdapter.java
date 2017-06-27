package com.yezi.text.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.yezi.text.activity.TestFragment;

public class TestFragmentStateAdapter extends FragmentStatePagerAdapter {

    public TestFragmentStateAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return new TestFragment();
    }

    @Override
    public int getCount() {
        return 6;
    }
}