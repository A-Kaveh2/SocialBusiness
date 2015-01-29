package ir.rasen.myapplication.helper;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import ir.rasen.myapplication.FragmentSearch;
import ir.rasen.myapplication.R;
import ir.rasen.myapplication.classes.Comment;
import ir.rasen.myapplication.ui.EditTextFont;
import ir.rasen.myapplication.webservice.WebserviceResponse;
import ir.rasen.myapplication.webservice.post.DeletePost;
import ir.rasen.myapplication.webservice.review.DeleteReview;

public class Dialogs {

    public static Typeface getTypeface(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/font.ttf");
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

    public void showPostSharePopup(Context context, final String post_id, final WebserviceResponse delegate) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
                .setTitle(R.string.share)
                .setMessage(R.string.popup_share)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // TODO:: SHARE NOW
                        //new DeletePost(buisness_id,post_id,delegate).execute();
                    }
                })
                .setNegativeButton(R.string.not_now, null);
        showCustomizedDialog(context, builder);
    }

    public void showPostReportPopup(Context context, final String post_id, final WebserviceResponse delegate) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
                .setTitle(R.string.report)
                .setMessage(R.string.popup_report)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // TODO:: REPORT NOW
                        //new DeletePost(buisness_id,post_id,delegate).execute();
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

    public void showReviewDeletePopup(Context context, final String userId, final String review_id, final WebserviceResponse delegate) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
                .setTitle(R.string.delete_review)
                .setMessage(R.string.popup_delete_review)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        new DeleteReview(userId,review_id,delegate).execute();
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

    public void showFollowerUnblockPopup(Context context, final String userId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
                .setTitle(R.string.unblock_follower)
                .setMessage(R.string.popup_unblock_follower)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // TODO:: UNBLOCK FOLLOWER ( ID is in userId )
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

    public Dialog showCommentEditPopup(final Context context, final Comment comment) {
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
                    newComment.setErrorC(context.getString(R.string.comment_is_too_short));
                    return;
                }
                if (newComment.length() > Params.COMMENT_TEXT_MAX_LENGTH) {
                    newComment.setErrorC(context.getString(R.string.enter_is_too_long));
                    return;
                }
                // TODO:: edit comment
            }
        });
        dialog.show();
        return dialog;
    }

    public void showLocationNotFoundPopup(final FragmentSearch fragmentSearch, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(fragmentSearch.getActivity())
                .setTitle(R.string.popup_warning)
                .setMessage(message)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        fragmentSearch.setLocation();
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert);
        showCustomizedDialog(fragmentSearch.getActivity(), builder);
    }

    public void showBusinessDeletePopup(Context context, final String businessId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
                .setTitle(R.string.delete_business)
                .setMessage(R.string.popup_delete_business)
                .setPositiveButton(R.string.delete_business, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // TODO:: DELETE BUSINESS NOW
                        //new DeletePost(buisness_id,post_id,delegate).execute();
                    }
                })
                .setNegativeButton(R.string.not_now, null);
        showCustomizedDialog(context, builder);
    }

    public void showBusinessUnfollowPopup(Context context, final String businessId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
                .setTitle(R.string.unfollow_business)
                .setMessage(R.string.popup_unfollow_business)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // TODO:: UNFOLLOW BUSINESS NOW
                        //new DeletePost(buisness_id,post_id,delegate).execute();
                    }
                })
                .setNegativeButton(R.string.not_now, null);
        showCustomizedDialog(context, builder);
    }

    static Dialog showCustomizedDialog(Context context, AlertDialog.Builder builder) {
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
        return dialog;
    }

    public static void showMessage(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
            .setTitle(R.string.popup_warning)
            .setMessage(message)
            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            })
            .setIcon(android.R.drawable.ic_dialog_alert);
        showCustomizedDialog(context, builder);
    }

}
