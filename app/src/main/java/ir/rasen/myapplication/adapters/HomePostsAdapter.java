package ir.rasen.myapplication.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import ir.rasen.myapplication.ActivityMain;
import ir.rasen.myapplication.ActivityNewPost_Step1;
import ir.rasen.myapplication.R;
import ir.rasen.myapplication.classes.Post;
import ir.rasen.myapplication.helper.Functions;
import ir.rasen.myapplication.helper.InnerFragment;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.PassingPosts;
import ir.rasen.myapplication.helper.TextProcessor;
import ir.rasen.myapplication.ui.ImageViewCircle;
import ir.rasen.myapplication.ui.TextViewFont;
import ir.rasen.myapplication.webservice.WebserviceResponse;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by 'Sina KH'.
 */

// TODO: POSTS LIST ADAPTER ( HOME & PROFILE LIST )
public class HomePostsAdapter extends ArrayAdapter<Post> implements StickyListHeadersAdapter {
	private ArrayList<Post> mPosts;
	private LayoutInflater mInflater;

    private boolean singleTapped;
    private WebserviceResponse webserviceResponse;
    private Context context;

    private Post post;

	public HomePostsAdapter(Context context, ArrayList<Post> posts,WebserviceResponse webserviceResponse) {
		super(context, R.layout.layout_post_home, posts);
		mPosts 	= posts;
        this.webserviceResponse = webserviceResponse;
		mInflater	= LayoutInflater.from(context);
        this.context = context;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup group) {
        final ViewHolder holder;
        post = mPosts.get(position);
        final String postId = post.id;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.layout_post_home, null);

            holder.postPic = (ImageView) convertView.findViewById(R.id.img_home_post_pic);
            holder.description = (TextViewFont) convertView.findViewById(R.id.txt_home_post_description);
            holder.comment1 = (TextViewFont) convertView.findViewById(R.id.txt_home_post_comment_1_comment);
            holder.comment1_user = (TextViewFont) convertView.findViewById(R.id.txt_home_post_comment_1_user);
            holder.comment1_pic = (ImageViewCircle) convertView.findViewById(R.id.img_home_post_comment_1);
            holder.comments = (LinearLayout) convertView.findViewById(R.id.ll_home_post_comments);
            holder.likes = (LinearLayout) convertView.findViewById(R.id.ll_home_post_likes);
            holder.likeHeart = (ImageView) convertView.findViewById(R.id.img_like_heart);
            holder.options = (ImageButton) convertView.findViewById(R.id.btn_home_post_options);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(position==1) {
            holder.postPic.setImageResource(R.drawable.test2);
        }
        if (post != null) {
            holder.description.setText(Html.fromHtml(post.title+"<br />"
                    +"<font color=#3F6F94>"+ getContext().getString(R.string.product_price) +":</font> " + post.price
                    + "<br /><font color=#3F6F94>"+ getContext().getString(R.string.product_code) +"</font> " + post.code
                    + "<br /><font color=#3F6F94>" + getContext().getString(R.string.product_description) + "</font>" + post.description));

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

            // SHOW COMMENTS LISTENER
            holder.comments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO: SHOW COMMENTS
                    InnerFragment innerFragment = new InnerFragment(getContext());
                    String userId="1";
                    innerFragment.newComments(userId, postId);
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

            holder.options.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    showOptionsPopup(view, position,webserviceResponse);
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
        TextViewFont description, comment1, comment1_user;
        ImageView postPic, likeHeart;
        ImageViewCircle comment1_pic;
        ImageButton options;
        LinearLayout likes, comments;
        int id;
    }

    public void showOptionsPopup(View view, final int position, final WebserviceResponse webserviceResponse) {
        // TODO: CHECK IS MINE
        Boolean isMine = true;
        // SHOWING POPUP WINDOW
        final LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.layout_menu_post_options_owner,
                new LinearLayout(getContext()));
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
                    Intent intent = new Intent(getContext(), ActivityNewPost_Step1.class);
                    getContext().startActivity(intent);
                    ((ActivityMain) getContext()).overridePendingTransition(R.anim.to_0, R.anim.to_left);
                    pw.dismiss();
                }
            });
            // DELETE OPTION
            ((LinearLayout) layout.findViewById(R.id.ll_menu_post_options_delete)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                // TODO DELETE POST
                showDeletePopup(webserviceResponse);

                    pw.dismiss();
                }
            });
        }
    }

    public void showDeletePopup(WebserviceResponse webserviceResponse) {
        //TODO where is business.id and post.id?
            // TODO ANSWER:: post.businessID and post.id prepared for you
        // SHOWING POPUP WINDOW
        Functions functions = new Functions();

        //"1": post.businessID
        //"4": post.id
        functions.showPostDeletePopup(getContext(),"1","5",webserviceResponse);

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

        holder.business_name.setText(post.businessID);

        // TODO: لطفأ ورودی زمان و کامنت را اصلاح و سایر مقادیر را وارد کنید
        // FOR TEST ONLY!
        holder.time.setText(Functions.timeToTimeAgo(getContext(), new Random().nextInt(100000)));

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