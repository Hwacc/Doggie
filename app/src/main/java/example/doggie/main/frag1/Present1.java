package example.doggie.main.frag1;

import android.os.Handler;
import android.support.v4.util.Pair;
import android.util.Log;
import android.view.ViewGroup;

import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import example.doggie.app.core.base.IBasePresenter;
import example.doggie.app.core.bean.BaseGankData;
import example.doggie.app.core.bean.GankDaily;
import example.doggie.app.service.Gank;
import example.doggie.app.service.GankService;
import example.doggie.main.MainContract;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Hwa on 2017/8/28.
 */

public class Present1 implements MainContract.PresenterI, IBasePresenter {

    private MainContract.View mView;
    private Gank mGank;
    private GankService mGankService;
    private int mY,mM,mD;
    private Disposable mDisposable;
    private boolean mCompleteLock = false;


    public Present1(MainContract.View view){
        mGank = Gank.getInstance();
        mGankService = mGank.getGankService();
        mView = view;
    }

    public void initDataTime(int year,int month,int day){
        this.mY = year;
        this.mM = month;
        this.mD = day;
    }

    public void subscribeByDate(int year,int month,int day){
        this.mY = year;
        this.mM = month;
        this.mD = day;
        this.subscribe();
    }

    @Override
    public void subscribe() {
        if(mGankService != null){
            Observable<GankDaily> observable =  mGankService.getDaily(mY,mM,mD)
                    .filter(new Predicate<GankDaily>() {
                        @Override
                        public boolean test(@NonNull GankDaily gankDaily) throws Exception {
                            mCompleteLock = false;
                            return !(gankDaily.results.androidData == null || gankDaily.results.welfareData == null);
                        }
                    })
                   /*.map(new Function<GankDaily,GankDaily>() {
                        @Override
                        public GankDaily apply(@NonNull GankDaily gankDaily) throws Exception {
                            Log.d("TAG","map in "+Thread.currentThread().getName()+"thread");
                            dataMap.put(mY+"-"+mM+"-"+mD,gankDaily);
                            return dataMap;
                        }
                    })*/
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());

            mDisposable =  observable.subscribe(new Consumer<GankDaily>() {
                @Override
                public void accept(GankDaily baseGankDatas) throws Exception {
                    Log.d("TAG","subcribe in "+Thread.currentThread().getName()+"thread");
                    mCompleteLock = true;
                    final Pair<String,GankDaily> data = new Pair<String,GankDaily>(mY+"-"+mM+"-"+mD,baseGankDatas);
                    mView.onSucceed(data);
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
    @Override
    public void unsubscribe() {mDisposable.dispose();}

    @Override
    public void getData() {}
}
