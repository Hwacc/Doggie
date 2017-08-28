package example.doggie.app.service;

import example.doggie.app.core.bean.GankDaily;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Hwa on 2017/8/25.
 */

public interface GankService {

    @GET("day/{year}/{month}/{day}")
    Observable<GankDaily> getDaily(@Path("year") int year, @Path("month") int month, @Path("day") int day);

    @GET("data/福利/{count}/{page}")
    Observable<GankDaily> getFuli(@Path("count")int count,@Path("page") int page);
}
