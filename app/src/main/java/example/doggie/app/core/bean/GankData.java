package example.doggie.app.core.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Hwa on 2017/8/29.
 */

public class GankData extends Error implements Serializable {

    @SerializedName("results") public ArrayList<BaseGankData> results;
}