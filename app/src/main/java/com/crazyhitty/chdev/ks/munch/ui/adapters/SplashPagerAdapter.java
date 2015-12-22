package com.crazyhitty.chdev.ks.munch.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.crazyhitty.chdev.ks.munch.ui.fragments.SplashFragment;

/**
 * Created by Kartik_ch on 12/17/2015.
 */
public class SplashPagerAdapter extends FragmentPagerAdapter {

    public SplashPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return SplashFragment.newInstance(position + 1);
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }
}