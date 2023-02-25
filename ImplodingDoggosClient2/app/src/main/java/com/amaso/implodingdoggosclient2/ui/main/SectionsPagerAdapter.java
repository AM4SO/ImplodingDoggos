package com.amaso.implodingdoggosclient2.ui.main;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.amaso.implodingdoggosclient2.R;
import com.amaso.implodingdoggosclient2.newLanTab;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentStateAdapter {

    private static final String[] TAB_TITLES = new String[]{"Host LAN Game", "Join LAN Game"};

    public SectionsPagerAdapter(FragmentActivity frag) {
        super(frag);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0){
            return new newLanTab();
        }
        return PlaceholderFragment.newInstance(position);
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}