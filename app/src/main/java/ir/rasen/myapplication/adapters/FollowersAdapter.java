package ir.rasen.myapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;

import java.util.ArrayList;

import ir.rasen.myapplication.R;
import ir.rasen.myapplication.classes.Business;
import ir.rasen.myapplication.classes.User;
import ir.rasen.myapplication.helper.Functions;
import ir.rasen.myapplication.helper.InnerFragment;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.ui.ImageViewCircle;
import ir.rasen.myapplication.ui.TextViewFont;

/**
 * Created by 'Sina KH' on 01/11/2015.
 */
// TODO: FRIENDS LIST ADAPTER
public class FollowersAdapter extends ArrayAdapter<User> {
	private ArrayList<User> mFollowers;
	private LayoutInflater mInflater;
    private boolean mOwnFollowers = false;

	public FollowersAdapter(Context context, ArrayList<User> followers, boolean ownFollowers) {
		super(context, R.layout.layout_businesses_business, followers);
		mFollowers	= followers;
		mInflater	= LayoutInflater.from(context);
        mOwnFollowers = ownFollowers;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup group) {
        final ViewHolder holder;
        final User follower = mFollowers.get(position);

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.layout_followers_follower, null);

            holder.friend_profile_pic = (ImageViewCircle) convertView.findViewById(R.id.img_followers_follower_image);
            holder.friend_name = (TextViewFont) convertView.findViewById(R.id.txt_followers_follower_name);
            holder.delete = (ImageButton) convertView.findViewById(R.id.btn_followers_follower_delete);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (follower != null) {
            holder.friend_name.setText(follower.name);
            // show followers profile
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    InnerFragment innerFragment = new InnerFragment(getContext());
                    innerFragment.newProfile(Params.ProfileType.PROFILE_USER, false, follower.userID);
                }
            });
            if(mOwnFollowers) {
                holder.delete.setVisibility(View.VISIBLE);
                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDeletePopup(follower.userID);
                    }
                });
            } else
                holder.delete.setVisibility(View.INVISIBLE);
        }

        return  convertView;
    }
    class ViewHolder {
        ImageViewCircle friend_profile_pic;
        TextViewFont friend_name;
        ImageButton delete;
    }

    private void showDeletePopup(String userId) {
        // SHOWING POPUP WINDOW
        Functions functions = new Functions();
        functions.showFollowerDeletePopup(getContext(), userId);
    }

}