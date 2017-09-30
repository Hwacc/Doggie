package example.doggie.app.service;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import example.doggie.App;
import example.doggie.app.core.api.ParseVideoApi;
import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.TlsVersion;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Hwa on 2017/9/28.
 */

public class ParseVideo {

    private static ParseVideo INSTANCE;

    private ParseVideoService mPVService;

    public static synchronized ParseVideo getInstance(){
        if(INSTANCE == null) INSTANCE = new ParseVideo();
        return INSTANCE;
    }

    private ParseVideo(){

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
/*
       ConnectionSpec cs = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .tlsVersions(TlsVersion.TLS_1_1)
                .tlsVersions(TlsVersion.TLS_1_2)
                .cipherSuites(
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256
                ).build();
        ArrayList<ConnectionSpec> csList = new ArrayList<>();
        csList.add(cs);*/

        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(7676, TimeUnit.MILLISECONDS)
//                .connectionSpecs(csList)
//                .addInterceptor(new RequestInterceptor())
//                .addInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(ParseVideoApi.NEW_BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
                .addConverterFactory(GsonConverterFactory.create(App.getInstance().gson))
                .client(client)
                .build();
        Long currentTime = System.currentTimeMillis();
        this.mPVService = retrofit.create(ParseVideoService.class);
        Log.e("TAG","time = "+(System.currentTimeMillis()-currentTime));

    }
    public ParseVideoService getPVService(){return this.mPVService;}

    private class RequestInterceptor implements Interceptor{

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request().newBuilder()
                    .build();
            return chain.proceed(request);
        }
    }
}
