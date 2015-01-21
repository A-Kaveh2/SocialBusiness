package ir.rasen.myapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.ArrayList;

import ir.rasen.myapplication.R;
import ir.rasen.myapplication.classes.Review;
import ir.rasen.myapplication.helper.InnerFragment;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.SearchItemUserBusiness;
import ir.rasen.myapplication.helper.TextProcessor;
import ir.rasen.myapplication.ui.ImageViewCircle;
import ir.rasen.myapplication.ui.TextViewFont;

/**
 * Created by 'Sina KH' on '01/21/2015'.
 */

// TODO: REQUESTS ADAPTER
public class RequestsAdapter extends ArrayAdapter<SearchItemUserBusiness> {
	private ArrayList<SearchItemUserBusiness> mRequests;
	private LayoutInflater mInflater;

	public RequestsAdapter(Context context, ArrayList<SearchItemUserBusiness> requests) {
		super(context, R.layout.layout_requests_request, requests);
		mRequests 	= requests;
		mInflater	= LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup group) {
        final ViewHolder holder;
        final SearchItemUserBusiness request = mRequests.get(position);

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.layout_requests_request, null);

            holder.username = (TextViewFont) convertView.findViewById(R.id.txt_requests_request_username);
            holder.name = (TextViewFont) convertView.findViewById(R.id.txt_requests_request_name);
            holder.pic = (ImageViewCircle) convertView.findViewById(R.id.img_requests_request_image);
            holder.accept = (ImageButton) convertView.findViewById(R.id.btn_requests_request_accept);
            holder.reject = (ImageButton) convertView.findViewById(R.id.btn_requests_request_reject);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (request != null) {
            holder.username.setText(request.userID);
            holder.name.setText(request.name);

            holder.accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO:: ACCEPT NOW!!
                }
            });

            holder.reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO:: REJECT NOW!!
                }
            });
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // show request sender's profile
                    InnerFragment innerFragment = new InnerFragment(getContext());
                    innerFragment.newProfile(Params.ProfileType.PROFILE_USER, false, request.userID);
                }
            });
        }

        return  convertView;
    }
    class ViewHolder {
        TextViewFont username, name;
        ImageViewCircle pic;
        ImageButton accept, reject;
        int id;
    }
/*
    public void showOptionsPopup(View view) {
        // TODO: CHECK IS MINE
        Boolean isMine = true;
        // SHOWING POPUP WINDOW
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.layout_menu_post_options_owner,
                new LinearLayout(getContext()));
        PopupWindow pw = new PopupWindow(layout, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        pw.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pw.setOutsideTouchable(true);
        pw.showAsDropDown(view);
        // SETTING ON CLICK LISTENERS
        if(isMine) {
            // EDIT OPTION
            ((LinearLayout) layout.findViewById(R.id.ll_menu_post_options_edit)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                // TODO: EDIT POST
                }
            });
            // DELETE OPTION
            ((LinearLayout) layout.findViewById(R.id.ll_menu_post_options_delete)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                // TODO DELETE POST
                showDeletePopup();
                }
            });
        }
    }
*/
}