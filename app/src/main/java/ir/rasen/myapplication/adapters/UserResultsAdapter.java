package ir.rasen.myapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import ir.rasen.myapplication.R;
import ir.rasen.myapplication.classes.User;
import ir.rasen.myapplication.helper.Dialogs;
import ir.rasen.myapplication.helper.InnerFragment;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.ui.ImageViewCircle;
import ir.rasen.myapplication.ui.TextViewFont;

/**
 * Created by 'Sina KH' on 01/11/2015.
 */
// TODO: FRIENDS LIST ADAPTER
public class UserResultsAdapter extends ArrayAdapter<User> {
	private ArrayList<User> mFriends;
	private LayoutInflater mInflater;
    private Context context;

	public UserResultsAdapter(Context context, ArrayList<User> friends, boolean ownFriends) {
		super(context, R.layout.layout_friends_friend, friends);
		mFriends 	= friends;
		mInflater	= LayoutInflater.from(context);
        this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup group) {
        final ViewHolder holder;
        final User friend = mFriends.get(position);

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.layout_friends_friend, null);

            holder.user_profile_pic = (ImageViewCircle) convertView.findViewById(R.id.img_friends_friend_image);
            holder.user_name = (TextViewFont) convertView.findViewById(R.id.txt_friends_friend_name);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (friend != null) {
            holder.user_name.setText(friend.name);
            // show friends profile
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    InnerFragment innerFragment = new InnerFragment(getContext());
                    innerFragment.newProfile(context,Params.ProfileType.PROFILE_USER, false, friend.userID);
                }
            });
        }

        return  convertView;
    }
    class ViewHolder {
        ImageViewCircle user_profile_pic;
        TextViewFont user_name;
    }

    private void showDeletePopup(String friendId) {
        // SHOWING POPUP WINDOW
        Dialogs dialogs = new Dialogs();
        dialogs.showFriendDeletePopup(getContext(), friendId);
    }

}