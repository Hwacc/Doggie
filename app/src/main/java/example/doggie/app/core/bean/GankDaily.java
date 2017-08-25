package example.doggie.app.core.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Hwa on 2017/8/25.
 */

public class GankDaily extends Error implements Serializable {

    @SerializedName("results") public DailyResults results;

    @SerializedName("category") public ArrayList<String> category;

    public class DailyResults {

        @SerializedName("福利") public ArrayList<BaseGankData> welfareData;

        @SerializedName("Android") public ArrayList<BaseGankData> androidData;

        @SerializedName("iOS") public ArrayList<BaseGankData> iosData;

        @SerializedName("前端") public ArrayList<BaseGankData> jsData;

        @SerializedName("休息视频") public ArrayList<BaseGankData> videoData;

        @SerializedName("拓展资源") public ArrayList<BaseGankData> resourcesData;

        @SerializedName("App") public ArrayList<BaseGankData> appData;

        @SerializedName("瞎推荐") public ArrayList<BaseGankData> recommendData;
    }
}