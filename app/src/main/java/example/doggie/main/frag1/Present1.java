package example.doggie.main.frag1;

import android.util.Log;
import android.view.ViewGroup;

import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.List;
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
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
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

    @Override
    public void subscribe() {
        if(mGankService != null){
            Observable<List<BaseGankData>> observable =  mGankService.getDaily(mY,mM,mD)
                    .map(new Function<GankDaily, List<BaseGankData>>() {
                        List<BaseGankData> dataList = new ArrayList<BaseGankData>();
                        @Override
                        public List<BaseGankData> apply(@NonNull GankDaily gankDaily) throws Exception {
                            Log.d("TAG","map in "+Thread.currentThread().getName()+"thread");
                            dataList.addAll(gankDaily.results.androidData);
                            dataList.addAll(gankDaily.results.welfareData);
                            return dataList;
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());

            mDisposable =  observable.subscribe(new Consumer<List<BaseGankData>>() {

                @Override
                public void accept(List<BaseGankData> baseGankDatas) throws Exception {
                    Log.d("TAG","get GankData");
                    Log.d("TAG","subcribe in "+Thread.currentThread().getName()+"thread");
                    mView.showData(baseGankDatas);
                }

            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    // on Error
                    Log.e("ERROR",throwable.getMessage());
                }
            }, new Action() {
                @Override
                public void run() throws Exception {
                    // on Complete
                }
            });
        }
    }

    @Override
    public void unsubscribe() {
        mDisposable.dispose();
    }

    @Override
    public void getData() {

    }
}
