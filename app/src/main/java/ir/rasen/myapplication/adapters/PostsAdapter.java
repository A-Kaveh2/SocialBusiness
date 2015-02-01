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

import java.util.ArrayList;
import java.util.Random;

import ir.rasen.myapplication.R;
import ir.rasen.myapplication.classes.Post;
import ir.rasen.myapplication.helper.EditInterface;
import ir.rasen.myapplication.helper.InnerFragment;
import ir.rasen.myapplication.helper.LoginInfo;
import ir.rasen.myapplication.helper.OptionsPost;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.TextProcessor;
import ir.rasen.myapplication.ui.ImageViewCircle;
import ir.rasen.myapplication.ui.TextViewFont;
import ir.rasen.myapplication.webservice.WebserviceResponse;
import ir.rasen.myapplication.webservice.post.Like;

/**
 * Created by 'Sina KH'.
 */

// TODO: POSTS LIST ADAPTER ( HOME & PROFILE LIST )
public class PostsAdapter extends ArrayAdapter<Post> {
	private ArrayList<Post> mPosts;
	private LayoutInflater mInflater;

    private boolean singleTapped;

    private WebserviceResponse delegate;
    private EditInterface editDelegateInterface;
    private Context context;

	public PostsAdapter(Context context, ArrayList<Post> posts,WebserviceResponse delegate, EditInterface editDelegateInterface) {
		super(context, R.layout.layout_post, posts);
		mPosts 	= posts;
		mInflater	= LayoutInflater.from(context);
        this.delegate = delegate;
        this.context = context;
        this.editDelegateInterface = editDelegateInterface;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup group) {
        final ViewHolder holder;
        final Post post = mPosts.get(position);

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
            holder.comment1_pic = (ImageViewCircle) convertView.findViewById(R.id.img_home_post_comment_1);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(position==1) {
            holder.postPic.setImageResource(R.drawable.test2);
            holder.business_pic.setImageResource(R.drawable.test_ic2);
        }
        if (post != null && holder != null) {
            holder.business_name.setText(post.businessID);
            holder.description.setText(Html.fromHtml("<font color=#3F6F94>"+ getContext().getString(R.string.product_price) +":</font> " + post.price
                    + "<br /><font color=#3F6F94><b>"+ getContext().getString(R.string.product_code) +"</font> " + post.code
                    + "<br /><font color=#3F6F94>" + getContext().getString(R.string.product_description) + "</font>" + post.description));

            holder.time.setText(TextProcessor.timeToTimeAgo(getContext(), new Random().nextInt(100000)));

            holder.comment1_user.setText(post.lastThreeComments.get(0).userID);
            holder.comment1_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO:: OPEN COMMENT's USER's PROFILE
                    InnerFragment innerFragment = new InnerFragment(getContext());
                    innerFragment.newProfile(context,Params.ProfileType.PROFILE_BUSINESS, false, post.lastThreeComments.get(0).userID);
                }
            });
            TextProcessor textProcessor = new TextProcessor(getContext());
            textProcessor.process(post.lastThreeComments.get(0).text, holder.comment1);

            holder.options.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                OptionsPost optionsPost = new OptionsPost(context);
                optionsPost.showOptionsPopup(mPosts.get(position), view, delegate, editDelegateInterface);
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
                        singleTapped=true;
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                singleTapped=false;
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
                    holder.likeHeart.setImageResource(R.drawable.ic_menu_liked);
                    likeNow(mPosts.get(position).id);
                }
            });

            // SHOW COMMENTS LISTENER
            holder.comments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO: SHOW COMMENTS
                    InnerFragment innerFragment = new InnerFragment(getContext());
                    innerFragment.newComments(post.id);
                }
            });

            // ON CLICK LISTENERS FOR business pic and name
            holder.business_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    InnerFragment innerFragment = new InnerFragment(getContext());
                    innerFragment.newProfile(context,Params.ProfileType.PROFILE_BUSINESS, false, post.businessID);
                }
            });
            holder.business_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    InnerFragment innerFragment = new InnerFragment(getContext());
                    innerFragment.newProfile(context,Params.ProfileType.PROFILE_BUSINESS, false, post.businessID);
                }
            });

            holder.comment1_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    InnerFragment innerFragment = new InnerFragment(getContext());
                    innerFragment.newProfile(context,Params.ProfileType.PROFILE_USER, false, post.lastThreeComments.get(0).userID);
                }
            });
            holder.comment1_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    InnerFragment innerFragment = new InnerFragment(getContext());
                    innerFragment.newProfile(context,Params.ProfileType.PROFILE_USER, false, post.lastThreeComments.get(0).userID);
                }
            });

            // TODO:: Check if liked
            // IF LIKED ::
            //holder.likeHeart.setImageResource(R.drawable.ic_menu_liked);
            //else
            //holder.likeHeart.setImageResource(R.drawable.ic_menu_like);
        }

        return  convertView;
    }
    class ViewHolder {
        TextViewFont business_name, description, time, comment1, comment1_user;
        ImageView options, postPic, likeHeart;
        ImageViewCircle business_pic, comment1_pic;
        LinearLayout likes, comments;
        int id;
    }

    void likeNow(int post_id) {
       new Like(LoginInfo.getUserId(context),post_id,delegate).execute();
    }

}