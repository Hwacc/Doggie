package example.doggie.app.core.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Hwa on 2017/8/29.
 */

public class Error {

    // 每个请求都有error数据
    @SerializedName("error") public Boolean error;

    @SerializedName("msg") public String msg;
}
