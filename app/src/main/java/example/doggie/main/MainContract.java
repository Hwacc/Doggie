package example.doggie.main;

import java.util.concurrent.Callable;

import example.doggie.app.core.base.IBasePresenter;
import example.doggie.app.core.base.IBaseView;

/**
 * Created by Hwa on 2017/8/28.
 */

public interface MainContract {

    interface View<T> extends IBaseView<PresenterI> {
        void onSucceed(T data);
        void onError(String errorMsg);
        void onComplete();
    }

    interface PresenterI extends IBasePresenter {
        void getData();
    }

}
