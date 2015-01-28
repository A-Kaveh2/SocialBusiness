package ir.rasen.myapplication.adapters;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
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
import ir.rasen.myapplication.helper.Dialogs;
import ir.rasen.myapplication.helper.InnerFragment;
import ir.rasen.myapplication.helper.Params;
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
    private Context context;

	public CommentsAdapter(Context context, ArrayList<Comment> comments) {
		super(context, R.layout.layout_comments_comment, comments);
		mComments 	= comments;
		mInflater	= LayoutInflater.from(context);
        this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup group) {
        final ViewHolder holder;
        final Comment comment = mComments.get(position);

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.layout_comments_comment, null);

            holder.comment_profile_pic = (ImageViewCircle) convertView.findViewById(R.id.img_comments_comment_image);
            holder.comment_user = (TextViewFont) convertView.findViewById(R.id.txt_comments_comment_user);
            holder.comment_comment = (TextViewFont) convertView.findViewById(R.id.txt_comments_comment_comment);
            holder.comment_options = (ImageView) convertView.findViewById(R.id.btn_comments_comment_options);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (comment != null) {
            holder.comment_user.setText(comment.userID);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO:: navigate to comment's user's profile...
                    InnerFragment innerFragment = new InnerFragment(getContext());
                    innerFragment.newProfile(context,Params.ProfileType.PROFILE_USER, false, comment.userID);
                }
            });
            TextProcessor textProcessor = new TextProcessor(getContext());
            textProcessor.process(comment.text, holder.comment_comment);
            // options
            holder.comment_options.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showOptionsPopup(view, comment);
                }
            });
        }

        return  convertView;
    }
    class ViewHolder {
        ImageViewCircle comment_profile_pic;
        TextViewFont comment_comment, comment_user;
        ImageView comment_options;
        int id;
    }

    public void showOptionsPopup(View view, final Comment comment) {
        // TODO: CHECK COMMENT IS MINE OR NOT
        Boolean isMine = true;
        Boolean isMyBusiness = true; // TODO: CHECK THE BUSINESS IS MINE OR NOT
        // SHOWING POPUP WINDOW
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
        View layout;
        if (isMine)
            layout = inflater.inflate(R.layout.layout_menu_comment_review_options_owner,new LinearLayout(getContext()));
        else if (isMyBusiness)
            layout = inflater.inflate(R.layout.layout_menu_comment_options_business_owner,new LinearLayout(getContext()));
        else return;
        final PopupWindow pw = new PopupWindow(layout, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        pw.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pw.setOutsideTouchable(true);
        pw.showAsDropDown(view);
        // SETTING ON CLICK LISTENERS
        if(isMine) {
            // EDIT OPTION
            ((LinearLayout) layout.findViewById(R.id.ll_menu_comment_options_edit)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // TODO: EDIT COMMENT
                    Dialogs dialogs = new Dialogs();
                    dialogs.showCommentEditPopup(getContext(), comment);
                    pw.dismiss();
                }
            });
            // DELETE OPTION
            ((LinearLayout) layout.findViewById(R.id.ll_menu_comment_options_delete)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // TODO DELETE COMMENT
                    Dialogs dialogs = new Dialogs();
                    dialogs.showCommentDeletePopup(getContext());
                    pw.dismiss();
                }
            });
        } else if(isMyBusiness) {
            // DELETE OPTION
            ((LinearLayout) layout.findViewById(R.id.ll_menu_comment_options_delete)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // TODO DELETE POST
                    Dialogs dialogs = new Dialogs();
                    dialogs.showCommentDeleteFromMyBusinessPopup(getContext());
                    pw.dismiss();
                }
            });
        }
    }

}