package example.doggie.main.activity.HomeActivity;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.Calendar;

import example.doggie.R;
import example.doggie.main.frag1.AdapterHelper;
import example.doggie.main.widget.CircleImageView;
import example.doggie.main.widget.MaterialProgressDrawable;

/**
 * Created by Hwa on 2017/9/27.
 */

public class MainActivity extends AppCompatActivity implements HomeContract.View {

    private HomePresenter mPresenter;

    private static int FOOTER_TYPE = 1;
    private Toolbar mToolbar;
    private TabLayout mTableLayout;
    private AppBarLayout mAppbarLayout;
    private CollapsingToolbarLayout mToolbarLayout;
    private CoordinatorLayout mCoordinator;
    private RecyclerView mRecyclerView;
    private VirtualLayoutManager mLayoutManager;
    private DelegateAdapter mAdapter;
    private Handler mMainHandler;
    private int mYear = 2015,mMonth = 8,mDay = 7;
    private SimpleDraweeView mToolbarImg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_main);

        mAppbarLayout = (AppBarLayout) findViewById(R.id.toolbar_layout);
        mToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing);
        //tool bar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        mToolbarImg = (SimpleDraweeView) findViewById(R.id.toolbar_img);

        mRecyclerView = (RecyclerView) findViewById(R.id.main_recycler);
        mLayoutManager = new VirtualLayoutManager(this, OrientationHelper.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        mRecyclerView.setRecycledViewPool(viewPool);

        viewPool.setMaxRecycledViews(HomeAdapterHelper.INFO_TYPE,10);
        viewPool.setMaxRecycledViews(FOOTER_TYPE,2);
        mAdapter = new MainAdapter(mLayoutManager, true);
        mRecyclerView.setAdapter(mAdapter);


        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH) + 1;
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        mPresenter = new HomePresenter(this,this);
        mPresenter.initDataTime(mYear, mMonth, mDay);
        mMainHandler = new Handler();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.subscribe();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.unsubscribe();
    }

    @Override
    public void setPresenter(Object presenter) {

    }

    @Override
    public void onSucceed(Object data) {

    }

    @Override
    public void onError(String errorMsg) {

    }

    @Override
    public void onComplete() {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                setNextDay();
                mPresenter.subscribeByDate(mYear,mMonth,mDay);
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
    public void setAdapter(DelegateAdapter.Adapter adapter) {
        mAdapter.addAdapter(adapter);
        mAdapter.notifyItemInserted(mAdapter.getItemCount() - 1);
    }

    @Override
    public void onUpdateToolBar(String url) {
        Uri imageUri = Uri.parse(url.replace("large","bmiddle"));
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(imageUri)
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(mToolbarImg.getController())
                .build();
        mToolbarImg.setController(controller);
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
                        LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_recycler_loading_item,parent,false));
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
            progress = new MaterialProgressDrawable(MainActivity.this,circleImageView);
            progress.setBackgroundColor(Color.WHITE);
            progress.setColorSchemeColors(R.color.lightblue200,R.color.lightblue300,R.color.lightblue400,R.color.lightblue500);
            progress.setAlpha(255);
            circleImageView.setImageDrawable(progress);
        }
    }

}
