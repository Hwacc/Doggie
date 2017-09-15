package example.doggie.main.frag2;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import example.doggie.app.core.bean.BaseGankData;
import example.doggie.app.core.bean.GankDaily;
import example.doggie.app.core.bean.GankData;
import example.doggie.app.service.Gank;
import example.doggie.app.service.GankService;
import example.doggie.main.MainContract;
import io.reactivex.Observable;
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

public class Presenter2 implements MainContract.PresenterI{

    private Gank mGank;
    private GankService mGankService;
    private MainContract.View mView;
    private Disposable mDisposable;

    public Presenter2(MainContract.View view){
        mGank = Gank.getInstance();
        mGankService = mGank.getGankService();
        mView = view;
    }

    @Override
    public void subscribe() {
        if(mGankService != null){
            Observable<GankData> observable =  mGankService.getFuli(10,1)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io());

            mDisposable =  observable.subscribe(new Consumer<GankData>() {
                @Override
                public void accept(GankData gankData) throws Exception {
                    Log.d("TAG","get FuliData");
                    mView.onSucceed(gankData);
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

    }

    @Override
    public void getData() {

    }

}
