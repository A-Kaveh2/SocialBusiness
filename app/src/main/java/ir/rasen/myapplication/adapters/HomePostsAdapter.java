package ir.rasen.myapplication.adapters;

import android.content.Context;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;

import com.google.android.gms.internal.is;

import java.util.ArrayList;
import java.util.Random;

import ir.rasen.myapplication.R;
import ir.rasen.myapplication.classes.Post;
import ir.rasen.myapplication.helper.EditInterface;
import ir.rasen.myapplication.helper.InnerFragment;
import ir.rasen.myapplication.helper.OptionsPost;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.TextProcessor;
import ir.rasen.myapplication.ui.ImageViewCircle;
import ir.rasen.myapplication.ui.ProgressDialogCustom;
import ir.rasen.myapplication.ui.TextViewFont;
import ir.rasen.myapplication.webservice.DownloadImages;
import ir.rasen.myapplication.webservice.WebserviceResponse;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by 'Sina KH'.
 */

public class HomePostsAdapter extends ArrayAdapter<Post> implements StickyListHeadersAdapter {
	private ArrayList<Post> mPosts;
	private LayoutInflater mInflater;

    private boolean singleTapped;
    private WebserviceResponse webserviceResponse;
    private EditInterface editDelegateInterface;
    private Context context;
    private DownloadImages downloadImages;

    private Post post;
    private ProgressDialogCustom pd;


	public HomePostsAdapter(Context context, ArrayList<Post> posts,WebserviceResponse webserviceResponse, EditInterface editDelegateInterface, ProgressDialogCustom pd) {
		super(context, R.layout.layout_post_home, posts);
		mPosts 	= posts;

        this.webserviceResponse = webserviceResponse;
		mInflater	= LayoutInflater.from(context);
        this.context = context;
        this.editDelegateInterface = editDelegateInterface;
        this.downloadImages = new DownloadImages(context);
        this.pd = pd;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup group) {
        final ViewHolder holder;
        post = mPosts.get(position);
        final int postId = post.id;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.layout_post_home, null);

