package com.example.homepc.tutti;

import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.ParseAnalytics;

import java.util.Locale;


public class MainActivity extends ActionBarActivity implements ActionBar.TabListener {

    //declare tab views
    CharSequence titles[] = {"Browse", "Search"};
    int numberOfTabs = 2;

    MyFragmentPagerAdapter myFragmentPagerAdapter;
    ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        final ActionBar actionBar = getSupportActionBar();
//        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(
//                R.color.blue
//        )));
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());

        viewPager = (ViewPager)findViewById(R.id.pager);
        viewPager.setAdapter(myFragmentPagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        for (int i = 0; i < myFragmentPagerAdapter.getCount(); i++) {
            actionBar.addTab(actionBar.newTab().setText(
                    myFragmentPagerAdapter.getPageTitle(i)
            ).setTabListener(this));

            actionBar.setSplitBackgroundDrawable(getResources().getDrawable(android.R.color.white));
            actionBar.setStackedBackgroundDrawable(getResources().getDrawable(R.color.blue));

        }

    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public class MyFragmentPagerAdapter extends FragmentPagerAdapter {


        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new BrowseFragment();
            } else {
                return new SearchFragment();
            }
        }

        @Override
        public int getCount() {
            return numberOfTabs;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale locale = Locale.getDefault();
            switch (position) {
                case 0:
                    return titles[position].toString().toUpperCase(locale);
                case 1:
                    return titles[position].toString().toUpperCase(locale);
            }
            return null;
        }
    }



    // declare views and variables
  /*  Toolbar toolbar;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    SlidingTabLayout slidingTabLayout;
   */


  /*  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // create toolbar
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // create viewpageadapter
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), titles, numberOfTabs);

        // assign viewpager view
        viewPager = (ViewPager)findViewById(R.id.pager);
        viewPager.setAdapter(viewPagerAdapter);

        // assign slidingtablayout view
        slidingTabLayout = (SlidingTabLayout)findViewById(R.id.tabs);
        slidingTabLayout.setDistributeEvenly(true);

        // custom color for scrollbar
        slidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        // set viewpager for slidingtabslayout
        slidingTabLayout.setViewPager(viewPager);
    }
*/

}
