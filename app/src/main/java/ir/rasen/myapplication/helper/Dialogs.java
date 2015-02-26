package ir.rasen.myapplication.helper;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;

import ir.rasen.myapplication.ActivityMain;
import ir.rasen.myapplication.FragmentSearch;
import ir.rasen.myapplication.R;
import ir.rasen.myapplication.classes.Comment;
import ir.rasen.myapplication.classes.Post;
import ir.rasen.myapplication.ui.EditTextFont;
import ir.rasen.myapplication.ui.ProgressDialogCustom;
import ir.rasen.myapplication.webservice.WebserviceResponse;
import ir.rasen.myapplication.webservice.business.BlockUser;
import ir.rasen.myapplication.webservice.business.DeleteBusiness;
import ir.rasen.myapplication.webservice.comment.DeleteComment;
import ir.rasen.myapplication.webservice.business.UnblockUser;
import ir.rasen.myapplication.webservice.comment.UpdateComment;
import ir.rasen.myapplication.webservice.post.DeletePost;
import ir.rasen.myapplication.webservice.post.Report;
import ir.rasen.myapplication.webservice.review.DeleteReview;

public class Dialogs {

    public static Typeface getTypeface(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/font.ttf");
    }

    //business_id= business.id
    //post_id = post.id

    public void showPostDeletePopup(Context context, final int buisness_id, final int post_id, final WebserviceResponse delegate) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
                .setTitle(R.string.delete_post)
                .setMessage(R.string.popup_delete)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        new DeletePost(buisness_id, post_id, delegate).execute();
                    }
                })
                .setNegativeButton(R.string.not_now, null);
        showCustomizedDialog(context, builder);
    }

    public void showPostSharePopup(final Context context, final Post post, final WebserviceResponse delegate) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
                .setTitle(R.string.share)
                .setMessage(R.string.popup_share)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // TODO:: SHARE NOW

                        // TODO TESTING INSTAGRAM SHARE FOR FUTURE...
                        File file = new File( Environment.getExternalStorageDirectory().getAbsolutePath() +
                            context.getResources().getString(R.string.download_storage_path), String.valueOf(post.pictureId) + "_" + String.valueOf(1) + ".jpg");

                        String type = "image/*";
                        String captionText = post.description;

                        createInstagramIntent(context, type, file.getAbsolutePath(), captionText);

                    }
                })
                .setNegativeButton(R.string.not_now, null);
        showCustomizedDialog(context, builder);
    }

    public void showPostReportPopup(final Context context, final int post_id, final WebserviceResponse delegate, final ProgressDialogCustom pd) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
                .setTitle(R.string.report)
                .setMessage(R.string.popup_report)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // REPORT NOW
                        pd.show();
                        new Report(LoginInfo.getUserId(context),post_id,delegate).execute();
                    }
                })
                .setNegativeButton(R.string.not_now, null);
        showCustomizedDialog(context, builder);
    }

    public void showCommentDeletePopup(final Context context, final int businessId, final int commentId, final WebserviceResponse delegate, final EditInterface editDelegateInterface) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
                .setTitle(R.string.delete_comment)
                .setMessage(R.string.popup_delete_comment)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        new DeleteComment(businessId, commentId, delegate).execute();
                    }
                })
                .setNegativeButton(R.string.not_now, null)
        .setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                editDelegateInterface.setEditing(0, null, null);
            }
        });
        showCustomizedDialog(context, builder);
    }

    public void showCommentDeleteFromMyBusinessPopup(final Context context, final int businessId, final int commentId, final WebserviceResponse delegate, final ProgressDialogCustom pd) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
                .setTitle(R.string.delete_comment)
                .setMessage(R.string.popup_delete_comment_from_my_business)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // DELETE COMMENT from my business
                        new DeleteComment(businessId, commentId, delegate);
                        pd.show();
                    }
                })
                .setNegativeButton(R.string.not_now, null);
        showCustomizedDialog(context, builder);
    }

    public void showFriendDeletePopup(Context context, final int friendId) {
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

    public void showReviewDeletePopup(Context context, final int userId, final int review_id, final WebserviceResponse delegate) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
                .setTitle(R.string.delete_review)
                .setMessage(R.string.popup_delete_review)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        new DeleteReview(userId, review_id, delegate).execute();
                    }
                })
                .setNegativeButton(R.string.not_now, null);
        showCustomizedDialog(context, builder);
    }

    public void showFollowerBlockPopup(Context context,final int businessId, final int userId,final WebserviceResponse delegate, final EditInterface editDelegate) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
                .setTitle(R.string.block_follower)
                .setMessage(R.string.popup_block_follower)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // BLOCK FOLLOWER ( ID is in userId )
                        new BlockUser(businessId,userId,delegate).execute();
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        editDelegate.setEditing(0,null,null);
                    }
                })
                .setNegativeButton(R.string.not_now, null);
        showCustomizedDialog(context, builder);
    }

    public void showFollowerUnblockPopup(Context context,final int businessId, final int userId,final WebserviceResponse delegate, final EditInterface editDelegate) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
                .setTitle(R.string.unblock_follower)
                .setMessage(R.string.popup_unblock_follower)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        new UnblockUser(businessId, userId, delegate).execute();
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        editDelegate.setEditing(0, null, null);
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

    public Dialog showCommentEditPopup(final Context context, final Comment comment,final WebserviceResponse delegate, final EditInterface editDelegate) {
        final Dialog dialog = new Dialog(context, R.style.AppTheme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_edit_comment);
        final EditTextFont newComment = (EditTextFont) dialog.findViewById(R.id.edt_edit_comment_comment);
        newComment.setText(comment.text);
        dialog.findViewById(R.id.btn_edit_comment_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editDelegate.setEditing(0,null,null);
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
                new UpdateComment(comment, delegate).execute();
                dialog.dismiss();

            }
        });
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                editDelegate.setEditing(0,null,null);
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

    public void showBusinessDeletePopup(final Context context, final int businessId, final WebserviceResponse delegate, ProgressDialogCustom pd) {
        pd.show();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
                .setTitle(R.string.delete_business)
                .setMessage(R.string.popup_delete_business)
                .setPositiveButton(R.string.delete_business, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        new DeleteBusiness(LoginInfo.getUserId(context), businessId, delegate).execute();


                    }
                })
                .setNegativeButton(R.string.not_now, null);
        showCustomizedDialog(context, builder);
    }

    public void showBusinessUnfollowPopup(Context context, final int businessId,WebserviceResponse delegate) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder
                .setTitle(R.string.unfollow_business)
                .setMessage(R.string.popup_unfollow_business)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // TODO:: UNFOLLOW BUSINESS NOW
                        //new DeletePost(businessId,postId,delegate).execute();
                    }
                })
                .setNegativeButton(R.string.not_now, null);
        showCustomizedDialog(context, builder);
    }
    public static void showExitDialog(final ActivityMain activityMain) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activityMain);
        builder
                .setTitle(R.string.exit_app)
                .setMessage(R.string.popup_exit_app)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        activityMain.finish();
                        activityMain.overridePendingTransition(R.anim.to_0_from_left, R.anim.to_right);
                    }
                })
                .setNegativeButton(R.string.not_now, null);
        showCustomizedDialog(activityMain, builder);
    }

    public static Dialog showCustomizedDialog(Context context, AlertDialog.Builder builder) {
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

    public static void showMessage(final Context context, String message, final boolean back) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(R.string.popup_warning)
                .setMessage(message)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (back) {
                            try {
                                ((Activity) context).onBackPressed();
                            } catch (Exception e) {
                            }
                        }
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert);
        showCustomizedDialog(context, builder).show();
    }

    private void createInstagramIntent(Context context, String type, String mediaPath, String caption){

        // Create the new Intent using the 'Send' action.
        Intent share = new Intent(Intent.ACTION_SEND);

        // Set the MIME type
        share.setType(type);

        // Create the URI from the media
        File media = new File(mediaPath);
        Uri uri = Uri.fromFile(media);

        // Add the URI and the caption to the Intent.
        share.putExtra(Intent.EXTRA_STREAM, uri);
        share.putExtra(Intent.EXTRA_TEXT, caption);

        share.setPackage("com.instagram.android");

        // Broadcast the Intent.
        context.startActivity(Intent.createChooser(share, "Share to"));
    }
}
