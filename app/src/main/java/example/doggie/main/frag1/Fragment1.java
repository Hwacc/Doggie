package example.doggie.main.frag1;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import example.doggie.R;
import example.doggie.app.core.base.BaseFragment;
import example.doggie.app.core.base.IBasePresenter;
import example.doggie.app.core.bean.GankDaily;
import example.doggie.main.MainContract;

/**
 * Created by Hwa on 2017/8/28.
 */

public class Fragment1 extends BaseFragment implements MainContract.View{

    private RecyclerView mRecycler;

    public static Fragment1 newInstance() {
       return new Fragment1();
    }

    @Override
    public View initFragment(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.layout_frag1,container,false);
        mRecycler = (RecyclerView) root.findViewById(R.id.frag1_recycler);
        mRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        mRecycler.setAdapter();
        return root;
    }

    @Override
    public IBasePresenter initPresenter() {
        Present1 present = new Present1(this);
        present.initDataTime(2015,8,7);
        return present;
    }


    @Override
    public void setPresenter(Object presenter) {

    }

    @Override
    public void showData(Object data) {
        GankDaily gank = (GankDaily)data;
    }

    public class MainRecycleAdapter extends RecyclerView.Adapter<MainRecycleAdapter.MainRecycleHolder>{

        @Override
        public MainRecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(MainRecycleHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }

        class MainRecycleHolder extends RecyclerView.ViewHolder{

            public MainRecycleHolder(View itemView) {
                super(itemView);

            }
        }

    }
}
