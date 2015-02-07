package ir.rasen.myapplication.adapters;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;

import java.util.ArrayList;

import ir.rasen.myapplication.R;
import ir.rasen.myapplication.classes.Review;
import ir.rasen.myapplication.helper.EditInterface;
import ir.rasen.myapplication.helper.InnerFragment;
import ir.rasen.myapplication.helper.OptionsReview;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.TextProcessor;
import ir.rasen.myapplication.ui.ImageViewCircle;
import ir.rasen.myapplication.ui.TextViewFont;
import ir.rasen.myapplication.webservice.WebserviceResponse;

/**
 * Created by 'Sina KH'.
 */

// TODO: REVIEWS ADAPTER
public class ReviewsAdapter extends ArrayAdapter<Review> {
    private ArrayList<Review> mReviews;
    private LayoutInflater mInflater;
    int idOfView;
    private Context context;
    private WebserviceResponse delegate;
    private Dialog dialog;
    private EditInterface editDelegateInterface;

    public ReviewsAdapter(Context context, ArrayList<Review> reviews, WebserviceResponse delegate, EditInterface editDelegateInterface) {
        super(context, R.layout.layout_reviews_review, reviews);
        mReviews = reviews;
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.delegate = delegate;
        this.editDelegateInterface = editDelegateInterface;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup group) {
        final ViewHolder holder;
        final Review review = mReviews.get(position);
        final int postId = review.id;

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
            holder.profile_name.setText(review.userName);

            holder.ratingBar.setRating(review.rate);

            TextProcessor textProcessor = new TextProcessor(getContext());
            textProcessor.process(review.text, holder.review);

            // TODO: CHECK IS MINE OR NOT
            //if(review.userID.equals(myId))
            boolean isMine = true;
            if (isMine) {
                holder.options.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                    // TODO:: SHOW OPTIONS POPUP
                    OptionsReview optionsReview = new OptionsReview(context);
                    optionsReview.showOptionsPopup(mReviews.get(position),view,delegate, editDelegateInterface);
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
                    innerFragment.newProfile(context, Params.ProfileType.PROFILE_USER, false, review.userID);
                }
            });
        }

        return convertView;
    }

    class ViewHolder {
        TextViewFont profile_name, review;
        ImageView options;
        ImageViewCircle profile_pic;
        RatingBar ratingBar;
        int id;
    }

}