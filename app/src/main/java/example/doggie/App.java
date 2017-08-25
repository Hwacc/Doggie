package example.doggie;

import android.app.Application;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.logging.Logger;

import example.doggie.app.core.api.GankApi;

/**
 * Created by Hwa on 2017/8/25.
 */

public class App extends Application {

    private static App ourInstance = new App();
    public boolean log = true;
    public Gson gson;

    public static final long ONE_KB = 1024L;
    public static final long ONE_MB = ONE_KB * 1024L;


    public static App getInstance() {
        return ourInstance;
    }


    @Override protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }


    @Override public void onCreate() {
        super.onCreate();
        ourInstance = this;
        this.initGson();
    }


    private void initGson() {
        this.gson = new GsonBuilder().setDateFormat(GankApi.GANK_DATA_FORMAT).create();
    }

}
