package example.doggie.main.frag2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import example.doggie.R;
import example.doggie.app.core.base.BaseFragment;
import example.doggie.app.core.base.IBasePresenter;
import example.doggie.app.core.bean.GankData;
import example.doggie.main.MainContract;

/**
 * Created by Hwa on 2017/8/28.
 */

public class Fragment2 extends BaseFragment implements MainContract.View{

    public static Fragment2 newInstance() {
        return new Fragment2();
    }

    @Override
    protected View initFragment(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.layout_frag2,container,false);
        return root;
    }

    @Override
    protected IBasePresenter initPresenter() {
        Presenter2 presenter2 = new Presenter2(this);
        return presenter2;
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

    }
}
