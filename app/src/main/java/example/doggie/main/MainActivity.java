package example.doggie.main;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.ActionBar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import example.doggie.R;
import example.doggie.app.core.base.BaseFragment;
import example.doggie.main.frag1.Fragment1;
import example.doggie.main.frag2.Fragment2;

/**
 * Created by Hwa on 2017/8/28.
 */

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener,BaseFragment.OnPageViewSelected{

    private static final String TAG = MainActivity.class.getSimpleName();
    private Fragment1 mFstFragment;
    private Fragment2 mSecFragment;

    private ViewPager mViewPager;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NavigationView mNavigation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

/*        mCoordinator = (CoordinatorLayout) findViewById(R.id.main_colayout);
        mAppbarLayout = (AppBarLayout) findViewById(R.id.toolbar_layout);
        mToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing);
        //tool bar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(mToolbar);*/
/*        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);*/

        initDrawer();

        //add fragment
        mFstFragment = (Fragment1) getSupportFragmentManager().findFragmentByTag("0");
        if(mFstFragment == null){
            mFstFragment = Fragment1.newInstance();
            mFstFragment.setOnPageViewSelected(this);
        }
        mSecFragment = (Fragment2) getSupportFragmentManager().findFragmentByTag("1");
        if(mSecFragment == null){
            mSecFragment = Fragment2.newInstance();
            mSecFragment.setOnPageViewSelected(this);
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

/*        mAppbarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                Log.d(TAG,"verticalOffset = "+verticalOffset);
                Log.d(TAG,"total = "+appBarLayout.getTotalScrollRange());

                if((Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange())){
//                    appBarLayout.setScrollY(appBarLayout.getTotalScrollRange());
                }
            }
        });*/
    }

    private void initDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.drawer_open,R.string.drawer_close){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        mDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(mDrawerToggle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
 /*     MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);*/
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {}

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public int seleced() {
        return mViewPager.getCurrentItem();
    }
}
