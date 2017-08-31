package example.doggie.main.frag1;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.AbstractDraweeController;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.DraweeView;

import java.util.ArrayList;
import java.util.List;

import example.doggie.R;
import example.doggie.app.core.base.BaseFragment;
import example.doggie.app.core.base.IBasePresenter;
import example.doggie.app.core.bean.BaseGankData;
import example.doggie.app.core.bean.GankDaily;
import example.doggie.main.MainContract;

/**
 * Created by Hwa on 2017/8/28.
 */

public class Fragment1 extends BaseFragment implements MainContract.View{

    private RecyclerView mRecycler;
    private MainRecycleAdapter mAdapter;
    private List<BaseGankData> mDatas = new ArrayList<>();

    public static Fragment1 newInstance() {
       return new Fragment1();
    }

    @Override
    public View initFragment(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.layout_frag1,container,false);
        mRecycler = (RecyclerView) root.findViewById(R.id.frag1_recycler);
        mRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new MainRecycleAdapter();
        mRecycler.setAdapter(mAdapter);
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
        mDatas.addAll((List<BaseGankData>)data);
        mAdapter.notifyDataSetChanged();
    }

    public class MainRecycleAdapter extends RecyclerView.Adapter<MainRecycleAdapter.MainRecycleHolder>{

        public MainRecycleAdapter(){

        }
        @Override
        public MainRecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MainRecycleHolder holder = new MainRecycleHolder(
                    LayoutInflater.from(mContext).inflate(R.layout.layout_frag1_recycler_item,parent,false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MainRecycleHolder holder, int position) {
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(mDatas.get(position).url)
                    .setOldController(holder.draweeView.getController())
                    .build();
            holder.draweeView.setController(controller);
        }

        @Override
        public int getItemCount() {
            return mDatas == null ? 0:mDatas.size();
        }

        class MainRecycleHolder extends RecyclerView.ViewHolder{

            DraweeView draweeView;
            public MainRecycleHolder(View itemView) {
                super(itemView);
                draweeView = (DraweeView) itemView.findViewById(R.id.item_drawee_view);
            }
        }

    }
}
