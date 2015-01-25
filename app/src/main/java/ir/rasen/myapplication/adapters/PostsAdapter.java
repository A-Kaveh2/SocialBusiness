package ir.rasen.myapplication.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Random;

import ir.rasen.myapplication.ActivityMain;
import ir.rasen.myapplication.ActivityNewPost;
import ir.rasen.myapplication.R;
import ir.rasen.myapplication.helper.Functions;
import ir.rasen.myapplication.classes.Post;
import ir.rasen.myapplication.helper.InnerFragment;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.PassingPosts;
import ir.rasen.myapplication.helper.TextProcessor;
import ir.rasen.myapplication.ui.ImageViewCircle;
import ir.rasen.myapplication.ui.TextViewFont;
import ir.rasen.myapplication.webservice.WebserviceResponse;

/**
 * Created by 'Sina KH'.
 */

// TODO: POSTS LIST ADAPTER ( HOME & PROFILE LIST )
public class PostsAdapter extends ArrayAdapter<Post> {
	private ArrayList<Post> mPosts;
	private LayoutInflater mInflater;

    private boolean singleTapped;

    private WebserviceResponse webserviceResponse;

	public PostsAdapter(Context context, ArrayList<Post> posts,WebserviceResponse webserviceResponse) {
		super(context, R.layout.layout_post, posts);
		mPosts 	= posts;
		mInflater	= LayoutInflater.from(context);
        this.webserviceResponse = webserviceResponse;
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

            holder.time.setText(Functions.timeToTimeAgo(getContext(), new Random().nextInt(100000)));

            holder.comment1_user.setText(post.lastThreeComments.get(0).userID);
            holder.comment1_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO:: OPEN COMMENT's USER's PROFILE
                    InnerFragment innerFragment = new InnerFragment(getContext());
                    innerFragment.newProfile(Params.ProfileType.PROFILE_BUSINESS, false, post.lastThreeComments.get(0).userID);
                }
            });
            TextProcessor textProcessor = new TextProcessor(getContext());
            textProcessor.process(post.lastThreeComments.get(0).text, holder.comment1);

            holder.options.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                showOptionsPopup(view, position);
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
                    likeNow();
                }
            });

            // SHOW COMMENTS LISTENER
            holder.comments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO: SHOW COMMENTS
                    InnerFragment innerFragment = new InnerFragment(getContext());
                    innerFragment.newComments(post.businessID, post.id);
                }
            });

            // ON CLICK LISTENERS FOR business pic and name
            holder.business_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    InnerFragment innerFragment = new InnerFragment(getContext());
                    innerFragment.newProfile(Params.ProfileType.PROFILE_BUSINESS, false, post.businessID);
                }
            });
            holder.business_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    InnerFragment innerFragment = new InnerFragment(getContext());
                    innerFragment.newProfile(Params.ProfileType.PROFILE_BUSINESS, false, post.businessID);
                }
            });

            holder.comment1_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    InnerFragment innerFragment = new InnerFragment(getContext());
                    innerFragment.newProfile(Params.ProfileType.PROFILE_USER, false, post.lastThreeComments.get(0).userID);
                }
            });
            holder.comment1_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    InnerFragment innerFragment = new InnerFragment(getContext());
                    innerFragment.newProfile(Params.ProfileType.PROFILE_USER, false, post.lastThreeComments.get(0).userID);
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

    public void showOptionsPopup(View view, final int position) {
        // TODO: CHECK IS MINE
        Boolean isMine = true;
        // SHOWING POPUP WINDOW
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
        View layout;
        if (isMine)
            layout = inflater.inflate(R.layout.layout_menu_post_options_owner,new LinearLayout(getContext()));
        else
            layout = inflater.inflate(R.layout.layout_menu_post_options,new LinearLayout(getContext()));
        final PopupWindow pw = new PopupWindow(layout, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        pw.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pw.setOutsideTouchable(true);
        pw.showAsDropDown(view);
        // SETTING ON CLICK LISTENERS
        if(isMine) {
            // EDIT OPTION
            ((LinearLayout) layout.findViewById(R.id.ll_menu_post_options_edit)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                // TODO: EDIT POST
                ArrayList<Post> posts = new ArrayList<Post>();
                posts.add(mPosts.get(position));
                PassingPosts.getInstance().setValue(posts);
                Intent intent = new Intent(getContext(), ActivityNewPost.class);
                getContext().startActivity(intent);
                ((ActivityMain) getContext()).overridePendingTransition(R.anim.to_0, R.anim.to_left);
                pw.dismiss();
                }
            });
            // DELETE OPTION
            ((LinearLayout) layout.findViewById(R.id.ll_menu_post_options_delete)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                // TODO where is business.id and post.id
                Functions functions = new Functions();
                functions.showPostDeletePopup(getContext(),"1","3",webserviceResponse);
                pw.dismiss();
                }
            });
        }
    }

    void likeNow() {
        // TODO:: LIKE NOW
    }

}