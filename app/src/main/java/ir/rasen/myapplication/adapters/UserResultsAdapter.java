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
import ir.rasen.myapplication.webservice.DownloadImages;

/**
 * Created by 'Sina KH' on 01/11/2015.
 */
// TODO: FRIENDS LIST ADAPTER
public class UserResultsAdapter extends ArrayAdapter<User> {
	private ArrayList<User> mFriends;
	private LayoutInflater mInflater;
    private Context context;
    private DownloadImages downloadImages;

	public UserResultsAdapter(Context context, ArrayList<User> friends) {
		super(context, R.layout.layout_friends_friend, friends);
		mFriends 	= friends;
		mInflater	= LayoutInflater.from(context);
        this.context = context;
        this.downloadImages = new DownloadImages(context);
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
            downloadImages.download(friend.profilePictureId, 3, holder.user_profile_pic);
            holder.user_name.setText(friend.userName);
            // show friends profile
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    InnerFragment innerFragment = new InnerFragment(getContext());
                    innerFragment.newProfile(context,Params.ProfileType.PROFILE_USER, false, friend.id);
                }
            });
        }

        return  convertView;
    }
    class ViewHolder {
        ImageViewCircle user_profile_pic;
        TextViewFont user_name;
    }

    private void showDeletePopup(int friendId) {
        // SHOWING POPUP WINDOW
        Dialogs dialogs = new Dialogs();
        dialogs.showFriendDeletePopup(getContext(), friendId);
    }

}