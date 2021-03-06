package ir.rasen.myapplication.adapters;

import android.app.Dialog;
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
import ir.rasen.myapplication.helper.EditInterface;
import ir.rasen.myapplication.helper.Image_M;
import ir.rasen.myapplication.helper.InnerFragment;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.TextProcessor;
import ir.rasen.myapplication.ui.ImageViewCircle;
import ir.rasen.myapplication.ui.ProgressDialogCustom;
import ir.rasen.myapplication.ui.TextViewFont;
import ir.rasen.myapplication.webservice.DownloadImages;
import ir.rasen.myapplication.webservice.WebserviceResponse;

/**
 * Created by 'Sina KH' on 01/11/2015.
 */
// TODO: COMMENTS ADAPTER
public class CommentsAdapter extends ArrayAdapter<Comment> {
	private ArrayList<Comment> mComments;
	private LayoutInflater mInflater;
    private Context context;
    private EditInterface editDelegateInterface;
    private DownloadImages downloadImages;
    private ProgressDialogCustom pd;
    private WebserviceResponse delegate;

	public CommentsAdapter(Context context, ArrayList<Comment> comments, WebserviceResponse delegate, EditInterface editDelegateInterface) {
		super(context, R.layout.layout_comments_comment, comments);
		mComments 	= comments;
		mInflater	= LayoutInflater.from(context);
        this.context = context;
        this.delegate = delegate;
        this.editDelegateInterface = editDelegateInterface;
        this.downloadImages = new DownloadImages(context);
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
            downloadImages.download(comment.userProfilePictureID, Image_M.getImageSize(Image_M.ImageSize.SMALL), holder.comment_profile_pic);
            holder.comment_user.setText(comment.username);
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
                    editDelegateInterface.setEditing(comment.id, comment.text, null);
                    Dialogs dialogs = new Dialogs();
                    Dialog dialog = dialogs.showCommentEditPopup(getContext(), comment, delegate, editDelegateInterface);
                    pw.dismiss();
                }
            });
            // DELETE OPTION
            ((LinearLayout) layout.findViewById(R.id.ll_menu_comment_options_delete)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // DELETE COMMENT
                    Dialogs dialogs = new Dialogs();
                    editDelegateInterface.setEditing(comment.id, null, null);
                    dialogs.showCommentDeletePopup(getContext(), comment.businessID, comment.id, delegate, editDelegateInterface);
                    pw.dismiss();
                }
            });
        } else if(isMyBusiness) {
            // BLOCK OPTION
            ((LinearLayout) layout.findViewById(R.id.ll_menu_comment_options_block)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // TODO DELETE POST
                    Dialogs dialogs = new Dialogs();
                    dialogs.showFollowerBlockPopup(getContext(), comment.businessID, comment.userID, delegate, editDelegateInterface);
                    editDelegateInterface.setEditing(comment.id, comment.text, null);
                    pw.dismiss();
                }
            });
            // DELETE OPTION
            ((LinearLayout) layout.findViewById(R.id.ll_menu_comment_options_delete)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // TODO DELETE POST
                    Dialogs dialogs = new Dialogs();
                    dialogs.showCommentDeleteFromMyBusinessPopup(getContext(), comment.businessID, comment.id, delegate, pd);
                    editDelegateInterface.setEditing(comment.id, comment.text, null);
                    pw.dismiss();
                }
            });
        }
    }

}