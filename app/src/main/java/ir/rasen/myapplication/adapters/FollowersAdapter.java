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
import ir.rasen.myapplication.helper.InnerFragment;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.ui.ImageViewCircle;
import ir.rasen.myapplication.ui.TextViewFont;
import ir.rasen.myapplication.webservice.DownloadImages;
import ir.rasen.myapplication.webservice.WebserviceResponse;

/**
 * Created by 'Sina KH' on 01/11/2015.
 */
// TODO: FRIENDS LIST ADAPTER
public class FollowersAdapter extends ArrayAdapter<User> {
	private ArrayList<User> mFollowers;
	private LayoutInflater mInflater;
    private boolean mOwnFollowers = false;
    private Context context;
    private EditInterface editDelegateInterface;
    private DownloadImages downloadImages;
    private WebserviceResponse delegate;
    private int businessId;

	public FollowersAdapter(Context context, ArrayList<User> followers, boolean ownFollowers,
                            int businessId, WebserviceResponse delegate, EditInterface editDelegateInterface) {
		super(context, R.layout.layout_businesses_business, followers);
		mFollowers	= followers;
		mInflater	= LayoutInflater.from(context);
        mOwnFollowers = ownFollowers;
        this.context = context;
        this.delegate = delegate;
        this.editDelegateInterface = editDelegateInterface;
        this.businessId = businessId;
        downloadImages =  new DownloadImages(context);
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
            holder.block = (ImageButton) convertView.findViewById(R.id.btn_followers_follower_block);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        if (follower != null) {
            holder.friend_name.setText(follower.userName);
            // show followers profile
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    InnerFragment innerFragment = new InnerFragment(getContext());
                    innerFragment.newProfile(context,Params.ProfileType.PROFILE_USER, false, follower.id);
                }
            });
            downloadImages.download(follower.profilePictureId,3,holder.friend_profile_pic);

            if(mOwnFollowers) {
                holder.block.setVisibility(View.VISIBLE);
                holder.block.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    editDelegateInterface.setEditing(follower.id, null, null);
                    showBlockPopup(follower.id);
                    }
                });
            } else
                holder.block.setVisibility(View.INVISIBLE);
        }

        return  convertView;
    }
    class ViewHolder {
        ImageViewCircle friend_profile_pic;
        TextViewFont friend_name;
        ImageButton block;
    }

    private void showBlockPopup(int userId) {
        // SHOWING POPUP WINDOW
        Dialogs dialogs = new Dialogs();
        dialogs.showFollowerBlockPopup(getContext(),businessId, userId, delegate, editDelegateInterface);
    }

}