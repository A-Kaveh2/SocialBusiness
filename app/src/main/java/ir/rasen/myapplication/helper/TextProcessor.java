package ir.rasen.myapplication.helper;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

import ir.rasen.myapplication.R;

/**
 * Created by 'Sina KH' on 01/11/2015.
 */
public class TextProcessor {

    private Context context;
    public TextProcessor(Context context) {
        this.context = context;
    }

    // processing comments for owner and profile tags
    public void process(final String text, TextView textView) {
        String TAG = "TextProcessor->processComment";

        int index_end = 0;
        Spannable wordtoSpan = new SpannableString(text);

/*        index_end = text.indexOf(":", 0);
        if (index_end == -1) {
            Log.e(TAG, "comment text without ':' char!");
        }
        final String commentOwner = text.substring(0, index_end).toString();
        wordtoSpan.setSpan(new ForegroundColorSpan(R.color.button_on_dark), 0, index_end+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                // username clicked!
                // TODO:: commentOwner is the profile id that should be passed
                InnerFragment innerFragment = new InnerFragment(context);
                innerFragment.newProfile(Params.ProfileType.PROFILE_USER, false);
            }
        };
        wordtoSpan.setSpan(clickableSpan, 0, index_end+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(wordtoSpan);
*/
        // TODO:: FIND AND LINK PROFILE TAGS!
        int index_temp = 0;
        while ((index_temp = text.indexOf("@", index_temp)) != -1 && index_temp<=text.length()-1-Params.USER_NAME_MIN_LENGTH) {

            // detect the place of supposed tag
            index_end=index_temp+Params.USER_NAME_MIN_LENGTH;
            while(index_end<=text.length()-1 && text.substring(index_temp+1, index_end).toString().matches(Params.USER_USERNAME_VALIDATION)) {
                index_end++;
            }
            index_temp++;
            index_end--;

            // if found tag is really acceptable
            if(text.substring(index_temp, index_end).toString().length()>=Params.USER_NAME_MIN_LENGTH && text.substring(index_temp, index_end).toString().matches(Params.USER_NAME_VALIDATION)) {

                final String profileId = text.substring(index_temp, index_end);
                wordtoSpan.setSpan(new ForegroundColorSpan(R.color.button_on_dark), index_temp-1, index_end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ClickableSpan clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View textView) {
                        // username clicked!
                        InnerFragment innerFragment = new InnerFragment(context);
                        innerFragment.newProfile(context,Params.ProfileType.PROFILE_USER, false, profileId);
                    }
                };
                wordtoSpan.setSpan(clickableSpan, index_temp-1, index_end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            }
        }

        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(wordtoSpan);

    }
}
