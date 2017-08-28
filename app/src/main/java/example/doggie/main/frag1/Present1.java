package example.doggie.main.frag1;

import android.util.Log;

import example.doggie.app.core.base.IBasePresenter;
import example.doggie.app.core.bean.GankDaily;
import example.doggie.app.service.Gank;
import example.doggie.app.service.GankService;
import example.doggie.main.MainContract;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
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
           Observable<GankDaily> observable =  mGankService.getDaily(mY,mM,mD)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io());

            mDisposable =  observable.subscribe(new Consumer<GankDaily>() {
                @Override
                public void accept(GankDaily gankDaily) throws Exception {
                    Log.d("TAG","get GankData");
                    mView.showData(gankDaily);
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
