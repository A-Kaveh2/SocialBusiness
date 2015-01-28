package ir.rasen.myapplication;

import ir.rasen.myapplication.helper.Dialogs;
import ir.rasen.myapplication.helper.LoginInfo;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.ServerAnswer;
import ir.rasen.myapplication.ui.EditTextFont;
import ir.rasen.myapplication.webservice.WebserviceResponse;
import ir.rasen.myapplication.webservice.user.Login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
public class ActivityLogin extends Activity implements WebserviceResponse {
    private String TAG = "ActivityLogin";

    EditTextFont edtEmail, edtPassword;
    Context cotext;
    WebserviceResponse webserviceResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        cotext = this;
        webserviceResponse = this;

        // SET VALUES
        edtEmail = ((EditTextFont) findViewById(R.id.edt_login_email));
        edtPassword = ((EditTextFont) findViewById(R.id.edt_login_password));

        // SET ANIMATIONS
        setAnimations();
    }

    // LOGIN TOUCHED
    public void login(View view) {
        // CHECK INPUT DATA

        //TODO
        //commented for the test

       /* if (!android.util.Patterns.EMAIL_ADDRESS.matcher(edtEmail.getText().toString()).matches()) {
            edtEmail.requestFocus();
            edtEmail.setError(getString(R.string.enter_valid_email));
            return;
        }
        if (edtPassword.getText().length() < Params.USER_PASSWORD_MIN_LENGTH) {
            edtPassword.requestFocus();
            edtPassword.setError(getString(R.string.enter_password_5_digits));
            return;
        }*/

        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();

        //TODO
        //for the test
        email = "ali@gmail.com";
        password="123456";
        //for the test



        new Login(cotext, email, password, webserviceResponse).execute();
    }

    // FORGOT TOUCHED
    public void forgot(View v) {
        // TODO FORGOT
        /*ForgetPasswordDialog forgetPasswordDialog = new ForgetPasswordDialog(cotext,
                getResources().getString(R.string.dialog_forget_password));
        forgetPasswordDialog.show();*/
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

    @Override
    public void getResult(Object result) {
        try {
            boolean b = LoginInfo.isLoggedIn(cotext);
            startActivity(new Intent(cotext, ActivityMain.class));
        } catch (Exception e) {
            Log.e(TAG, Params.CLOSED_BEFORE_RESPONSE);
        }
    }

    @Override
    public void getError(Integer errorCode) {
        try {
            String errorMessage = ServerAnswer.getError(getBaseContext(), errorCode);
            Dialogs.showMessage(getBaseContext(), errorMessage);
        } catch(Exception e) {
            Log.e(TAG, Params.CLOSED_BEFORE_RESPONSE);
        }
    }
}
