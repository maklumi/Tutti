package com.example.homepc.tutti;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.Locale;

/**
 * Created by HomePC on 28/3/2016.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence titlesOfTabs[];
    int numberOfTabs;

    public ViewPagerAdapter(FragmentManager fm, CharSequence[] titlesOfTabs, int numberOfTabs) {
        super(fm);
        this.titlesOfTabs = titlesOfTabs;
        this.numberOfTabs = numberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            BrowseFragment browseFragment = new BrowseFragment();
            return browseFragment;
        }
        else {
            SearchFragment searchFragment = new SearchFragment();
            return searchFragment;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Locale locale = Locale.getDefault();
        switch (position) {
            case 0:
                return titlesOfTabs[position].toString().toUpperCase(locale);
            case 1:
                return titlesOfTabs[position].toString().toUpperCase(locale);
        }
        return null;
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
