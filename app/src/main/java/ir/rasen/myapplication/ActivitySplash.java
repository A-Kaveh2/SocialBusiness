package ir.rasen.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;

import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.ui.EditTextFont;

/**
 * Created by 'Sina KH'.
 */
public class ActivitySplash extends Activity {

	EditTextFont email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_splash_screen);

        // SET ANIMATIONS
        setAnimations();
    }

    // LOGIN TOUCHED
    public void login(View view) {
        gotoActivity(ActivityLogin.class);
     }

    // REGISTER TOUCHED
    public void register(View view) {
    	gotoActivity(ActivityRegister.class);
    }

    public void setAnimations() {
    	Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in_normal);
        findViewById(R.id.btn_splash_login).startAnimation(fadeIn);
		findViewById(R.id.btn_splash_register).startAnimation(fadeIn);
    }

    public void gotoActivity(Class targetClass) {
        Intent intent = new Intent(getBaseContext(), targetClass);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
