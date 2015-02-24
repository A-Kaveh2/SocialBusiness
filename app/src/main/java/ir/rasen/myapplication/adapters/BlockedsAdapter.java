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
import ir.rasen.myapplication.webservice.WebserviceResponse;

/**
 * Created by 'Sina KH' on 01/11/2015.
 */
public class BlockedsAdapter extends ArrayAdapter<User> {
    private ArrayList<User> mFollowers;
    private LayoutInflater mInflater;
    private Context context;
    private DownloadImages downloadImages;
    private int businessId;
    private WebserviceResponse delegate;
    private EditInterface editDelegateInterface;

    public BlockedsAdapter(Context context, ArrayList<User> blockeds, int businessId, WebserviceResponse delegate
            , EditInterface editDelegateInterface) {
        super(context, R.layout.layout_businesses_business, blockeds);
        mFollowers = blockeds;
        mInflater = LayoutInflater.from(context);
        this.context = context;
        downloadImages = new DownloadImages(context);
        this.businessId = businessId;
        this.delegate = delegate;
        this.editDelegateInterface = editDelegateInterface;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup group) {
        final ViewHolder holder;
        final User blocked = mFollowers.get(position);

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.layout_blockeds_blocked, null);

            holder.blocked_profile_pic = (ImageViewCircle) convertView.findViewById(R.id.img_blockeds_blocked_image);
            holder.blocked_name = (TextViewFont) convertView.findViewById(R.id.txt_blockeds_blocked_name);
            holder.unblock = (ImageButton) convertView.findViewById(R.id.btn_blockeds_blocked_unblock);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (blocked != null) {
            downloadImages.download(blocked.profilePictureId, Image_M.getImageSize(Image_M.ImageSize.SMALL), holder.blocked_profile_pic);
            holder.blocked_name.setText(blocked.userName);
            // show followers profile
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    InnerFragment innerFragment = new InnerFragment(getContext());
                    innerFragment.newProfile(context, Params.ProfileType.PROFILE_USER, false, blocked.id);
                }
            });
            holder.unblock.setVisibility(View.VISIBLE);
            holder.unblock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editDelegateInterface.setEditing(blocked.id, null, null);
                    showUnblockPopup(blocked.id);
                }
            });
        }

        return convertView;
    }

    class ViewHolder {
        ImageViewCircle blocked_profile_pic;
        TextViewFont blocked_name;
        ImageButton unblock;
    }

    private void showUnblockPopup(int userId) {
        // SHOWING POPUP WINDOW
        Dialogs dialogs = new Dialogs();
        dialogs.showFollowerUnblockPopup(getContext(), businessId, userId, delegate, editDelegateInterface);
    }

}