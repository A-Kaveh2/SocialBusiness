package ir.rasen.myapplication.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class LoginInfo {

    private static String USER_ID = "user_id";

    public static boolean isLoggedIn(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(
                context.getPackageName(), Context.MODE_PRIVATE);
        if (!preferences.getString(USER_ID, "").equals(""))
            return true;
        return false;
    }

    public static void logout(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(
                context.getPackageName(), Context.MODE_PRIVATE);
        Editor edit = preferences.edit();
        edit.putString(USER_ID, "");
        edit.commit();


    }

    public static void login(Context context, String userID) {
        SharedPreferences preferences = context.getSharedPreferences(
                context.getPackageName(), Context.MODE_PRIVATE);
        Editor edit = preferences.edit();
        edit.putString(USER_ID, userID);
        edit.commit();

    }

}
