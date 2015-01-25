package ir.rasen.myapplication.helper;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.Array;

import ir.rasen.myapplication.R;
import ir.rasen.myapplication.classes.Comment;
import ir.rasen.myapplication.ui.EditTextFont;
import ir.rasen.myapplication.ui.TextViewFont;
import ir.rasen.myapplication.webservice.WebserviceResponse;
import ir.rasen.myapplication.webservice.post.DeletePost;

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

    //business_id= business.id
    //post_id = post.id
    public void showPostDeletePopup(Context context, final String buisness_id, final String post_id, final WebserviceResponse delegate) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
                .setTitle(R.string.delete_post)
                .setMessage(R.string.popup_delete)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        new DeletePost(buisness_id,post_id,delegate).execute();
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

    public void showCommentDeleteFromMyBusinessPopup(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
                .setTitle(R.string.delete_comment)
                .setMessage(R.string.popup_delete_comment_from_my_business)
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

    public void showFollowerBlockPopup(Context context, final String userId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
                .setTitle(R.string.block_follower)
                .setMessage(R.string.popup_block_follower)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // TODO:: BLOCK FOLLOWER ( ID is in userId )
                    }
                })
                .setNegativeButton(R.string.not_now, null);
        showCustomizedDialog(context, builder);
    }

    public void showChooseCategoryFirstPopup(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
                .setTitle(R.string.choose_category)
                .setMessage(R.string.choose_category_first)
                .setPositiveButton(R.string.ok, null);
        showCustomizedDialog(context, builder);
    }

    public void showCommentEditPopup(final Context context, final Comment comment) {
        final Dialog dialog = new Dialog(context,R.style.AppTheme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_edit_comment);
        final EditTextFont newComment = (EditTextFont) dialog.findViewById(R.id.edt_edit_comment_comment);
        newComment.setText(comment.text);
        dialog.findViewById(R.id.btn_edit_comment_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.btn_edit_comment_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newComment.setText(newComment.getText().toString().trim());
                if (newComment.length() < Params.COMMENT_TEXT_MIN_LENGTH) {
                    newComment.setError(context.getString(R.string.comment_is_too_short));
                    return;
                }
                if (newComment.length() > Params.COMMENT_TEXT_MAX_LENGTH) {
                    newComment.setError(context.getString(R.string.enter_is_too_long));
                    return;
                }
                // TODO:: edit comment
            }
        });
        dialog.show();
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

    public static void showMessage(Context context, String message) {
        new AlertDialog.Builder(context)
                .setTitle(R.string.popup_warning)
                .setMessage(message)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

}
