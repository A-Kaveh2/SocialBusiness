package ir.rasen.myapplication.helper;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.drive.query.internal.Operator;

import ir.rasen.myapplication.R;
import ir.rasen.myapplication.ui.EditTextFont;
import ir.rasen.myapplication.ui.TextViewFont;
import ir.rasen.myapplication.webservice.user.ForgetPassword;


public class ForgetPasswordDialog extends ParentDialog {
    Context context;
    EditTextFont editTextFont;

    TextViewFont tv_ok;
    TextViewFont tv_cancel;
    String callerType;


    public ForgetPasswordDialog(final Context context, String title) {
        super(context, title);

        this.context = context;

        int screenHeight = context.getResources().getDisplayMetrics().heightPixels;
        int screenWidth = context.getResources().getDisplayMetrics().widthPixels;

        int height = screenHeight / context.getResources().getInteger(R.integer.dialog_body_weight);
        int width = screenWidth / 20;

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        editTextFont = new EditTextFont(context);
        editTextFont.setId(123467);
        params.setMargins(20, 60, 20, 60);
        editTextFont.setLayoutParams(params);
        editTextFont.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.demo_dialog_txt));
        editTextFont.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL);
        editTextFont.setHint(context.getResources().getString(R.string.email));
        editTextFont.setHintTextColor(context.getResources().getColor(R.color.gray));

        LinearLayout ll = new LinearLayout(context);
        params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,(int) context.getResources().getDimension(R.dimen.dialog_button_hieght));

        params.addRule(RelativeLayout.BELOW, editTextFont.getId());

        ll.setLayoutParams(params);
        ll.setWeightSum(100);
        ll.setOrientation(LinearLayout.HORIZONTAL);

        tv_cancel = new TextViewFont(context);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
        params2.weight = (float) 49.75;
        params2.setMargins(2, 0, 0, 2);
        tv_cancel.setLayoutParams(params2);

        tv_cancel.setBackgroundResource(R.drawable.dialog_button_bg_left);
        tv_cancel.setText(context.getResources().getString(R.string.cancel));
        tv_cancel.setTextColor(Color.WHITE);
        tv_cancel.setGravity(Gravity.CENTER);
        tv_cancel.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.dialog_button));
        ll.addView(tv_cancel);

        TextView blank = new TextView(context);
        params2 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
        params2.weight = (float) 0.5;
        blank.setLayoutParams(params2);
        ll.addView(blank);

        tv_ok = new TextViewFont(context);
        params2 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
        params2.weight = (float) 49.75;
        params2.setMargins(0, 0, 2, 2);
        tv_ok.setLayoutParams(params2);

        tv_ok.setBackgroundResource(R.drawable.dialog_button_bg_right);
        tv_ok.setText(context.getResources().getString(R.string.ok));
        tv_ok.setTextColor(Color.WHITE);
        tv_ok.setGravity(Gravity.CENTER);
        tv_ok.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.dialog_button));
        ll.addView(tv_ok);


        RelativeLayout ll_body = getBody();
        ll_body.addView(editTextFont);
        ll_body.addView(ll);


        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
            }
        });


        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(editTextFont.getText().toString()).matches()) {
                    editTextFont.requestFocus();
                    editTextFont.setErrorC(context.getResources().getString(R.string.enter_valid_email));
                    return;
                }

                new ForgetPassword(LoginInfo.getUserId(context),editTextFont.getText().toString());
                dismiss();
            }
        });

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }


}
