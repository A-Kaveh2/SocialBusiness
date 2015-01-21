package ir.rasen.myapplication.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import ir.rasen.myapplication.ActivityMain;
import ir.rasen.myapplication.R;
import ir.rasen.myapplication.classes.Business;
import ir.rasen.myapplication.classes.User;
import ir.rasen.myapplication.helper.InnerFragment;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.ui.ImageViewCircle;
import ir.rasen.myapplication.ui.TextViewFont;

/**
 * Created by 'Sina KH' on 01/13/2015.
 */
// TODO: FRIENDS LIST ADAPTER
public class BusinessesAdapter extends ArrayAdapter<Business> {
	private ArrayList<Business> mBusinesses;
	private LayoutInflater mInflater;

	public BusinessesAdapter(Context context, ArrayList<Business> businesses) {
		super(context, R.layout.layout_businesses_business, businesses);
		mBusinesses 	= businesses;
		mInflater	    = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup group) {
        final ViewHolder holder;
        Business business = mBusinesses.get(position);

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.layout_businesses_business, null);

            holder.picture = (ImageViewCircle) convertView.findViewById(R.id.img_businesses_business_image);
            holder.name = (TextViewFont) convertView.findViewById(R.id.txt_businesses_business_name);
            holder.item = (RelativeLayout) convertView.findViewById(R.id.rl_businesses_business);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (business != null) {
            holder.name.setText(business.name);
            // TODO:: SET PICTURE
            holder.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    InnerFragment innerFragment = new InnerFragment(getContext());
                    innerFragment.newProfile(Params.ProfileType.PROFILE_BUSINESS, true);
                    ((ActivityMain) getContext()).closeDrawer(Gravity.RIGHT);
                }
            });
        }

        return  convertView;
    }
    class ViewHolder {
        RelativeLayout item;
        ImageViewCircle picture;
        TextViewFont name;
        int id;
    }

}