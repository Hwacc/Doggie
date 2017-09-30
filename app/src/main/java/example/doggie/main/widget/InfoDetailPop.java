package example.doggie.main.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.memory.PooledByteBuffer;
import com.facebook.common.memory.PooledByteBufferInputStream;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.BaseDataSubscriber;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import example.doggie.R;
import example.doggie.app.core.api.GankApi;
import example.doggie.app.core.bean.BaseGankData;
import example.easypopup.lib.BaseCustomPopup;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by Hwa on 2017/9/29.
 */

public class InfoDetailPop extends BaseCustomPopup {

    private static final String TAG = InfoDetailPop.class.getSimpleName();

    @Bind(R.id.pop_viewpager) ViewPager mPager;
    @Bind(R.id.pop_title) TextView mTitleText;
    @Bind(R.id.pop_autor) TextView mAuthorText;
    @Bind(R.id.pop_date) TextView mDateText;
    @Bind(R.id.pop_btn_go) Button mGoBtn;
    @Bind(R.id.pop_btn_cancel) Button mCancelBtn;
    private BaseGankData mData;
    private Context mContext;
    private PopPagerAdapter mAdapter;
    private List<GifDrawable> mGifDrawableList = new ArrayList<>();
    private CloseableReference<PooledByteBuffer> mRef;

    public InfoDetailPop(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected void initAttributes() {
        setContentView(R.layout.layout_popup_info_detail);
        setFocusAndOutsideEnable(false)
                .setBackgroundDimEnable(true)
                .setDimValue(0.5f);
    }

    @Override
    protected void initViews(View view) {
        ButterKnife.bind(this,view);
    }

    public void setData(BaseGankData data){
        if(data != null){
            mData = data;
            if(mAdapter == null){
                mAdapter = new PopPagerAdapter(data);
                mPager.setAdapter(mAdapter);
            }else{
                mAdapter.resetData(data);
            }

            if(data.imagesList != null && data.imagesList.size()>0){
                for (String image:data.imagesList) {
                    ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(image)).build();
                    ImagePipeline imagePipeline = Fresco.getImagePipeline();
                    DataSource<CloseableReference<PooledByteBuffer>> dataSource = imagePipeline.fetchEncodedImage(request, mContext);
                    BaseDataSubscriber<CloseableReference<PooledByteBuffer>> dataSubscriber =
                            new BaseDataSubscriber<CloseableReference<PooledByteBuffer>>() {

                                @Override
                                protected void onNewResultImpl(DataSource<CloseableReference<PooledByteBuffer>> dataSource) {
                                    if(!dataSource.isFinished()){
                                        return;
                                    }
                                    mRef = dataSource.getResult();
                                    if(mRef != null){
                                        try {
                                            PooledByteBuffer buffer = mRef.get();
                                            InputStream is = new PooledByteBufferInputStream(buffer);
                                            try {
                                                mGifDrawableList.add(new GifDrawable(is));
                                                Message msg = Message.obtain();
                                                msg.arg1 = 0;
                                                mPopHandler.sendMessage(msg);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }finally {
                                                //不能在这关闭InputStream
                                                //InputStreams are closed automatically in finalizer if GifDrawable is no longer needed so you don't need to explicitly close them.
                                                //see At https://github.com/koral--/android-gif-drawable
//                                                Closeables.closeQuietly(is);
                                            }
                                            Log.d(TAG,"buffer = "+buffer.toString());
                                        } finally {
                                            //对ref的引用也不能在这关闭，Fresco说可以保持数据源结果的引用，但是需要手动关闭
//                                            CloseableReference.closeSafely(ref);
                                        }
                                    }
                                }

                                @Override
                                protected void onFailureImpl(DataSource<CloseableReference<PooledByteBuffer>> dataSource) {
                                    Throwable t = dataSource.getFailureCause();
                                    try {
                                        throw new Exception(t);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                    dataSource.subscribe(dataSubscriber, CallerThreadExecutor.getInstance());
                }
            }

        }
    }

    @Override
    public void onDismiss() {
        mGifDrawableList.clear();
        //在这里关闭Ref引用
        CloseableReference.closeSafely(mRef);
        mAdapter =null;
        super.onDismiss();
    }

    private Handler mPopHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.arg1 == 0){
                Log.e(TAG,"handle message.. thread = "+Thread.currentThread().getName());
                mAdapter.updateDrawable(mGifDrawableList);
            }
        }
    };

    class PopPagerAdapter extends PagerAdapter{

        private List<String> images;
        private List<View> viewList = new ArrayList<>();
        private List<GifDrawable> gifDrawableList = new ArrayList<>();

        public PopPagerAdapter(BaseGankData data){
            Log.d(TAG,"Creat adapter..");
            images = data.imagesList;
            if(images != null && images.size() >0){
                initDraweeViewList();
            }else{
                images = new ArrayList<>();
                images.add(data.type);
                initImageList();
            }
        }
        public void resetData(BaseGankData data){
            Log.d(TAG,"reset Data..");
            images = data.imagesList;
            if(images != null && images.size() >0){
                initDraweeViewList();
            }else{
                images = new ArrayList<>();
                images.add(data.type);
                initImageList();
            }
            this.notifyDataSetChanged();
        }
        public void updateDrawable(List<GifDrawable> list){
            this.gifDrawableList.clear();
            this.gifDrawableList.addAll(list);
            this.notifyDataSetChanged();
        }

        private void initDraweeViewList(){
            for (int i = 0;i<images.size();i++) {
                String image = images.get(i);
/*              SimpleDraweeView draweeView = new SimpleDraweeView(mContext);
                draweeView.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
                draweeView.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER);*/

                final GifImageView gifImageView = new GifImageView(mContext);
                gifImageView.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
                gifImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

/*                DraweeController controller = Fresco.newDraweeControllerBuilder()
                        .setImageRequest(request)
                        .setAutoPlayAnimations(true)
                        .setOldController(draweeView.getController())
                        .build();
                draweeView.setController(controller);*/
                viewList.add(gifImageView);
            }
        }
        private void initImageList(){
            for (String image:images
                 ) {
                Drawable drawable = null;
                switch (image){
                    case GankApi.DATA_TYPE_ANDROID:
                        drawable = mContext.getDrawable(R.mipmap.android);
                        break;
                    case GankApi.DATA_TYPE_IOS:
                        drawable = mContext.getDrawable(R.mipmap.ios_logo);
                        break;
                    case GankApi.DATA_TYPE_JS:
                        drawable = mContext.getDrawable(R.mipmap.js_logo);
                        break;
                    default:
                        break;
                }
                ImageView imageView = new ImageView(mContext);
                imageView.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageView.setImageDrawable(drawable);
                viewList.add(imageView);
            }
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            GifImageView gifView = null;
            GifDrawable drawable = null;
            Log.e(TAG,"notify with list size = "+gifDrawableList.size());
            if(gifDrawableList.size ()>0){
                gifView = (GifImageView) viewList.get(position);
                drawable = gifDrawableList.get(position);
            }
            if(drawable != null){
                Log.e(TAG,"set drawable...");
                gifView.setImageDrawable(drawable);
            }
            container.addView(viewList.get(position));
            return viewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
/*          images.clear();
            viewList.clear();*/
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return images == null? 1 : images.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