            holder.ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBar_home_post_review);
            holder.businessPic = (ImageViewCircle) convertView.findViewById(R.id.img_home_post_business_pic);
            holder.postPic = (ImageView) convertView.findViewById(R.id.img_home_post_pic);
            holder.description = (TextViewFont) convertView.findViewById(R.id.txt_home_post_description);
            holder.comment1 = (TextViewFont) convertView.findViewById(R.id.txt_home_post_comment_1_comment);
            holder.comment1_user = (TextViewFont) convertView.findViewById(R.id.txt_home_post_comment_1_user);
            holder.comment1_pic = (ImageViewCircle) convertView.findViewById(R.id.img_home_post_comment_1);
            holder.comments = (LinearLayout) convertView.findViewById(R.id.ll_home_post_comments);
            holder.comments_3 = (RelativeLayout) convertView.findViewById(R.id.rl_home_comments);
            holder.likes = (LinearLayout) convertView.findViewById(R.id.ll_home_post_likes);
            holder.likeHeart = (ImageView) convertView.findViewById(R.id.img_like_heart);
            holder.options = (ImageButton) convertView.findViewById(R.id.btn_home_post_options);
            holder.shares = (LinearLayout) convertView.findViewById(R.id.ll_home_post_shares);
            holder.likesNum = (TextViewFont) convertView.findViewById(R.id.txt_home_post_likes);
            holder.commentsNum = (TextViewFont) convertView.findViewById(R.id.txt_home_post_comments);
            holder.sharesNum = (TextViewFont) convertView.findViewById(R.id.txt_home_post_shares);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (post != null) {

            if(post.type==Post.Type.Complete) {

                holder.postPic.setVisibility(View.VISIBLE);
                holder.likes.setVisibility(View.VISIBLE);
                holder.comments.setVisibility(View.VISIBLE);
                holder.comment1_pic.setVisibility(View.VISIBLE);
                holder.options.setVisibility(View.VISIBLE);
                holder.shares.setVisibility(View.VISIBLE);
                holder.comments_3.setVisibility(View.VISIBLE);
                holder.description.setVisibility(View.VISIBLE);

                downloadImages.download(post.pictureId, 1, holder.postPic);
                if (post.lastThreeComments != null && post.lastThreeComments.size() > 0)
                    downloadImages.download(post.lastThreeComments.get(0).userProfilePictureID, 3, holder.comment1_pic);
                holder.description.setText(Html.fromHtml(post.title + "<br />"
                        + "<font color=#3F6F94>" + getContext().getString(R.string.product_price) + ":</font> " + post.price
                        + "<br /><font color=#3F6F94>" + getContext().getString(R.string.product_code) + "</font> " + post.code
                        + "<br /><font color=#3F6F94>" + getContext().getString(R.string.product_description) + "</font>" + post.description));

                if (post.lastThreeComments.size() > 0) {
                    holder.comment1_user.setText(post.lastThreeComments.get(0).username);
                    holder.comment1_user.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            InnerFragment innerFragment = new InnerFragment(getContext());
                            innerFragment.newProfile(context, Params.ProfileType.PROFILE_BUSINESS, false, post.lastThreeComments.get(0).userID);
                        }
                    });
                    TextProcessor textProcessor = new TextProcessor(getContext());
                    textProcessor.process(post.lastThreeComments.get(0).text, holder.comment1);
                } else {
                    holder.comments_3.setVisibility(View.GONE);
                }

                // SHOW COMMENTS LISTENER
                holder.comments.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        InnerFragment innerFragment = new InnerFragment(getContext());
                        innerFragment.newComments(postId);
                    }
                });

                holder.comment1_pic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        InnerFragment innerFragment = new InnerFragment(getContext());
                        innerFragment.newProfile(context, Params.ProfileType.PROFILE_USER, false, post.lastThreeComments.get(0).userID);
                    }
                });
                holder.comment1_user.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        InnerFragment innerFragment = new InnerFragment(getContext());
                        innerFragment.newProfile(context, Params.ProfileType.PROFILE_USER, false, post.lastThreeComments.get(0).userID);
                    }
                });

                // double tap listener !
                holder.postPic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (singleTapped) {
                            holder.likeHeart.setImageResource(R.drawable.ic_menu_liked);
                            likeNow();
                        } else {
                            singleTapped = true;
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    singleTapped = false;
                                }
                            }, 250);
                        }
                    }
                });
                // LIKE!!
                //holder.likesNum.setText
                //holder.
                holder.likes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // like now!
                        holder.likeHeart.setImageResource(R.drawable.ic_menu_liked);
                        likeNow();
                    }
                });

                holder.options.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        OptionsPost optionsPost = new OptionsPost(getContext());
                        optionsPost.showOptionsPopup(mPosts.get(position), view, webserviceResponse, editDelegateInterface, pd);
                    }
                });

                // Check if liked
                if(post.isLiked)
                holder.likeHeart.setImageResource(R.drawable.ic_menu_liked);
                else
                holder.likeHeart.setImageResource(R.drawable.ic_menu_like);
            } else if(post.type==Post.Type.Review) {
                holder.postPic.setVisibility(View.GONE);
                holder.likes.setVisibility(View.GONE);
                holder.comments.setVisibility(View.GONE);
                holder.comment1_pic.setVisibility(View.GONE);
                holder.options.setVisibility(View.GONE);
                holder.shares.setVisibility(View.GONE);
                holder.comments_3.setVisibility(View.GONE);
                TextProcessor textProcessor = new TextProcessor(getContext());
                textProcessor.process(post.reviewText, holder.description);
                holder.description.setText(post.reviewText);
                holder.ratingBar.setRating(post.reviewRate);
                holder.ratingBar.setVisibility(View.VISIBLE);
            } else if(post.type==Post.Type.Follow) {
                holder.postPic.setVisibility(View.GONE);
                holder.likes.setVisibility(View.GONE);
                holder.comments.setVisibility(View.GONE);
                holder.comment1_pic.setVisibility(View.GONE);
                holder.options.setVisibility(View.GONE);
                holder.shares.setVisibility(View.GONE);
                holder.comments_3.setVisibility(View.GONE);
                holder.description.setVisibility(View.GONE);
            }
        }

        return  convertView;
    }
    class ViewHolder {
        TextViewFont description, comment1, comment1_user, likesNum, commentsNum, sharesNum;
        ImageView postPic, likeHeart;
        ImageViewCircle comment1_pic, businessPic;
        ImageButton options;
        LinearLayout likes, comments, shares;
        RelativeLayout comments_3;
        RatingBar ratingBar;
        int id;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;
        final Post post = mPosts.get(position);
        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = mInflater.inflate(R.layout.layout_post_header, parent, false);
            holder.business_pic = (ImageViewCircle) convertView.findViewById(R.id.img_home_post_business_pic);
            holder.business_name = (TextViewFont) convertView.findViewById(R.id.txt_home_post_business_name);
            holder.time = (TextViewFont) convertView.findViewById(R.id.txt_home_post_time);

            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }

        if(post!=null){
            holder.business_name.setTextSize(context.getResources().getDimension(R.dimen.font_medium));
            if(post.type== Post.Type.Complete) {
                holder.business_name.setText(post.businessUserName);
                downloadImages.download(post.businessProfilePictureId, 3, holder.business_pic);

                holder.time.setText(TextProcessor.timeToTimeAgo(getContext(), post.creationDate));


                // ON CLICK LISTENERS FOR business pic and name
                holder.business_pic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        InnerFragment innerFragment = new InnerFragment(getContext());
                        innerFragment.newProfile(context, Params.ProfileType.PROFILE_BUSINESS, false, post.businessID);
                    }
                });
                holder.business_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        InnerFragment innerFragment = new InnerFragment(getContext());
                        innerFragment.newProfile(context, Params.ProfileType.PROFILE_BUSINESS, false, post.businessID);
                    }
                });
            } else if(post.type== Post.Type.Review) {
                TextProcessor textProcessor = new TextProcessor(context);
                textProcessor.processTitle(post.userName, post.businessUserName, context.getString(R.string.reviewed), holder.business_name);
                holder.business_name.setText(post.userName);
            } else if(post.type==Post.Type.Follow) {
                TextProcessor textProcessor = new TextProcessor(getContext());
                textProcessor.processTitle(post.userName, post.businessUserName, context.getString(R.string.followed), holder.business_name);
                holder.business_name.setTextSize(context.getResources().getDimension(R.dimen.font_small));
            }
        }

        return convertView;
    }
    @Override
    public long getHeaderId(int position) {
        //return the first character of the country as ID because this is what headers are based upon
        return position;
    }

    class HeaderViewHolder {
        TextViewFont business_name, time;
        ImageViewCircle business_pic;
    }

    void likeNow() {
        // TODO:: LIKE NOW
    }

}