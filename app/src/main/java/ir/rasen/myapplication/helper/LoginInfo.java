package ir.rasen.myapplication.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class LoginInfo {


    public static boolean isLoggedIn(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(
                context.getPackageName(), Context.MODE_PRIVATE);
        if (!preferences.getString(Params.USER_ID, "").equals(""))
            return true;
        return false;
    }

    public static void logout(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(
                context.getPackageName(), Context.MODE_PRIVATE);
        Editor edit = preferences.edit();
        edit.putString(Params.USER_ID, "");
        edit.commit();


    }

    public static void login(Context context, String userID, String accessToken) {
        SharedPreferences preferences = context.getSharedPreferences(
                context.getPackageName(), Context.MODE_PRIVATE);
        Editor edit = preferences.edit();
        edit.putString(Params.USER_ID, userID);
        edit.putString(Params.ACCESS_TOKEN, accessToken);

        edit.commit();

    }

    public static String getUserId(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(
                context.getPackageName(), Context.MODE_PRIVATE);
        return preferences.getString(Params.USER_ID, "");
    }
    public static String getAccessToken(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(
                context.getPackageName(), Context.MODE_PRIVATE);
        return preferences.getString(Params.ACCESS_TOKEN, "");
    }

}
