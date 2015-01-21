package ir.rasen.myapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

import ir.rasen.myapplication.R;
import ir.rasen.myapplication.classes.Review;
import ir.rasen.myapplication.helper.InnerFragment;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.TextProcessor;
import ir.rasen.myapplication.ui.ImageViewCircle;
import ir.rasen.myapplication.ui.TextViewFont;

/**
 * Created by 'Sina KH'.
 */

// TODO: REVIEWS ADAPTER
public class ProfileReviewsAdapter extends ArrayAdapter<Review> {
	private ArrayList<Review> mReviews;
	private LayoutInflater mInflater;
    int idOfView;

	public ProfileReviewsAdapter(Context context, ArrayList<Review> reviews) {
		super(context, R.layout.layout_post, reviews);
		mReviews 	= reviews;
		mInflater	= LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup group) {
        final ViewHolder holder;
        final Review review = mReviews.get(position);
        final String postId = review.id;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.layout_reviews_review, null);

            holder.business_pic = (ImageViewCircle) convertView.findViewById(R.id.img_reviews_review_profile);
            holder.business_name = (TextViewFont) convertView.findViewById(R.id.txt_reviews_review_text);
            holder.review = (TextViewFont) convertView.findViewById(R.id.txt_reviews_review_text);
            holder.options = (ImageView) convertView.findViewById(R.id.btn_reviews_review_options);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (review != null) {
            holder.business_name.setText(review.businessID);

            TextProcessor textProcessor = new TextProcessor(getContext());
            textProcessor.process("SALAM @haSAN jan!! in naghdo bebin!", holder.review);

            holder.options.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // TODO:: SHOW OPTIONS POPUP
                // showOptionsPopup(view);
            }
            });

            holder.business_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    InnerFragment innerFragment = new InnerFragment(getContext());
                    innerFragment.newProfile(Params.ProfileType.PROFILE_USER, false, review.userID);
                }
            });
        }

        return  convertView;
    }
    class ViewHolder {
        TextViewFont business_name, review;
        ImageView options;
        ImageViewCircle business_pic;
        int id;
    }
/*
    public void showOptionsPopup(View view) {
        // TODO: CHECK IS MINE
        Boolean isMine = true;
        // SHOWING POPUP WINDOW
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.layout_menu_post_options_owner,
                new LinearLayout(getContext()));
        PopupWindow pw = new PopupWindow(layout, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        pw.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pw.setOutsideTouchable(true);
        pw.showAsDropDown(view);
        // SETTING ON CLICK LISTENERS
        if(isMine) {
            // EDIT OPTION
            ((LinearLayout) layout.findViewById(R.id.ll_menu_post_options_edit)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                // TODO: EDIT POST
                }
            });
            // DELETE OPTION
            ((LinearLayout) layout.findViewById(R.id.ll_menu_post_options_delete)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                // TODO DELETE POST
                showDeletePopup();
                }
            });
        }
    }
*/
}