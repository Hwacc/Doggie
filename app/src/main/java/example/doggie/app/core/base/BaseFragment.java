package example.doggie.app.core.base;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Hwa on 2017/8/28.
 */

public abstract class BaseFragment extends Fragment {

    private IBasePresenter mPresenter;
    protected Context mContext;
    protected Resources mResources;
    protected abstract View initFragment(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);
    protected abstract IBasePresenter initPresenter();

    public interface OnPageViewSelected {int seleced();}
    protected OnPageViewSelected mSelectLinstener;

    public BaseFragment(){}

    public void setOnPageViewSelected(OnPageViewSelected selected){
        this.mSelectLinstener = selected;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = initPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return initFragment(inflater,container,savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
        mResources=context.getResources();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
