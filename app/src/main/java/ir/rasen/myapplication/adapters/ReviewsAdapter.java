package ir.rasen.myapplication.adapters;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;

import java.util.ArrayList;

import ir.rasen.myapplication.R;
import ir.rasen.myapplication.classes.Review;
import ir.rasen.myapplication.helper.Functions;
import ir.rasen.myapplication.helper.InnerFragment;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.TextProcessor;
import ir.rasen.myapplication.ui.ImageViewCircle;
import ir.rasen.myapplication.ui.TextViewFont;

/**
 * Created by 'Sina KH'.
 */

// TODO: REVIEWS ADAPTER
public class ReviewsAdapter extends ArrayAdapter<Review> {
	private ArrayList<Review> mReviews;
	private LayoutInflater mInflater;
    int idOfView;
    private Context context;

	public ReviewsAdapter(Context context, ArrayList<Review> reviews) {
		super(context, R.layout.layout_reviews_review, reviews);
		mReviews 	= reviews;
		mInflater	= LayoutInflater.from(context);
        this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup group) {
        final ViewHolder holder;
        final Review review = mReviews.get(position);
        final String postId = review.id;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.layout_reviews_review, null);

            holder.profile_pic = (ImageViewCircle) convertView.findViewById(R.id.img_reviews_review_profile);
            holder.profile_name = (TextViewFont) convertView.findViewById(R.id.txt_reviews_review_profile);
            holder.review = (TextViewFont) convertView.findViewById(R.id.txt_reviews_review_text);
            holder.options = (ImageView) convertView.findViewById(R.id.btn_reviews_review_options);
            holder.ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBar_review);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (review != null) {
            holder.profile_name.setText(review.userID);

            holder.ratingBar.setRating(review.rate);

            TextProcessor textProcessor = new TextProcessor(getContext());
            textProcessor.process(review.text, holder.review);

            // TODO: CHECK IS MINE OR NOT
            //if(review.userID.equals(myId))
            boolean isMine=true;
            if(isMine) {
                holder.options.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                    // TODO:: SHOW OPTIONS POPUP
                    showOptionsPopup(view);
                    }
                });
                holder.options.setVisibility(View.VISIBLE);
            } else {
                holder.options.setVisibility(View.GONE);
            }

            holder.profile_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    InnerFragment innerFragment = new InnerFragment(getContext());
                    innerFragment.newProfile(context,Params.ProfileType.PROFILE_USER, false, review.userID);
                }
            });
        }

        return  convertView;
    }
    class ViewHolder {
        TextViewFont profile_name, review;
        ImageView options;
        ImageViewCircle profile_pic;
        RatingBar ratingBar;
        int id;
    }

    public void showOptionsPopup(View view) {
        // TODO review is mine because we've checked it before calling this method...
        // SHOWING POPUP WINDOW
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.layout_menu_post_options_owner,
                new LinearLayout(getContext()));
        PopupWindow pw = new PopupWindow(layout, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        pw.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pw.setOutsideTouchable(true);
        pw.showAsDropDown(view);
        // SETTING ON CLICK LISTENERS
        //if(isMine) {
            // EDIT OPTION
            ((LinearLayout) layout.findViewById(R.id.ll_menu_post_options_edit)).setVisibility(View.GONE);
            /*((LinearLayout) layout.findViewById(R.id.ll_menu_post_options_edit)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                // TODO: EDIT REVIEW (disable for now)
                }
            });*/
            // DELETE OPTION
            ((LinearLayout) layout.findViewById(R.id.ll_menu_post_options_delete)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // TODO DELETE REVIEW
                    Functions functions = new Functions();
                    functions.showReviewDeletePopup(getContext());
                }
            });
        //}
    }

}