package ir.rasen.myapplication;

import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.ui.EditTextFont;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;

/**
 * Created by 'Sina KH'.
 */
public class ActivityLogin extends Activity {

	EditTextFont email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        // SET VALUES
		email = ((EditTextFont) findViewById(R.id.edt_login_email));
		password = ((EditTextFont) findViewById(R.id.edt_login_password));

        // SET ANIMATIONS
        setAnimations();
    }

    // LOGIN TOUCHED
    public void login(View view) {
    	// CHECK INPUT DATA
    	if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
    		email.requestFocus();
    		email.setError(getString(R.string.enter_valid_email));
    		return;
    	}
    	if(password.getText().length()< Params.USER_PASSWORD_MIN_LENGTH) {
    		password.requestFocus();
    		password.setError(getString(R.string.enter_password_5_digits));
    		return;
    	}
    	// TODO LOGIN NOW!
    	// .....
     }

    // FORGOT TOUCHED
    public void forgot(View v) {
    	// TODO FORGOT
    	// .....
        /**
         * HERE WE USE INTENT TO TEST MAIN ACTIVITY DURING DEVELOPMENT OPERATIONS
         * */
        gotoActivity(ActivityMain.class);
        finish();
    }

    public void setAnimations() {
    	Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in_normal);
		findViewById(R.id.btn_login_login).startAnimation(fadeIn);
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
		findViewById(R.id.btn_login_forget).startAnimation(animationSetBtn);
    }


    public void gotoActivity(Class targetClass) {
        Intent intent = new Intent(getBaseContext(), targetClass);
        startActivity(intent);
        overridePendingTransition(R.anim.to_0, R.anim.to_left);
    }
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.to_0_from_left, R.anim.to_right);
    }

    public void back(View v) {
        onBackPressed();
    }
}
