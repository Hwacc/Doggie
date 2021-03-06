package example.doggie.main.frag1;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;

import java.util.Calendar;

import example.doggie.R;
import example.doggie.app.core.base.BaseFragment;
import example.doggie.app.core.base.IBasePresenter;
import example.doggie.main.widget.CircleImageView;
import example.doggie.main.widget.MaterialProgressDrawable;

/**
 * Created by Hwa on 2017/8/28.
 */

public class Fragment1 extends BaseFragment implements
        F1Contract.View,SwipeRefreshLayout.OnRefreshListener,TabLayout.OnTabSelectedListener{

    private static int FOOTER_TYPE = 1;
    private static int LOADING = 2;
    private static int LOADED = 3;

    private SwipeRefreshLayout mSwipe;
    private VirtualLayoutManager mLayoutManager;
    private DelegateAdapter mAdapter;
    private Present1 mPresent;
    private int mYear = 2015,mMonth = 8,mDay = 7;
    private int mLoadState = LOADED;
    private Handler mMainHandler;

    private Toolbar mToolbar;
    private TabLayout mTableLayout;
    private AppBarLayout mAppbarLayout;
    private CollapsingToolbarLayout mToolbarLayout;
    private CoordinatorLayout mCoordinator;

    public static Fragment1 newInstance() {
        return new Fragment1();
    }

    @Override
    public View initFragment(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.layout_frag1, container, false);

/*        mSwipe = (SwipeRefreshLayout) root.findViewById(R.id.fra1_swipe);
        mSwipe.setProgressBackgroundColorSchemeResource(R.color.white);
        mSwipe.setColorSchemeResources(R.color.lightblue200,R.color.lightblue300,R.color.lightblue400,R.color.lightblue500);
        mSwipe.setProgressViewOffset(false, 0, (int) getResources().getDimension(R.dimen.dis_refresh));
        mSwipe.setOnRefreshListener(this);*/

//        mCoordinator = (CoordinatorLayout) root.findViewById(R.id.main_colayout);
        mAppbarLayout = (AppBarLayout) root.findViewById(R.id.toolbar_layout);
        mToolbarLayout = (CollapsingToolbarLayout) root.findViewById(R.id.collapsing);
        //tool bar
        mToolbar = (Toolbar) root.findViewById(R.id.toolbar);
        mToolbar.setTitleTextColor(Color.parseColor("#ffffff"));

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(mToolbar);
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        mTableLayout = (TabLayout) root.findViewById(R.id.tabLayout);
        mTableLayout.addOnTabSelectedListener(this);
        mTableLayout.getTabAt(mSelectLinstener.seleced()).select();

        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.frag1_recycler);
        mLayoutManager = new VirtualLayoutManager(mContext, OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        recyclerView.setRecycledViewPool(viewPool);

        viewPool.setMaxRecycledViews(AdapterHelper.GRID_TYPE, 5);
        viewPool.setMaxRecycledViews(AdapterHelper.LINEAR_TYPE, 5);
        viewPool.setMaxRecycledViews(AdapterHelper.STICK_TYPE,50);
        viewPool.setMaxRecycledViews(FOOTER_TYPE,2);
        mAdapter = new MainAdapter(mLayoutManager, true);

        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnScrollListener(new OnScrollListener() {
            private int lastVisibleItem;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState ==RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 ==mAdapter.getItemCount()){
                    if(mLoadState == LOADED){
                        mMainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mLoadState = LOADING;
                                setNextDay();
                                mPresent.subscribeByDate(mYear,mMonth,mDay);
                            }
                        });
                    }
                }
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            }
        });
        mMainHandler = new Handler();
        return root;
    }

    @Override
    public void onRefresh() {
        mMainHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                mSwipe.setRefreshing(false);
            }
        },3000);
    }

    @Override
    public IBasePresenter initPresenter() {
        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH) + 1;
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        mPresent = new Present1(mContext,this);
        mPresent.initDataTime(mYear, mMonth, mDay);
        return mPresent;
    }


    @Override
    public void setPresenter(Object presenter) {

    }

    @Override
    public void setAdapter(DelegateAdapter.Adapter adapter) {
        mAdapter.addAdapter(adapter);
        mAdapter.notifyItemInserted(mAdapter.getItemCount() - 1);
        mLoadState = LOADED;
    }

    @Override
    public void onSucceed(Object data) {
        //noinspection unchecked
    }

    @Override
    public void onError(String errorMsg) {

    }

    @Override
    public void onComplete() {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                mLoadState = LOADING;
                setNextDay();
                mPresent.subscribeByDate(mYear,mMonth,mDay);
            }
        });
    }

    private void setNextDay(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(mYear,mMonth-1,mDay);
        calendar.add(Calendar.DAY_OF_MONTH,-1);
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH)+1;
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }


    private class MainAdapter extends DelegateAdapter{

        private ValueAnimator valueAnimator;

        MainAdapter(VirtualLayoutManager layoutManager, boolean hasConsistItemType) {
            super(layoutManager,hasConsistItemType);
            if(valueAnimator == null){
                valueAnimator = ValueAnimator.ofFloat(0f,1f);
                valueAnimator.setDuration(600);
                valueAnimator.setInterpolator(new DecelerateInterpolator());
            }
        }

        @Override
        public int getItemCount() {
            return super.getItemCount()+1;
        }

        @Override
        public int getItemViewType(int position) {
            if(position+1 == getItemCount()) {
                return FOOTER_TYPE;
            }
            return super.getItemViewType(position);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == FOOTER_TYPE){
                return  new LoadHolder(
                        LayoutInflater.from(mContext).inflate(R.layout.layout_recycler_loading_item,parent,false));
            }
            return super.onCreateViewHolder(parent, viewType);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof LoadHolder){
                final LoadHolder loadHolder = (LoadHolder)holder;
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float n = (float) animation.getAnimatedValue();
                        loadHolder.progress.setProgressRotation(n*0.5f);
                        //圈圈周长，0f-1F
                        loadHolder.progress.setStartEndTrim(0f, n * 0.8f);
                        //箭头大小，0f-1F
                        loadHolder.progress.setArrowScale(n);
                    }
                });
                valueAnimator.start();
                loadHolder.progress.start();
            }
            super.onBindViewHolder(holder, position);
        }
    }

    private class LoadHolder extends RecyclerView.ViewHolder{
        MaterialProgressDrawable progress;
        CircleImageView circleImageView;
        LoadHolder(View itemView) {
            super(itemView);
            circleImageView = (CircleImageView) itemView.findViewById(R.id.loading_item);
            progress = new MaterialProgressDrawable(mContext,circleImageView);
            progress.setBackgroundColor(Color.WHITE);
            progress.setColorSchemeColors(R.color.lightblue200,R.color.lightblue300,R.color.lightblue400,R.color.lightblue500);
            progress.setAlpha(255);
            circleImageView.setImageDrawable(progress);
        }
    }

}
