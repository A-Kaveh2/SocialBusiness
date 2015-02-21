package ir.rasen.myapplication.helper;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import java.util.ArrayList;

import ir.rasen.myapplication.ActivityMain;
import ir.rasen.myapplication.ActivityNewPost_Step1;
import ir.rasen.myapplication.R;
import ir.rasen.myapplication.classes.Post;
import ir.rasen.myapplication.ui.ProgressDialogCustom;
import ir.rasen.myapplication.webservice.WebserviceResponse;

public class OptionsPost {
    private Context context;
    private ProgressDialogCustom pd;

    public OptionsPost(Context context) {
        this.context = context;
    }

    public void showOptionsPopup(final Post post, View view, final WebserviceResponse webserviceResponse, final EditInterface editDelegateInterface, ProgressDialogCustom pd) {
        this.pd = pd;
        // SHOWING POPUP WINDOW
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        final View layout;
        // TODO :: TEST
        boolean isMine = true;
        //if (post.isMine(context)) {
        if (isMine) {
            layout = inflater.inflate(R.layout.layout_menu_post_options_owner,
                    new LinearLayout(context));
        } else {
            layout = inflater.inflate(R.layout.layout_menu_post_options,
                    new LinearLayout(context));
        }

        final PopupWindow pw = new PopupWindow(layout, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        pw.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pw.setOutsideTouchable(true);
        pw.showAsDropDown(view);
        // SETTING ON CLICK LISTENERS

        //if (post.isMine(context)) {
        if (isMine) {
            // EDIT OPTION
            (layout.findViewById(R.id.ll_menu_post_options_edit)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // TODO: EDIT POST
                    ArrayList<Post> posts = new ArrayList<Post>();
                    posts.add(post);
                    PassingPosts.getInstance().setValue(posts);
                    Intent intent = new Intent(context, ActivityNewPost_Step1.class);
                    context.startActivity(intent);
                    ((ActivityMain) context).overridePendingTransition(R.anim.to_0, R.anim.to_left);
                    pw.dismiss();
                }
            });
            // DELETE OPTION
            (layout.findViewById(R.id.ll_menu_post_options_delete)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // TODO DELETE POST
                    showDeletePopup(post, webserviceResponse);
                    editDelegateInterface.setEditing(post.id, null, null);
                    pw.dismiss();
                }
            });
        } else {
            // REPORT OPTION

            (layout.findViewById(R.id.ll_menu_post_options_report)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // TODO REPORT POST
                    showReportPopup(post, webserviceResponse);
                    pw.dismiss();
                }
            });

            // SHARE OPTION
            (layout.findViewById(R.id.ll_menu_post_options_share)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // TODO REPORT POST
                    showSharePopup(post, webserviceResponse);
                    pw.dismiss();
                }
            });
        }
    }

    public void showDeletePopup(Post post, WebserviceResponse webserviceResponse) {
        // SHOWING POPUP WINDOW
        Dialogs dialogs = new Dialogs();
        dialogs.showPostDeletePopup(context, post.businessID, post.id, webserviceResponse);
    }

    public void showReportPopup(Post post, WebserviceResponse webserviceResponse) {
        // SHOWING POPUP WINDOW
        Dialogs dialogs = new Dialogs();
        dialogs.showPostReportPopup(context, post.id, webserviceResponse, pd);
    }

    public void showSharePopup(Post post, WebserviceResponse webserviceResponse) {
        // SHOWING POPUP WINDOW
        Dialogs dialogs = new Dialogs();
        dialogs.showPostSharePopup(context, post.id, webserviceResponse);
    }

}
