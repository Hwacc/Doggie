package example.doggie.app.core.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Hwa on 2017/8/25.
 */

public class BaseGankData implements Serializable {

    // 发布人
    @SerializedName("who") public String who;

    // 发布时间
    @SerializedName("publishedAt") public Date publishedAt;

    // 标题
    @SerializedName("desc") public String desc;

    // 类型， 一般都是"福利"
    @SerializedName("type") public String type;

    // 图片url
    @SerializedName("url") public String url;

    // 是否可用
    @SerializedName("used") public Boolean used;

    // 对象id
    @SerializedName("objectId") public String objectId;

    // 创建时间
    @SerializedName("createdAt") public Date createdAt;

    // 更新时间
    @SerializedName("updatedAt") public Date updatedAt;

    //图片list
    @SerializedName("images") public ArrayList<String> imagesList;
}