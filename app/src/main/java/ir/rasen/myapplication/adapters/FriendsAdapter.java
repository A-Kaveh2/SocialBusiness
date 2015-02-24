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
import ir.rasen.myapplication.helper.EditInterface;
import ir.rasen.myapplication.helper.Image_M;
import ir.rasen.myapplication.helper.InnerFragment;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.ui.ImageViewCircle;
import ir.rasen.myapplication.ui.TextViewFont;
import ir.rasen.myapplication.webservice.DownloadImages;

/**
 * Created by 'Sina KH' on 01/11/2015.
 */
// TODO: FRIENDS LIST ADAPTER
public class FriendsAdapter extends ArrayAdapter<User> {
	private ArrayList<User> mFriends;
	private LayoutInflater mInflater;
    private boolean mOwnFriends = false;
    private Context context;
    private EditInterface editDelegateInterface;
    private DownloadImages downloadImages;

	public FriendsAdapter(Context context, ArrayList<User> friends, boolean ownFriends, EditInterface editDelegateInterface) {
		super(context, R.layout.layout_friends_friend, friends);
		mFriends 	= friends;
		mInflater	= LayoutInflater.from(context);
        mOwnFriends = ownFriends;
        this.context = context;
        this.editDelegateInterface = editDelegateInterface;
        this.downloadImages = new DownloadImages(context);
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
            downloadImages.download(friend.profilePictureId, Image_M.getImageSize(Image_M.ImageSize.SMALL), holder.friend_profile_pic);
            holder.friend_name.setText(friend.userName);
            // show friends profile
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    InnerFragment innerFragment = new InnerFragment(getContext());
                    innerFragment.newProfile(context,Params.ProfileType.PROFILE_USER, false, friend.id);
                }
            });
            if(mOwnFriends) {
                holder.delete.setVisibility(View.VISIBLE);
                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDeletePopup(friend.id);
                        editDelegateInterface.setEditing(friend.id, null, null);
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

    private void showDeletePopup(int friendId) {
        // SHOWING POPUP WINDOW
        Dialogs dialogs = new Dialogs();
        dialogs.showFriendDeletePopup(getContext(), friendId);
    }

}