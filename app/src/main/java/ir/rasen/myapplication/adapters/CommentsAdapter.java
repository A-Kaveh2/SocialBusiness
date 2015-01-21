package ir.rasen.myapplication.adapters;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import java.util.ArrayList;

import ir.rasen.myapplication.R;
import ir.rasen.myapplication.classes.Comment;
import ir.rasen.myapplication.helper.Functions;
import ir.rasen.myapplication.helper.TextProcessor;
import ir.rasen.myapplication.ui.ImageViewCircle;
import ir.rasen.myapplication.ui.TextViewFont;

/**
 * Created by 'Sina KH' on 01/11/2015.
 */
// TODO: COMMENTS ADAPTER
public class CommentsAdapter extends ArrayAdapter<Comment> {
	private ArrayList<Comment> mComments;
	private LayoutInflater mInflater;

	public CommentsAdapter(Context context, ArrayList<Comment> comments) {
		super(context, R.layout.layout_comments_comment, comments);
		mComments 	= comments;
		mInflater	= LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup group) {
        final ViewHolder holder;
        Comment comment = mComments.get(position);

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.layout_comments_comment, null);

            holder.comment_profile_pic = (ImageViewCircle) convertView.findViewById(R.id.img_comments_comment_image);
            holder.comment_comment = (TextViewFont) convertView.findViewById(R.id.txt_comments_comment_comment);
            holder.comment_options = (ImageView) convertView.findViewById(R.id.btn_comments_comment_options);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (comment != null) {
            TextProcessor textProcessor = new TextProcessor(getContext());
            textProcessor.processComment(comment.text, holder.comment_comment);
            // options
            holder.comment_options.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showOptionsPopup(view);
                }
            });
        }

        return  convertView;
    }
    class ViewHolder {
        ImageViewCircle comment_profile_pic;
        TextViewFont comment_comment;
        ImageView comment_options;
        int id;
    }

    public void showOptionsPopup(View view) {
        // TODO: CHECK IS MINE
        Boolean isMine = true;
        // SHOWING POPUP WINDOW
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
        View layout;
        if (isMine)
            layout = inflater.inflate(R.layout.layout_menu_comment_options_owner,new LinearLayout(getContext()));
        else
            layout = inflater.inflate(R.layout.layout_menu_comment_options,new LinearLayout(getContext()));
        final PopupWindow pw = new PopupWindow(layout, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        pw.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pw.setOutsideTouchable(true);
        pw.showAsDropDown(view);
        // SETTING ON CLICK LISTENERS
        if(isMine) {
            // EDIT OPTION
            ((LinearLayout) layout.findViewById(R.id.ll_menu_comment_options_edit)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // TODO: EDIT POST
                }
            });
            // DELETE OPTION
            ((LinearLayout) layout.findViewById(R.id.ll_menu_comment_options_delete)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // TODO DELETE POST
                    Functions functions = new Functions();
                    functions.showCommentDeletePopup(getContext());
                    pw.dismiss();
                }
            });
        }
    }

}