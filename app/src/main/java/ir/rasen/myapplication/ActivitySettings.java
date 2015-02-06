package ir.rasen.myapplication;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.CheckBox;

import ir.rasen.myapplication.helper.Dialogs;
import ir.rasen.myapplication.helper.LoginInfo;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.Permission;
import ir.rasen.myapplication.helper.ResultStatus;
import ir.rasen.myapplication.helper.ServerAnswer;
import ir.rasen.myapplication.ui.ButtonFont;
import ir.rasen.myapplication.webservice.WebserviceResponse;
import ir.rasen.myapplication.webservice.user.Login;
import ir.rasen.myapplication.webservice.user.UpdateSetting;

/**
 * Created by 'Sina KH'.
 */
public class ActivitySettings extends Activity implements WebserviceResponse {
    private String TAG = "ActivitySettings";

    CheckBox cbFriends, cbBusinesses, cbReviews;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_settings);
        context = this;
        cbFriends = (CheckBox) findViewById(R.id.cb_settings_friends);
        cbBusinesses = (CheckBox) findViewById(R.id.cb_settings_businesses);
        cbReviews = (CheckBox) findViewById(R.id.cb_settings_reviews);

        // SET ANIMATIONS
        setAnimations();

        cbFriends.setChecked(getIntent().getBooleanExtra(Params.PERMISSION_FRIENDS, false));
        cbReviews.setChecked(getIntent().getBooleanExtra(Params.PERMISSION_REVIEWS, false));
        cbBusinesses.setChecked(getIntent().getBooleanExtra(Params.PERMISSION_FOLLOWED_BUSINESSES, false));
    }

    // SWITCH FRIENDS
    public void switch_friends(View view) {
        if (cbFriends.isChecked())
            cbFriends.setChecked(false);
        else
            cbFriends.setChecked(true);
    }

    // SWITCH BUSINESSES
    public void switch_businesses(View view) {
        if (cbBusinesses.isChecked())
            cbBusinesses.setChecked(false);
        else
            cbBusinesses.setChecked(true);
    }

    // SWITCH REVIEWS
    public void switch_reviews(View view) {
        if (cbReviews.isChecked())
            cbReviews.setChecked(false);
        else
            cbReviews.setChecked(true);
    }

    // SAVE TOUCHED
    public void save(View view) {
        Permission permission = new Permission();
        permission.followedBusiness = cbBusinesses.isChecked();
        permission.friends = cbFriends.isChecked();
        permission.reviews = cbReviews.isChecked();

        //TODO remove test part
        //new UpdateSetting(LoginInfo.getUserId(ActivitySettings.this), permission, ActivitySettings.this).execute();

        //TODO for the test
        new UpdateSetting(LoginInfo.getUserId(this), permission, ActivitySettings.this).execute();
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
        overridePendingTransition(R.anim.to_0_from_left, R.anim.to_right);
    }

    public void back(View view) {
        onBackPressed();
    }

    @Override
    public void getResult(Object result) {
        try {
            if(result instanceof ResultStatus) {
                Dialogs.showMessage(ActivitySettings.this, getString(R.string.settings_saved));
            }
        } catch (Exception e) {
            Log.e(TAG, Params.CLOSED_BEFORE_RESPONSE);
        }
    }

    @Override
    public void getError(Integer errorCode) {
        try {
            String errorMessage = ServerAnswer.getError(getBaseContext(), errorCode);
            Dialogs.showMessage(context, errorMessage);
        } catch(Exception e) {
            Log.e(TAG, Params.CLOSED_BEFORE_RESPONSE);
        }
    }
}
