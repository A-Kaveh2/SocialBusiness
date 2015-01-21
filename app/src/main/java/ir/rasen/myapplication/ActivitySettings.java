package ir.rasen.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.CheckBox;

import ir.rasen.myapplication.ui.ButtonFont;

/**
 * Created by 'Sina KH'.
 */
public class ActivitySettings extends Activity {

    CheckBox cbFriends, cbBusinesses, cbReviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_settings);

        cbFriends = (CheckBox) findViewById(R.id.cb_settings_friends);
        cbBusinesses = (CheckBox) findViewById(R.id.cb_settings_businesses);
        cbReviews = (CheckBox) findViewById(R.id.cb_settings_reviews);

        // SET ANIMATIONS
        setAnimations();
    }

    // SWITCH FRIENDS
    public void switch_friends(View view) {
        if(cbFriends.isChecked())
            cbFriends.setChecked(false);
        else
            cbFriends.setChecked(true);
    }
    // SWITCH BUSINESSES
    public void switch_businesses(View view) {
        if(cbBusinesses.isChecked())
            cbBusinesses.setChecked(false);
        else
            cbBusinesses.setChecked(true);
    }
    // SWITCH REVIEWS
    public void switch_reviews(View view) {
        if(cbReviews.isChecked())
            cbReviews.setChecked(false);
        else
            cbReviews.setChecked(true);
    }

    // SAVE TOUCHED
    public void save(View view) {
        // TODO SAVE NOW!
        // .....
    }

    public void setAnimations() {
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in_normal);
        AnimationSet animationSetBtn = new AnimationSet(getBaseContext(), null);
        animationSetBtn.addAnimation(fadeIn);
        Animation anim_fromDown = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.2f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        anim_fromDown.setDuration(500);
        anim_fromDown.setInterpolator(new AccelerateInterpolator());
        animationSetBtn.addAnimation(anim_fromDown);
        ((ButtonFont) findViewById(R.id.btn_settings_save)).startAnimation(animationSetBtn);
    }
    public void onBackPressed() {
		finish();
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
    public void back(View view) {
        onBackPressed();
    }

}
