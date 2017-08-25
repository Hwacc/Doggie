package example.doggie.mvp.model;

/**
 * Created by Hwa on 2017/8/25.
 */

public class StringDataSource implements ITaskDataSource {

    @Override
    public Object getData() {
        return "This is MVP";
    }

}
