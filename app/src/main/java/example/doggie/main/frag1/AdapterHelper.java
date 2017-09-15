package example.doggie.main.frag1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
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
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.LayoutManagerHelper;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.alibaba.android.vlayout.layout.StickyLayoutHelper;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.DraweeView;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.facebook.imagepipeline.request.Postprocessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import example.doggie.R;
import example.doggie.app.core.bean.BaseGankData;
import example.doggie.app.core.bean.GankDaily;
import example.doggie.app.utils.MatcherUtil;

/**
 * Created by Hwa on 2017/9/11.
 */

public class AdapterHelper {

    public static int GRID_TYPE = 100;
    public static int LINEAR_TYPE = 101;
    public static int STICK_TYPE = 102;

    private static final int MAX_FACE_NUM = 5;//最大可以检测出的人脸数量
    private int mRealFaceNum = 0;//实际检测出的人脸数量
    private Bitmap bm;//选择的图片的Bitmap对象
    private Paint paint;//画人脸区域用到的Paint

    private boolean hasDetected = false;//标记是否检测到人脸

    private  Context mContext;

    public AdapterHelper(Context context){
        this.mContext = context;
    }

    public List<DelegateAdapter.Adapter> getAdapters(Pair<String,GankDaily> data){

        List<DelegateAdapter.Adapter> adapters = new ArrayList<>();

        final String time = data.first;
        final List<BaseGankData> androidData = data.second.results.androidData;
        final List<BaseGankData> welfData = data.second.results.welfareData;

        DelegateAdapter.Adapter stickAdapter = new DelegateAdapter.Adapter<StickyHolder>(){
            @Override
            public StickyHolder onCreateViewHolder(ViewGroup parent, int viewType) {return new StickyHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_recycler_stick_item,parent,false));}
            @Override
            public void onBindViewHolder(StickyHolder holder, int position) {holder.textView.setText(time);}
            @Override
            public int getItemCount() {
                return 1;
            }
            @Override
            public LayoutHelper onCreateLayoutHelper() {
                return new StickyLayoutHelper(true);
            }

            @Override
            public int getItemViewType(int position) {
                return STICK_TYPE;
            }
        };
        adapters.add(stickAdapter);
        adapters.add(makeAndroidAdapter(androidData));
        adapters.add(makeWelfareAdapter(welfData));
        return adapters;
    }

    private DelegateAdapter.Adapter makeAndroidAdapter(final List<BaseGankData> androidData){
        if(androidData == null || androidData.size() == 0){
            return DelegateAdapter.simpleAdapter(LayoutInflater.from(mContext).inflate(R.layout.layout_recycler_404_item,null));
        }else {
            DelegateAdapter.Adapter androidAdapter =  new DelegateAdapter.Adapter<GridHolder>() {
                @Override
                public LayoutHelper onCreateLayoutHelper() {
                    GridLayoutHelper helper = new GridLayoutHelper(2, androidData.size(),
                            (int) mContext.getResources().getDimension(R.dimen.margin_5dp)
                    );
                    int margin = (int) mContext.getResources().getDimension(R.dimen.margin_10dp);
                    helper.setMargin(margin, margin, margin, margin);
                    return helper;
                }
                @Override
                public GridHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    GridHolder gHolder = new GridHolder(
                            LayoutInflater.from(mContext).inflate(R.layout.layout_recycler_grid_item, parent, false));
                    return gHolder;
                }

                @Override
                public void onBindViewHolder(GridHolder holder, int position) {
                    BaseGankData data = androidData.get(position);
                    if (MatcherUtil.getPictureUrl(data.url)) {
                        DraweeController controller = Fresco.newDraweeControllerBuilder()
                                .setUri(androidData.get(position).url)
                                .setOldController(holder.draweeView.getController())
                                .build();
                        holder.draweeView.setController(controller);
                    } else {
                        holder.draweeView.setImageResource(R.mipmap.android);
                    }
                    holder.titleView.setText(androidData.get(position).desc);
                }
                @Override
                public int getItemViewType(int position) {
                    return GRID_TYPE;
                }
                @Override
                public int getItemCount() {
                    return androidData.size();
                }
            };

            return androidAdapter;
        }
    }

    private DelegateAdapter.Adapter makeWelfareAdapter(final List<BaseGankData> welfareData){
        if(welfareData == null || welfareData.size() == 0){
            return DelegateAdapter.simpleAdapter(LayoutInflater.from(mContext).inflate(R.layout.layout_recycler_404_item,null));
        }else{
            DelegateAdapter.Adapter welfareAdapter = new DelegateAdapter.Adapter<LinearHolder>() {
                @Override
                public LinearHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    LinearHolder lHolder = new LinearHolder(
                            LayoutInflater.from(mContext).inflate(R.layout.layout_recycler_linear_item, parent, false));
                    return lHolder;
                }
                @Override
                public void onBindViewHolder(LinearHolder holder, int position) {
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
    }

    private Map<String,PointF> mFaceMap = new HashMap<>();
    private Postprocessor getFacePostProcessor(final String url){

        Postprocessor postprocessor = new BasePostprocessor() {
            @Override
            public String getName() {
                return "face-postprocessor";
            }

            @Override
            public void process(Bitmap bitmap) {
                bitmap = bitmap.copy(Bitmap.Config.RGB_565,true);
                if(mFaceMap.get(url) == null){
                    FaceDetectTask task = new FaceDetectTask();
                    task.execute(new Pair<>(url,bitmap));
                }
                super.process(bitmap);
            }
        };

        return postprocessor;
    }

    private  class FaceDetectTask extends AsyncTask<Pair<String,Bitmap>,Void,PointF>{

        private Pair<String, Bitmap>[] pair;
        @Override
        protected PointF doInBackground(Pair<String,Bitmap>... bitmapPair) {
            pair = bitmapPair;
            FaceDetector detector = new FaceDetector(bitmapPair[0].second.getWidth(),
                    bitmapPair[0].second.getHeight(),MAX_FACE_NUM);
            FaceDetector.Face[] faces = new FaceDetector.Face[MAX_FACE_NUM];
            mRealFaceNum = detector.findFaces(bitmapPair[0].second,faces);

            PointF focusPoint = null;

            if(faces[0] != null){
                PointF facePoint = new PointF();
                FaceDetector.Face face = faces[0];
                face.getMidPoint(facePoint);
                Log.d("TAG","face point x = "+facePoint.x+"y = "+facePoint.y);

                focusPoint = new PointF();
                int bHeight = pair[0].second.getHeight();
                int bWidth = pair[0].second.getWidth();
                focusPoint.x = facePoint.x/bWidth;
                focusPoint.y = facePoint.y/bHeight;
                Log.d("TAG","focus point x = "+focusPoint.x+"y = "+focusPoint.y);
            }

            return focusPoint;
        }

        @Override
        protected void onPostExecute(PointF focusPoint) {
//            Log.d("TAG",""+(faces[0] == null));
            if(focusPoint != null){
                mFaceMap.put(pair[0].first,focusPoint);
            }else{
                focusPoint = new PointF();
            }
        }
    }

    private class StickyHolder extends RecyclerView.ViewHolder{
        TextView textView;
        StickyHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text_view);
        }
    }

    private class LinearHolder extends RecyclerView.ViewHolder {
        DraweeView draweeView;
        TextView titleView;

        public LinearHolder(View itemView) {
            super(itemView);
            draweeView = (DraweeView) itemView.findViewById(R.id.item_drawee_view);
            titleView = (TextView) itemView.findViewById(R.id.item_title);
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
