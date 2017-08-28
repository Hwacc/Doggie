package example.doggie.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.ActionBar;
import android.util.Log;

import example.doggie.R;
import example.doggie.main.frag1.Fragment1;
import example.doggie.main.frag2.Fragment2;

/**
 * Created by Hwa on 2017/8/28.
 */

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener,TabLayout.OnTabSelectedListener{

    private Fragment1 mFstFragment;
    private Fragment2 mSecFragment;

    private ViewPager mViewPager;
    private TabLayout mTableLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //tool bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        //add fragment
        mFstFragment = (Fragment1) getSupportFragmentManager().findFragmentByTag("0");
        if(mFstFragment == null){
            mFstFragment = Fragment1.newInstance();
        }
        mSecFragment = (Fragment2) getSupportFragmentManager().findFragmentByTag("1");
        if(mSecFragment == null){
            mSecFragment = Fragment2.newInstance();
        }

        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position){
                    case 0:
                        return mFstFragment;
                    case 1:
                        return mSecFragment;
                }
                return null;
            }

            @Override
            public int getCount() {
                return 2;
            }
        });

        mViewPager.addOnPageChangeListener(this);

        mTableLayout = (TabLayout) findViewById(R.id.tabLayout);
        mTableLayout.addOnTabSelectedListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mTableLayout.getTabAt(position).select();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
