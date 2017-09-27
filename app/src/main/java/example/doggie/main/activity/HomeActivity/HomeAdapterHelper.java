package example.doggie.main.activity.HomeActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.media.FaceDetector;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.DraweeView;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.facebook.imagepipeline.request.Postprocessor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import example.doggie.R;
import example.doggie.app.core.api.GankApi;
import example.doggie.app.core.bean.BaseGankData;
import example.doggie.app.utils.MatcherUtil;
import example.doggie.main.widget.StickyLayoutHelper;

/**
 * Created by Hwa on 2017/9/27.
 */

public class HomeAdapterHelper {
    public static final int INFO_TYPE = 100;
    public static final int LINEAR_TYPE = 101;
    public static final int STICK_TYPE = 102;

    private static final String TAG = HomeAdapterHelper.class.getSimpleName();

    private Context mContext;

    public HomeAdapterHelper(Context context){
        this.mContext = context;
    }


    public DelegateAdapter.Adapter makeStickyAdapter(final int year, final int month, final int day){

        DelegateAdapter.Adapter stickAdapter = new DelegateAdapter.Adapter<HomeAdapterHelper.StickyHolder>(){
            @Override
            public HomeAdapterHelper.StickyHolder onCreateViewHolder(ViewGroup parent, int viewType) {return new HomeAdapterHelper.StickyHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_recycler_stick_item,parent,false));}
            @Override
            public void onBindViewHolder(HomeAdapterHelper.StickyHolder holder, int position) {
                StringBuffer sb = new StringBuffer();
                sb.append(year).append('-').append(month).append('-').append(day);
                holder.textView.setText(sb.toString());}
            @Override
            public int getItemCount() {
                return 1;
            }
            @Override
            public LayoutHelper onCreateLayoutHelper() {
                StickyLayoutHelper helper = new StickyLayoutHelper(true);
                return helper;
            }

            @Override
            public int getItemViewType(int position) {
                return STICK_TYPE;
            }
        };
        return stickAdapter;
    }

    public DelegateAdapter.Adapter makeLinearAdapter(final List<BaseGankData> datas){
        if(datas == null || datas.size() == 0){
            return DelegateAdapter.simpleAdapter(LayoutInflater.from(mContext).inflate(R.layout.layout_recycler_404_item,null));
        }else {
            DelegateAdapter.Adapter androidAdapter =  new DelegateAdapter.Adapter<HomeAdapterHelper.InfoHolder>() {
                @Override
                public LayoutHelper onCreateLayoutHelper() {
                    LinearLayoutHelper helper = new LinearLayoutHelper();
                    int margin = (int) mContext.getResources().getDimension(R.dimen.margin_10dp);
                    helper.setMargin(margin, margin, margin, margin);
                    return helper;
                }
                @Override
                public HomeAdapterHelper.InfoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    HomeAdapterHelper.InfoHolder holder = new HomeAdapterHelper.InfoHolder(
                            LayoutInflater.from(mContext).inflate(R.layout.layout_recycler_item_linear, parent, false));
                    return holder;
                }

                @Override
                public void onBindViewHolder(HomeAdapterHelper.InfoHolder holder, int position) {
                    BaseGankData data = datas.get(position);
                    if(data.type.equals(GankApi.DATA_TYPE_ANDROID)){
                        holder.titleImgView.setImageResource(R.mipmap.android);
                        holder.titleView.setText(data.desc);
                        holder.authorView.setText(data.who);
                    }
                }

                @Override
                public int getItemViewType(int position) {
                    return INFO_TYPE;
                }
                @Override
                public int getItemCount() {
                    return datas.size();
                }
            };
            return androidAdapter;
        }
    }

    /*public DelegateAdapter.Adapter makeWelfareAdapter(final List<BaseGankData> welfareData){
        if(welfareData == null || welfareData.size() == 0){
            return DelegateAdapter.simpleAdapter(LayoutInflater.from(mContext).inflate(R.layout.layout_recycler_404_item,null));
        }else{
            DelegateAdapter.Adapter welfareAdapter = new DelegateAdapter.Adapter<AdapterHelper.LinearHolder>() {
                @Override
                public AdapterHelper.LinearHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    AdapterHelper.LinearHolder lHolder = new AdapterHelper.LinearHolder(
                            LayoutInflater.from(mContext).inflate(R.layout.layout_recycler_linear_item, parent, false));
                    return lHolder;
                }
                @Override
                public void onBindViewHolder(AdapterHelper.LinearHolder holder, int position) {
                    Uri imageUri = Uri.parse(welfareData.get(position).url.replace("large","bmiddle"));
                    ImageRequest request = ImageRequestBuilder.newBuilderWithSource(imageUri)
                            .setPostprocessor(getFacePostProcessor(imageUri.getPath()))
                            .build();
                    DraweeController controller = Fresco.newDraweeControllerBuilder()
                            .setImageRequest(request)
                            .setOldController(holder.draweeView.getController())
                            .build();
                    holder.draweeView.setController(controller);
                    ((GenericDraweeHierarchy)holder.draweeView.getHierarchy())
                            .setActualImageFocusPoint(
                                    mFaceMap.get(imageUri.getPath())==null ? new PointF(0.5f,0.5f):mFaceMap.get(imageUri.getPath()));
                    holder.titleView.setText(welfareData.get(position).desc);
                }

                @Override
                public int getItemCount() {
                    return welfareData.size();
                }

                @Override
                public int getItemViewType(int position) {
                    return LINEAR_TYPE;
                }

                @Override
                public LayoutHelper onCreateLayoutHelper() {
                    LinearLayoutHelper helper = new LinearLayoutHelper();
                    int margin = (int) mContext.getResources().getDimension(R.dimen.margin_10dp);
                    helper.setMargin(margin,0,margin,margin);
                    return helper ;
                }
            };
            return welfareAdapter;
        }
    }*/

    private class StickyHolder extends RecyclerView.ViewHolder{
        TextView textView;
        StickyHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text_view);
        }
    }

    private class InfoHolder extends RecyclerView.ViewHolder {
        ImageView titleImgView;
        TextView titleView;
        TextView authorView;

        public InfoHolder(View itemView) {
            super(itemView);
            titleImgView = (ImageView) itemView.findViewById(R.id.item_info_img);
            titleView = (TextView) itemView.findViewById(R.id.item_info_title);
            authorView = (TextView) itemView.findViewById(R.id.item_info_author);
        }
    }

    private class GridHolder extends RecyclerView.ViewHolder {
        DraweeView draweeView;
        TextView titleView;

        public GridHolder(View itemView) {
            super(itemView);
            draweeView = (DraweeView) itemView.findViewById(R.id.item_drawee_view);
            titleView = (TextView) itemView.findViewById(R.id.item_title);
        }
    }
}
