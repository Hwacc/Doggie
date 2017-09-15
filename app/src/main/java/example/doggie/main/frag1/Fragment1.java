package example.doggie.main.frag1;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;

import java.util.Calendar;
import java.util.List;

import example.doggie.R;
import example.doggie.app.core.base.BaseFragment;
import example.doggie.app.core.base.IBasePresenter;
import example.doggie.app.core.bean.GankDaily;
import example.doggie.main.MainContract;
import example.doggie.main.widget.MaterialProgressDrawable;

/**
 * Created by Hwa on 2017/8/28.
 */

public class Fragment1 extends BaseFragment implements
        MainContract.View,SwipeRefreshLayout.OnRefreshListener{

    private static int FOOTER_TYPE = 1;
    private static int LOADING = 2;
    private static int LOADED = 3;

    private SwipeRefreshLayout mSwipe;
    private VirtualLayoutManager mLayoutManager;
    private DelegateAdapter mAdapter;
    private Present1 mPresent;
    private AdapterHelper mAdapterHelper;
    private int mYear = 2015,mMonth = 8,mDay = 7;
    private int mLoadState = LOADED;
    private Handler mMainHandler;

    public static Fragment1 newInstance() {
        return new Fragment1();
    }

    @Override
    public View initFragment(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.layout_frag1, container, false);

        mSwipe = (SwipeRefreshLayout) root.findViewById(R.id.fra1_swipe);
        mSwipe.setProgressBackgroundColorSchemeResource(R.color.white);
        mSwipe.setColorSchemeResources(R.color.lightblue200,R.color.lightblue300,R.color.lightblue400,R.color.lightblue500);
        mSwipe.setProgressViewOffset(false, 0, (int) getResources().getDimension(R.dimen.dis_refresh));
        mSwipe.setOnRefreshListener(this);


        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.frag1_recycler);
        mLayoutManager = new VirtualLayoutManager(mContext, OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();

        recyclerView.setRecycledViewPool(viewPool);

        viewPool.setMaxRecycledViews(AdapterHelper.GRID_TYPE, 10);
        viewPool.setMaxRecycledViews(AdapterHelper.LINEAR_TYPE, 10);
        viewPool.setMaxRecycledViews(AdapterHelper.STICK_TYPE,5);
        viewPool.setMaxRecycledViews(FOOTER_TYPE,2);

        mAdapter = new MainAdapter(mLayoutManager, true);
        mAdapterHelper = new AdapterHelper(mContext);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnScrollListener(new OnScrollListener() {
            private int lastVisibleItem;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState ==RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 ==mAdapter.getItemCount()){
                    Log.d("TAG","recycler at last");
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
                mSwipe.setRefreshing(false);
            }
        },5000);
    }

    @Override
    public IBasePresenter initPresenter() {
        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH) + 1;
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        mPresent = new Present1(this);
        mPresent.initDataTime(mYear, mMonth, mDay);
        return mPresent;
    }


    @Override
    public void setPresenter(Object presenter) {

    }

    @Override
    public void onSucceed(Object data) {
        //noinspection unchecked
        List<DelegateAdapter.Adapter> adapters = mAdapterHelper.getAdapters((Pair<String,GankDaily>)data);
        mAdapter.addAdapters(adapters);
        mAdapter.notifyItemInserted(mAdapter.getItemCount() - 1);
        mLoadState = LOADED;
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
        calendar.set(mYear,mMonth,mDay);
        calendar.add(Calendar.DAY_OF_MONTH,-1);
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
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
                        //透明度，0-255
                        loadHolder.progress.setAlpha((int) (255 * n));
                    }
                });
                valueAnimator.start();
                loadHolder.progress.start();
            }
            super.onBindViewHolder(holder, position);
        }
    }

    private class LoadHolder extends RecyclerView.ViewHolder{
        ImageView imgView;
        MaterialProgressDrawable progress;
        LoadHolder(View itemView) {
            super(itemView);
            imgView = (ImageView) itemView.findViewById(R.id.loading_item);
            progress = new MaterialProgressDrawable(mContext,imgView);
            progress.setBackgroundColor(Color.WHITE);
            progress.setColorSchemeColors(R.color.lightblue200,R.color.lightblue300,R.color.lightblue400,R.color.lightblue500);
            progress.setAlpha(0);
            imgView.setImageDrawable(progress);
        }
    }

}
