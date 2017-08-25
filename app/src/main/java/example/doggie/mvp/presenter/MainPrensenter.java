package example.doggie.mvp.presenter;

import example.doggie.mvp.model.StringDataSource;
import example.doggie.mvp.model.TaskManager;
import example.doggie.mvp.ui.activity.IMainView;

/**
 * Created by Hwa on 2017/8/25.
 */

public class MainPrensenter  implements IMainPresenter{
    private IMainView mMainView;
    private TaskManager mManager;

    public MainPrensenter(IMainView iMainView){
        mMainView = iMainView;
        mManager = new TaskManager(new StringDataSource());
    }

    @Override
    public void getString() {
        mMainView.showStringText(mManager.getData());
    }
}
