package ir.rasen.myapplication.helper;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import ir.rasen.myapplication.R;
import ir.rasen.myapplication.ui.TextViewFont;


/**
 * Created by kh.bakhtiari on 9/24/2014.
 */
public class ParentDialog implements DialogInterface.OnClickListener {
    Context context;
    Dialog dialog;

    private View view;
    private RelativeLayout ll_body;
    TextViewFont tv_title;


    public ParentDialog(Context context, String title) {
        this.context = context;

        try {
            view = LayoutInflater.from(context).inflate(
                    R.layout.dialog, null);
        }
        catch (Exception e){
            String s = e.getMessage();
        }

        LinearLayout ll_dialog = (LinearLayout) view.findViewById(R.id.ll_dialog);
        LinearLayout ll_header = (LinearLayout) view.findViewById(R.id.ll_dialog_header);


        ll_body = (RelativeLayout) view.findViewById(R.id.rl_dialog_body);

        int screenHeight = context.getResources().getDisplayMetrics().heightPixels;


        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ll_dialog.getLayoutParams();
        layoutParams.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        ll_dialog.setLayoutParams(layoutParams);

        Resources resources = context.getResources();

        LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) ll_header.getLayoutParams();
        layoutParams2.height = screenHeight / resources.getInteger(R.integer.dialog_header_weight);
        layoutParams2.width = LinearLayout.LayoutParams.MATCH_PARENT;
        layoutParams2.setMargins(5,5,5,2);
        ll_header.setLayoutParams(layoutParams2);




        layoutParams2 = (LinearLayout.LayoutParams) ll_body.getLayoutParams();
        layoutParams2.height =  LinearLayout.LayoutParams.WRAP_CONTENT;
        layoutParams2.width = LinearLayout.LayoutParams.MATCH_PARENT;
        layoutParams2.setMargins(5,2,5,5);
        ll_body.setLayoutParams(layoutParams2);

        tv_title = (TextViewFont) view.findViewById(R.id.tv_dialog_title);
        tv_title.setText(title);
    }


    public RelativeLayout getBody(){
        return this.ll_body;
    }

    public int getBodyWidth()
    {
        return this.ll_body.getWidth();
    }
    public void dismiss(){
        dialog.dismiss();
    }
    public void show(){
        /*dialog = new AlertDialog.Builder(context,R.style.DialogCustomTheme)
                .setView(view)
                .create();*/

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        //dialog.show();
    }
    @Override
    public void onClick(DialogInterface dialog, int which) {

    }
}
