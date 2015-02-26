package ir.rasen.myapplication.adapters;

import android.content.Context;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Random;

import ir.rasen.myapplication.ActivityMain;
import ir.rasen.myapplication.FragmentHome;
import ir.rasen.myapplication.FragmentProfile;
import ir.rasen.myapplication.R;
import ir.rasen.myapplication.classes.Post;
import ir.rasen.myapplication.helper.EditInterface;
import ir.rasen.myapplication.helper.Image_M;
import ir.rasen.myapplication.helper.InnerFragment;
import ir.rasen.myapplication.helper.LoginInfo;
import ir.rasen.myapplication.helper.OptionsPost;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.TextProcessor;
import ir.rasen.myapplication.ui.ImageViewCircle;
import ir.rasen.myapplication.ui.ProgressDialogCustom;
import ir.rasen.myapplication.ui.TextViewFont;
import ir.rasen.myapplication.webservice.DownloadImages;
import ir.rasen.myapplication.webservice.WebserviceResponse;
import ir.rasen.myapplication.webservice.comment.SendComment;
import ir.rasen.myapplication.webservice.post.Like;
import ir.rasen.myapplication.webservice.post.Report;
import ir.rasen.myapplication.webservice.post.Unlike;

/**
 * Created by 'Sina KH'.
 */

// POSTS LIST ADAPTER ( PROFILE LIST )
public class PostsAdapter extends ArrayAdapter<Post> {
    private ArrayList<Post> mPosts;
    private LayoutInflater mInflater;

    private boolean singleTapped;

    private WebserviceResponse delegate;
    private EditInterface editDelegateInterface;
    private Context context;
    private DownloadImages downloadImages;

    private int position;
    private Post post;
    private ProgressDialogCustom pd;

    public PostsAdapter(Context context, ArrayList<Post> posts, WebserviceResponse delegate, EditInterface editDelegateInterface, ProgressDialogCustom pd) {
        super(context, R.layout.layout_post, posts);
        mPosts = posts;
        mInflater = LayoutInflater.from(context);
        this.delegate = delegate;
        this.context = context;
        this.editDelegateInterface = editDelegateInterface;
        this.downloadImages = new DownloadImages(context);
        this.pd = pd;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup group) {
        final ViewHolder holder;
        final Post post = mPosts.get(position);
        this.position = position;
        this.post = mPosts.get(position);

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.layout_post, null);

