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

import java.util.ArrayList;
import java.util.regex.Pattern;

import ir.rasen.myapplication.R;
import ir.rasen.myapplication.ui.EditTextFont;

/**
 * Created by 'Sina KH' on 01/11/2015.
 */
public class TextProcessor {

    private Context context;
    public TextProcessor(Context context) {
        this.context = context;
    }

    public static String timeToTimeAgo (Context context, int time)
    {

        if(time<60)
            return context.getString(R.string.time_now) ;

        String result = "";

        int[] values = new int[] {
                31536000,
                2592000,
                604800,
                86400,
                3600,
                60
        };
        String[] results = new String[] {
                context.getString(R.string.time_year),
                context.getString(R.string.time_month),
                context.getString(R.string.time_week),
                context.getString(R.string.time_day),
                context.getString(R.string.time_hour),
                context.getString(R.string.time_minute)
        };

        for (int i=0; i<values.length; i++) {
            if (time >= values[i])
                return ((int)(time / values[i]))+" "+results[i]+" "+context.getString(R.string.time_ago);
        }

        return "";
    }

    public static ArrayList<String> getHashtags(String text) {
        String TAG = "TextProcessor->getHashtags";

        ArrayList<String> hashtags = new ArrayList<>();

        int index_end = 0;
        // TODO:: FIND AND LIST HASHTAGS!
        int index_temp = 0;

        index_temp = 0;
        while ((index_temp = text.indexOf("#", index_temp)) != -1 && index_temp<=text.length()-1-Params.HASHTAG_MIN_LENGTH) {

            // detect the place of supposed tag
            index_end=index_temp+Params.HASHTAG_MIN_LENGTH;
            while(index_end<=text.length()-1 && text.substring(index_temp+1, index_end).toString().matches(Params.TEXT_HASHTAG_VALIDATION)) {
                index_end++;
            }
            index_temp++;
            index_end--;

            // if found tag is really acceptable
            if(text.substring(index_temp, index_end).toString().length()>=Params.HASHTAG_MIN_LENGTH && text.substring(index_temp, index_end).toString().matches(Params.TEXT_HASHTAG_VALIDATION)) {

                hashtags.add(text.substring(index_temp, index_end));

            }
        }

        return hashtags;
    }

    // processing texts for tags and hashtags
    public void process(final String text, TextView textView) {
        String TAG = "TextProcessor->processText";

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

                //TODO change profileId to profileUserName
                final String profileId = text.substring(index_temp, index_end);
                wordtoSpan.setSpan(new ForegroundColorSpan(R.color.button_on_dark), index_temp-1, index_end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ClickableSpan clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View textView) {
                        // username clicked!
                        InnerFragment innerFragment = new InnerFragment(context);

                        //TODO change 1 to the profileId
                        innerFragment.newProfile(context,Params.ProfileType.PROFILE_USER, false, 1);
                    }
                };
                wordtoSpan.setSpan(clickableSpan, index_temp-1, index_end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            }
        }

        // TODO:: FIND AND LINK HASHTAGS!
        index_temp = 0;
        while ((index_temp = text.indexOf("#", index_temp)) != -1 && index_temp<=text.length()-1-Params.HASHTAG_MIN_LENGTH) {

            // detect the place of supposed tag
            index_end=index_temp+Params.HASHTAG_MIN_LENGTH;
            while(index_end<=text.length()-1 && text.substring(index_temp+1, index_end).toString().matches(Params.TEXT_HASHTAG_VALIDATION)) {
                index_end++;
            }
            index_temp++;
            index_end--;

            // if found tag is really acceptable
            if(text.substring(index_temp, index_end).toString().length()>=Params.HASHTAG_MIN_LENGTH && text.substring(index_temp, index_end).toString().matches(Params.TEXT_HASHTAG_VALIDATION)) {

                final String hashtag = text.substring(index_temp, index_end);
                wordtoSpan.setSpan(new ForegroundColorSpan(R.color.button_on_dark), index_temp-1, index_end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ClickableSpan clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View textView) {
                        // username clicked!
                        InnerFragment innerFragment = new InnerFragment(context);
                        innerFragment.newSearchFragment(hashtag);
                    }
                };
                wordtoSpan.setSpan(clickableSpan, index_temp-1, index_end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            }
        }

        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(wordtoSpan);
    }

    // processing editTexts for hashtags only
    public void processEdtHashtags(final String text, EditTextFont editTextFont) {
        String TAG = "TextProcessor->processEdtHashtags";

        int selection = editTextFont.getSelectionStart();

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
        /*int index_temp = 0;
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
        }*/

        // TODO:: FIND AND LINK HASHTAGS!
        int index_temp = 0;
        while ((index_temp = text.indexOf("#", index_temp)) != -1 && index_temp<=text.length()-1-Params.HASHTAG_MIN_LENGTH) {

            // detect the place of supposed tag
            index_end=index_temp+Params.HASHTAG_MIN_LENGTH;
            while(index_end<=text.length()-1 && text.substring(index_temp+1, index_end).toString().matches(Params.TEXT_HASHTAG_VALIDATION)) {
                index_end++;
            }
            index_temp++;
            index_end--;

            // if found tag is really acceptable
            if(text.substring(index_temp, index_end).toString().length()>=Params.HASHTAG_MIN_LENGTH && text.substring(index_temp, index_end).toString().matches(Params.TEXT_HASHTAG_VALIDATION)) {

                final String hashtag = text.substring(index_temp, index_end);
                wordtoSpan.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.button_on_dark)), index_temp-1, index_end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                /*ClickableSpan clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View textView) {
                        // username clicked!
                        InnerFragment innerFragment = new InnerFragment(context);
                        innerFragment.newSearchFragment(hashtag);
                    }
                };
                wordtoSpan.setSpan(clickableSpan, index_temp-1, index_end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);*/

            }
        }

        editTextFont.setMovementMethod(LinkMovementMethod.getInstance());
        editTextFont.setText(wordtoSpan);
        editTextFont.setSelection(selection);

    }

}
