package example.doggie.main.frag1;

import com.alibaba.android.vlayout.DelegateAdapter;

import example.doggie.app.core.base.IBasePresenter;
import example.doggie.app.core.base.IBaseView;
import example.doggie.main.MainContract;

/**
 * Created by Hwa on 2017/9/21.
 */

public interface F1Contract extends MainContract {

    interface View<T> extends IBaseView<PresenterI> {
        void onSucceed(T data);
        void onError(String errorMsg);
        void onComplete();
        void setAdapter(DelegateAdapter.Adapter adapter);
    }

    interface PresenterI extends IBasePresenter {
        void getData();
    }

}
