package example.doggie.mvp.model;

/**
 * Created by Hwa on 2017/8/25.
 */

public class TaskManager {

    private ITaskDataSource mDataSource;
    public TaskManager(ITaskDataSource dataSource){
        this.mDataSource = dataSource;
    }

    public String getData(){
        return (String) mDataSource.getData();
    }
}
