package com.hassanmashraful.democonstract.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.hassanmashraful.democonstract.Fragment.FormFragment;

/**
 * Created by Hassan M.Ashraful on 11/25/2016.
 */

public class PagerAdapter extends FragmentPagerAdapter {

    private Fragment mCurrentFragment;

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public Fragment getCurrentFragment() {
        return mCurrentFragment;
    }


    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (getCurrentFragment() != object) {
            mCurrentFragment = ((Fragment) object);
        }
        super.setPrimaryItem(container, position, object);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:

                return new FormFragment();
            case 1:
                return new FormFragment();

            default:
                break;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
