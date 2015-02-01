package ir.rasen.myapplication.helper;


import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;

import ir.rasen.myapplication.R;
import ir.rasen.myapplication.classes.Review;
import ir.rasen.myapplication.ui.EditTextFont;
import ir.rasen.myapplication.ui.TextViewFont;
import ir.rasen.myapplication.webservice.WebserviceResponse;

public class OptionsReview {
    private Context context;
    private Dialog dialog;

    public OptionsReview(Context context) {
        this.context = context;
    }


    public void showOptionsPopup(final Review review, View view, final WebserviceResponse webserviceResponse, final EditInterface editDelegateInterface) {
        // TODO review is mine because we've checked it before calling this method...
        // SHOWING POPUP WINDOW
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.layout_menu_post_options_owner,
                new LinearLayout(context));
        PopupWindow pw = new PopupWindow(layout, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        pw.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pw.setOutsideTouchable(true);
        pw.showAsDropDown(view);
        // SETTING ON CLICK LISTENERS

        boolean isMine = true;
        if(isMine) {
            //if(mReviews.get(position).userID.equals(MyID)) {

            // EDIT OPTION
            ((LinearLayout) layout.findViewById(R.id.ll_menu_post_options_edit)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // TODO: EDIT REVIEW
                    showReviewEditDialog(review, editDelegateInterface);
                }
            });
            // DELETE OPTION
            ((LinearLayout) layout.findViewById(R.id.ll_menu_post_options_delete)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // TODO DELETE REVIEW
                    Dialogs dialogs = new Dialogs();
                    dialogs.showReviewDeletePopup(context, LoginInfo.getUserId(context), review.id, webserviceResponse);
                    editDelegateInterface.setEditing(review.id, null, null);
                }
            });
        }
    }

    void showReviewEditDialog(final Review review, final EditInterface editDelegateInterface) {
        dialog = new Dialog(context, R.style.AppTheme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_new_review);
        ((RatingBar) dialog.findViewById(R.id.ratingBar_new_review_rate)).setRating(review.rate);
        ((TextViewFont) dialog.findViewById(R.id.edt_new_review_review)).setText(review.text);
        ((RatingBar) dialog.findViewById(R.id.ratingBar_new_review_rate)).setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                ((RatingBar) dialog.findViewById(R.id.ratingBar_new_review_rate)).setRating((float) Math.ceil(v));
            }
        });
        dialog.findViewById(R.id.btn_new_review_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String reviewText = ((EditTextFont) dialog.findViewById(R.id.edt_new_review_review)).getText().toString();
                float rate = ((RatingBar) dialog.findViewById(R.id.ratingBar_new_review_rate)).getRating();
                if(rate>0) {
                    editDelegateInterface.setEditing(review.id, reviewText, dialog);
                    editReview(review.businessID, reviewText, rate);
                } else {
                    Dialogs.showMessage(context, context.getString(R.string.rate_needed));
                }
            }
        });
        dialog.findViewById(R.id.btn_new_review_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    void editReview(int businessId, String review, float rate) {
        // TODO:: EDIT REVIEW NOW

    }
}
