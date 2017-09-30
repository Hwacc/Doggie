package example.doggie.main.activity.HomeActivity;

import com.alibaba.android.vlayout.DelegateAdapter;

import example.doggie.app.core.base.IBasePresenter;
import example.doggie.app.core.base.IBaseView;
import example.doggie.app.core.bean.BaseGankData;
import example.doggie.main.MainContract;

/**
 * Created by Hwa on 2017/9/27.
 */

public class HomeContract {

    interface View<T> extends IBaseView<MainContract.PresenterI> {
        void onSucceed(T data);
        void onError(String errorMsg);
        void onComplete();
        void setAdapter(DelegateAdapter.Adapter adapter);
        void onUpdateToolBar(String url);
        void showDetailPop(BaseGankData data);
    }

    interface PresenterI extends IBasePresenter {
        void getData();
        void initDataTime(int year,int month,int day);
        void subscribeByDate(int year,int month,int day);
    }

}
