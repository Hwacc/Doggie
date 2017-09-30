package example.doggie.app.service;

import example.doggie.app.core.bean.ParseVideoData;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Hwa on 2017/9/28.
 */

public interface ParseVideoService {
    @GET("GetVideoUrl.php")
    Observable<ParseVideoData>getParseVideoData(@Query("cid") String cid, @Query("type") String type, @Query("quality") int quality);
}
