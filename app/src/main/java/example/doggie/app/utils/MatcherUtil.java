package example.doggie.app.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Hwa on 2017/9/6.
 */

public class MatcherUtil {

    public static Boolean getPictureUrl(String text) {
        Pattern p = Pattern.compile("\\.(jpg|gif|png)");
        Matcher matcher = p.matcher(text);
        return matcher.find();
    }

}
