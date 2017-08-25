package example.doggie.mvp.model;

import example.doggie.app.core.bean.GankDaily;
import example.doggie.app.service.Gank;
import example.doggie.app.service.GankService;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Hwa on 2017/8/25.
 */

public class GankDataSorce implements ITaskDataSource<GankDaily> {

    @Override
    public GankDaily getData() {
        GankService service = Gank.getInstance().getGankService();
        Observable<GankDaily> observable= service.getDaily(2015,8,7);
        observable.observeOn(Schedulers.io())
                .
        return null;
    }
}
