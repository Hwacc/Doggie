package example.doggie.main.activity.HomeActivity;

import android.content.Context;
import android.util.Log;
import android.util.Pair;
import android.view.View;

import com.alibaba.android.vlayout.DelegateAdapter;

import java.util.ArrayList;
import java.util.List;

import example.doggie.app.core.api.GankApi;
import example.doggie.app.core.bean.BaseGankData;
import example.doggie.app.core.bean.GankDaily;
import example.doggie.app.core.bean.ParseVideoData;
import example.doggie.app.service.Gank;
import example.doggie.app.service.GankService;
import example.doggie.app.service.ParseVideo;
import example.doggie.app.service.ParseVideoService;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Hwa on 2017/9/27.
 */

public class HomePresenter implements HomeContract.PresenterI{

    private static final String TAG = HomePresenter.class.getSimpleName();
    private HomeContract.View mView;
    private GankService mGankService;
    private ParseVideoService mPVService;
    private int mY,mM,mD;
    private Disposable mDisposable;
    private boolean mCompleteLock = false;
    private Context mContext;
    private HomeAdapterHelper mAdapterHelper;

    public HomePresenter(Context context,HomeContract.View view) {
        this.mContext = context;
        this.mView = view;
        mGankService = Gank.getInstance().getGankService();
        mPVService = ParseVideo.getInstance().getPVService();
        mAdapterHelper = new HomeAdapterHelper(context);
        mAdapterHelper.setmOnItemClickListener(new HomeAdapterHelper.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, BaseGankData data, int itemType) {
//                Log.d(TAG,"position = "+ data.url);
                mView.showDetailPop(data);
            }
        });
    }

    @Override
    public void initDataTime(int year,int month,int day){
        this.mY = year;
        this.mM = month;
        this.mD = day;
    }

    @Override
    public void subscribeByDate(int year,int month,int day){
        this.mY = year;
        this.mM = month;
        this.mD = day;
        this.subscribe();
    }


    @Override
    public void subscribe() {
        if(mGankService != null){
            Observable<String> titleObservable =  mGankService.getDaily(mY,mM,mD)
                    .subscribeOn(Schedulers.newThread())
                    .map(new Function<GankDaily, String>() {
                        @Override
                        public String apply(@NonNull GankDaily gankDaily) throws Exception {
                            if (gankDaily.results.welfareData != null){
                                return gankDaily.results.welfareData.get(0).url;
                            }
                            return "null";
                        }
                    });
//                    .observeOn(AndroidSchedulers.mainThread());
            titleObservable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                @Override
                public void accept(String s) throws Exception {
                    mView.onUpdateToolBar(s);
                }
            });

            Observable<DelegateAdapter.Adapter> observable =  mGankService.getDaily(mY,mM,mD)
                    .subscribeOn(Schedulers.io())
                    .filter(new Predicate<GankDaily>() {
                        @Override
                        public boolean test(@NonNull GankDaily gankDaily) throws Exception {
                            Log.d(TAG,"filter in "+Thread.currentThread().getName()+"thread");
                            mCompleteLock = false;
                            return !gankDaily.results.isEmpty();
                        }
                    })
                    .concatMap(new Function<GankDaily, ObservableSource<Pair<String,List<BaseGankData>>>>() {
                        @Override
                        public ObservableSource<Pair<String,List<BaseGankData>>> apply(@NonNull GankDaily gankDaily) throws Exception {
                            return Observable.fromArray(getPairData(GankApi.DATA_TYPE_ANDROID,gankDaily.results.androidData),
                                    getPairData(GankApi.DATA_TYPE_IOS,gankDaily.results.iosData),
                                    getPairData(GankApi.DATA_TYPE_JS,gankDaily.results.jsData),
                                    getPairData(GankApi.DATA_TYPE_APP,gankDaily.results.appData),
                                    getPairData(GankApi.DATA_TYPE_EXTEND_RESOURCES,gankDaily.results.resourcesData),
                                    getPairData(GankApi.DATA_TYPE_WELFARE,gankDaily.results.welfareData),
                                    getPairData(GankApi.DATA_TYPE_RECOMMEND,gankDaily.results.recommendData),
                                    getPairData(GankApi.DATA_TYPE_REST_VIDEO,gankDaily.results.videoData));
                        }
                    })
                    .filter(new Predicate<Pair<String, List<BaseGankData>>>() {
                        @Override
                        public boolean test(@NonNull Pair<String, List<BaseGankData>> stringListPair) throws Exception {
                            return stringListPair.second != null;
                        }
                    })
                    .concatMap(new Function<Pair<String, List<BaseGankData>>, ObservableSource<? extends DelegateAdapter.Adapter>>() {
                        @Override
                        public ObservableSource<? extends DelegateAdapter.Adapter> apply(@NonNull Pair<String, List<BaseGankData>> stringListPair) throws Exception {
                            List<DelegateAdapter.Adapter> adapterList = new ArrayList<>();
                            switch (stringListPair.first){
                                case GankApi.DATA_TYPE_ANDROID:
                                case GankApi.DATA_TYPE_IOS:
                                case GankApi.DATA_TYPE_JS:
                                    adapterList.add(mAdapterHelper.makeInfoAdapter(stringListPair.second));
                                    break;
                                case GankApi.DATA_TYPE_REST_VIDEO:
                                    Log.e(TAG,"get Rest Viedeo...");
//                                    subscribePV(stringListPair.second.get(0).url);
                                    adapterList.add(mAdapterHelper.makeVideoAdapter(stringListPair.second));
                                    break;
                            }
                            return Observable.fromIterable(adapterList);
                        }
                    });

            mDisposable =  observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<DelegateAdapter.Adapter>() {
                @Override
                public void accept(DelegateAdapter.Adapter adapter) throws Exception {
                    Log.d("TAG","subcribe in "+Thread.currentThread().getName()+"thread");
                    mCompleteLock = true;
                    mView.setAdapter(adapter);
                    mView.onSucceed(null);
                }

            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    // on Error
                    mView.onError(throwable.getMessage());
                }
            }, new Action() {
                @Override
                public void run() throws Exception {
                    // on Complete
                    if(!mCompleteLock){
                        mView.onComplete();
                    }
                }
            });
        }
    }
    private Pair<String,List<BaseGankData>> getPairData(String first,List<BaseGankData> second){
        return new Pair<String,List<BaseGankData>>(first,second);
    }

    @Override
    public void unsubscribe() {
        mDisposable.dispose();
    }

    @Override
    public void getData() {

    }

}
