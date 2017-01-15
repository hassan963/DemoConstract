package com.constructefile.democonstract.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.constructefile.democonstract.Fragment.FormFragment;
import com.constructefile.democonstract.Fragment.FormFragmentTwo;

/**
 * Created by Hassan M.Ashraful on 11/25/2016.
 */

public class PagerAdapter extends FragmentPagerAdapter {

    private Fragment mCurrentFragment;
    private FormFragment formFragment;
    private FormFragmentTwo formFragmentTwo;

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public Fragment getCurrentFragment() {
        return mCurrentFragment;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
        // save the appropriate reference depending on position
        switch (position) {
            case 0:
                formFragment = (FormFragment) createdFragment;
                break;
            case 1:
                formFragmentTwo = (FormFragmentTwo) createdFragment;
                break;
        }
        return createdFragment;
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
                return new FormFragmentTwo();

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
