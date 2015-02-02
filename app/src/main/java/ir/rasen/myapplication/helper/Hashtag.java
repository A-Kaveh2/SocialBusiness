package ir.rasen.myapplication.helper;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by android on 12/28/2014.
 */
public class Hashtag {

    public static ArrayList<String> getListFromString(String hashtagListString) {
        //e.g. "hashtag1,hashtag2,hashtag3"
        return new ArrayList<String>(Arrays.asList(hashtagListString.split(",")));
    }

    public static String getStringFromList(ArrayList<String> hashtagList) {
        if (hashtagList != null && hashtagList.size() != 0) {
            String result = "";
            for (String str : hashtagList) {
                result += str + ",";
            }
            return result.substring(0, result.length() - 1);
        } else
            return "";

    }
}
