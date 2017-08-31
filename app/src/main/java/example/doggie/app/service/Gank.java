package example.doggie.app.service;

import java.util.concurrent.TimeUnit;

import example.doggie.App;
import example.doggie.app.core.api.GankApi;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Hwa on 2017/8/25.
 */

public class Gank {

    private static Gank INSTANCE;

    private GankService mGankService;

    public static Gank getInstance(){
            if(INSTANCE == null) INSTANCE = new Gank();
            return INSTANCE;
    }

    private Gank(){

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(7676, TimeUnit.MILLISECONDS)
//                .addInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(GankApi.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(App.getInstance().gson))
                .client(client)
                .build();

        this.mGankService = retrofit.create(GankService.class);

    }

    public GankService getGankService(){return this.mGankService;}
}