            holder.business_pic = (ImageViewCircle) convertView.findViewById(R.id.img_home_post_business_pic);
            holder.postPic = (ImageView) convertView.findViewById(R.id.img_home_post_pic);
            holder.business_name = (TextViewFont) convertView.findViewById(R.id.txt_home_post_business_name);
            holder.description = (TextViewFont) convertView.findViewById(R.id.txt_home_post_description);
            holder.time = (TextViewFont) convertView.findViewById(R.id.txt_home_post_time);
            holder.comment1 = (TextViewFont) convertView.findViewById(R.id.txt_home_post_comment_1_comment);
            holder.comment1_user = (TextViewFont) convertView.findViewById(R.id.txt_home_post_comment_1_user);
            holder.options = (ImageView) convertView.findViewById(R.id.btn_home_post_options);
            holder.likes = (LinearLayout) convertView.findViewById(R.id.ll_home_post_likes);
            holder.likeHeart = (ImageView) convertView.findViewById(R.id.img_like_heart);
            holder.comments = (LinearLayout) convertView.findViewById(R.id.ll_home_post_comments);
            holder.comments_3 = (RelativeLayout) convertView.findViewById(R.id.rl_home_comments);
            holder.comment1_pic = (ImageViewCircle) convertView.findViewById(R.id.img_home_post_comment_1);
            holder.likesNum = (TextViewFont) convertView.findViewById(R.id.txt_home_post_likes);
            holder.commentsNum = (TextViewFont) convertView.findViewById(R.id.txt_home_post_comments);
            holder.sharesNum = (TextViewFont) convertView.findViewById(R.id.txt_home_post_shares);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.options.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //TODO here: position is wrong. e.g. clicking option menu on first item of the mPosts: position = 1 while it must be 0
                OptionsPost optionsPost = new OptionsPost(context);
                optionsPost.showOptionsPopup(mPosts.get(position), view, delegate, editDelegateInterface, pd);
            }
        });

        // double tap listener !
        holder.postPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (singleTapped) {
                    holder.likeHeart.setImageResource(R.drawable.ic_menu_liked);
                    likeNow(mPosts.get(position).id);
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
        holder.likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // like now!
                if(post.isLiked) {
                    holder.likeHeart.setImageResource(R.drawable.ic_menu_like);
                    unlikeNow(mPosts.get(position).id);
                } else {
                    holder.likeHeart.setImageResource(R.drawable.ic_menu_liked);
                    likeNow(mPosts.get(position).id);
                }
            }
        });

        // SHOW COMMENTS LISTENER
        holder.comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // SHOW COMMENTS
                InnerFragment innerFragment = new InnerFragment(getContext());
                innerFragment.newComments(post.id);
            }
        });

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

        if (post != null && holder != null) {
            // Check if liked
            if(post.isLiked)
                holder.likeHeart.setImageResource(R.drawable.ic_menu_liked);
            else
                holder.likeHeart.setImageResource(R.drawable.ic_menu_like);
            downloadImages.download(post.pictureId, Image_M.getImageSize(Image_M.ImageSize.LARGE), holder.postPic);
            downloadImages.download(post.businessProfilePictureId, Image_M.getImageSize(Image_M.ImageSize.SMALL), holder.business_pic);
            if(post.lastThreeComments!=null && post.lastThreeComments.size()>0)
                downloadImages.download(post.lastThreeComments.get(0).userProfilePictureID, Image_M.getImageSize(Image_M.ImageSize.SMALL), holder.comment1_pic);
            holder.business_name.setText(post.businessUserName);
            holder.description.setText(Html.fromHtml("<font color=#3F6F94>" + getContext().getString(R.string.product_price) + ":</font> " + post.price
                    + "<br /><font color=#3F6F94><b>" + getContext().getString(R.string.product_code) + "</font> " + post.code
                    + "<br /><font color=#3F6F94>" + getContext().getString(R.string.product_description) + "</font>" + post.description));
            holder.likesNum.setText(post.likeNumber+"");
            holder.commentsNum.setText(post.commentNumber+"");
            holder.sharesNum.setText(post.shareNumber+"");

            holder.time.setText(TextProcessor.timeToTimeAgo(getContext(), post.creationDate));

        }

        return convertView;
    }

    class ViewHolder {
        TextViewFont business_name, description, time, comment1, comment1_user, likesNum, commentsNum, sharesNum;
        ImageView options, postPic, likeHeart;
        ImageViewCircle business_pic, comment1_pic;
        LinearLayout likes, comments;
        RelativeLayout comments_3;
        int id;
    }

    void likeNow(int post_id) {
        new Like(LoginInfo.getUserId(context), post_id, delegate).execute();
        //new Report(LoginInfo.getUserId(context),post_id,delegate).execute();
        FragmentProfile fragment = (FragmentProfile) ((ActivityMain) context).getSupportFragmentManager().findFragmentByTag(((ActivityMain) context).pager.getCurrentItem() + "." + ((ActivityMain) context).fragCount[((ActivityMain) context).pager.getCurrentItem()]);
        if(fragment.uPosts.size()>0) {
            fragment.uPosts.get(position).isLiked = true;
        } else {
            fragment.bPosts.get(position).isLiked=false;
        }
    }
    void unlikeNow(int post_id) {
        new Unlike(LoginInfo.getUserId(context), post_id, delegate).execute();
        //new Report(LoginInfo.getUserId(context),post_id,delegate).execute();
        FragmentProfile fragment = (FragmentProfile) ((ActivityMain) context).getSupportFragmentManager().findFragmentByTag(((ActivityMain) context).pager.getCurrentItem() + "." + ((ActivityMain) context).fragCount[((ActivityMain) context).pager.getCurrentItem()]);
        if(fragment.uPosts.size()>0) {
            fragment.uPosts.get(position).isLiked = true;
        } else {
            fragment.bPosts.get(position).isLiked=false;
        }
    }

}