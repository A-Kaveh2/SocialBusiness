package ir.rasen.myapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

import ir.rasen.myapplication.R;
import ir.rasen.myapplication.classes.Post;
import ir.rasen.myapplication.helper.Image_M;
import ir.rasen.myapplication.helper.InnerFragment;
import ir.rasen.myapplication.webservice.DownloadImages;

// TODO: POSTS GRID ADAPTER
public class ProfilePostsGridAdapter extends ArrayAdapter<Post> {
	private ArrayList<Post> mPosts;
	private LayoutInflater mInflater;
    private DownloadImages downloadImages;

	public ProfilePostsGridAdapter(Context context, ArrayList<Post> posts) {
		super(context, R.layout.layout_post, posts);
		mPosts 	= posts;
		mInflater	= LayoutInflater.from(context);
        downloadImages = new DownloadImages(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup group) {
        final ViewHolder holder;
        final Post post = mPosts.get(position);

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.layout_profile_grid_post, null);

            holder.postPic = (ImageView) convertView.findViewById(R.id.img_profile_grid_post_pic);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (post != null) {
            downloadImages.download(post.pictureId, Image_M.getImageSize(Image_M.ImageSize.MEDIUM), holder.postPic);
            holder.postPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    InnerFragment innerFragment = new InnerFragment(getContext());
                    innerFragment.newPostFragment(post);
                }
            });
        }

        return  convertView;
    }
    class ViewHolder {
        ImageView postPic;
        int id;
    }

}