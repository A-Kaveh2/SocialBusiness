package ir.rasen.myapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;

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
public class FriendsAdapter extends ArrayAdapter<User> {
	private ArrayList<User> mFriends;
	private LayoutInflater mInflater;
    private boolean mOwnFriends = false;
    private Context context;

	public FriendsAdapter(Context context, ArrayList<User> friends, boolean ownFriends) {
		super(context, R.layout.layout_friends_friend, friends);
		mFriends 	= friends;
		mInflater	= LayoutInflater.from(context);
        mOwnFriends = ownFriends;
        this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup group) {
        final ViewHolder holder;
        final User friend = mFriends.get(position);

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.layout_friends_friend, null);

            holder.friend_profile_pic = (ImageViewCircle) convertView.findViewById(R.id.img_friends_friend_image);
            holder.friend_name = (TextViewFont) convertView.findViewById(R.id.txt_friends_friend_name);
            holder.delete = (ImageButton) convertView.findViewById(R.id.btn_friends_friend_delete);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (friend != null) {
            holder.friend_name.setText(friend.name);
            // show friends profile
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    InnerFragment innerFragment = new InnerFragment(getContext());
                    innerFragment.newProfile(context,Params.ProfileType.PROFILE_USER, false, friend.userID);
                }
            });
            if(mOwnFriends) {
                holder.delete.setVisibility(View.VISIBLE);
                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDeletePopup(friend.userID);
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

    private void showDeletePopup(String friendId) {
        // SHOWING POPUP WINDOW
        Dialogs dialogs = new Dialogs();
        dialogs.showFriendDeletePopup(getContext(), friendId);
    }

}