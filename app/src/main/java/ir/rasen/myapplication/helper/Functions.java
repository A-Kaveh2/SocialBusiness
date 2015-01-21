package ir.rasen.myapplication.helper;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.Array;

import ir.rasen.myapplication.R;

public class Functions {

    public static Typeface getTypeface(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/font.ttf");
    }

    public static String timeToTimeAgo (Context context, int time)
    {

        if(time<60)
            return context.getString(R.string.time_now) ;

        String result = "";

        int[] values = new int[] {
            31536000,
            2592000,
            604800,
            86400,
            3600,
            60
        };
        String[] results = new String[] {
            context.getString(R.string.time_year),
            context.getString(R.string.time_month),
            context.getString(R.string.time_week),
            context.getString(R.string.time_day),
            context.getString(R.string.time_hour),
            context.getString(R.string.time_minute)
        };

        for (int i=0; i<values.length; i++) {
            if (time >= values[i])
                return ((int)(time / values[i]))+" "+results[i]+" "+context.getString(R.string.time_ago);
        }

        return "";
    }

    public void showPostDeletePopup(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
                .setTitle(R.string.delete_post)
                .setMessage(R.string.popup_delete)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // TODO:: DELETE POST
                    }
                })
                .setNegativeButton(R.string.not_now, null);
        showCustomizedDialog(context, builder);

    }

    public void showCommentDeletePopup(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
                .setTitle(R.string.delete_comment)
                .setMessage(R.string.popup_delete_comment)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // TODO:: DELETE COMMENT
                    }
                })
                .setNegativeButton(R.string.not_now, null);
        showCustomizedDialog(context, builder);
    }

    public void showFriendDeletePopup(Context context, final String friendId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
                .setTitle(R.string.delete_friend)
                .setMessage(R.string.popup_delete_friend)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // TODO:: DELETE FRIEND ( ID:: friendId )
                    }
                })
                .setNegativeButton(R.string.not_now, null);
        showCustomizedDialog(context, builder);
    }

    public void showFollowerDeletePopup(Context context, final String businessId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
                .setTitle(R.string.delete_follower)
                .setMessage(R.string.popup_delete_follower)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // TODO:: DELETE FRIEND ( ID:: businessId )
                    }
                })
                .setNegativeButton(R.string.not_now, null);
        showCustomizedDialog(context, builder);
    }

    void showCustomizedDialog(Context context, AlertDialog.Builder builder) {
        Dialog dialog = builder.show();
        Typeface tf = getTypeface(context);
        ((TextView) dialog.findViewById(android.R.id.message)).setTypeface(tf);
        ((Button) dialog.findViewById(android.R.id.button1)).setTypeface(tf);
        //int dividerId = d.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
        //View divider = d.findViewById(dividerId);
        //divider.setBackgroundColor(getResources().getColor(R.color.red_dark));
        int textViewId = dialog.getContext().getResources().getIdentifier("android:id/alertTitle", null, null);
        TextView tv = (TextView) dialog.findViewById(textViewId);
        tv.setTypeface(tf);
        //tv.setTextColor(getResources().getColor(R.color.red_dark));
    }

}
