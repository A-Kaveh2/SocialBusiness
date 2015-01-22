package ir.rasen.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;

import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.ui.EditTextFont;
import ir.rasen.myapplication.ui.TextViewFont;

public class ActivityRegister extends Activity {

    EditTextFont username, name, email, password, password_repeat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_register);

        // SET VALUES
        username = ((EditTextFont) findViewById(R.id.edt_register_username));
        name = ((EditTextFont) findViewById(R.id.edt_register_name));
        email = ((EditTextFont) findViewById(R.id.edt_register_email));
        password = ((EditTextFont) findViewById(R.id.edt_register_password));
        password_repeat = ((EditTextFont) findViewById(R.id.edt_register_password_repeat));

        // SET ANIMATIONS
        setAnimations();
    }

    // SET PICTURE
    public void setPicture(View view) {
        // TODO SET PICTURE NOW
    }

    // REGISTER TOUCHED
    public void register(View view) {
        // SET ON TEXT CHANGE LISTENERS (FOR ERRORS)
        //setOnTextChangeListeners();
        // CHECK INPUT DATA
        if(!username.getText().toString().matches(Params.USER_USERNAME_VALIDATION) || username.getText().length()<Params.USER_USERNAME_MIN_LENGTH) {
            username.requestFocus();
            username.setError(getString(R.string.enter_valid_username));
            return;
        }
        if(username.getText().length()>Params.USER_USERNAME_MAX_LENGTH) {
            username.requestFocus();
            username.setError(getString(R.string.enter_is_too_long));
            return;
        }
        if(!name.getText().toString().matches(Params.USER_NAME_VALIDATION) || name.getText().length()< Params.USER_NAME_MIN_LENGTH) {
            name.requestFocus();
            name.setError(getString(R.string.enter_valid_name));
            return;
        }
        if(name.getText().length()>Params.USER_NAME_MAX_LENGTH) {
            name.requestFocus();
            name.setError(getString(R.string.enter_is_too_long));
            return;
        }
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            email.requestFocus();
            email.setError(getString(R.string.enter_valid_email));
            return;
        }
        if(password.getText().length()<Params.USER_PASSWORD_MIN_LENGTH) {
            password.requestFocus();
            password.setError(getString(R.string.enter_password_5_digits));
            return;
        }
        if(password_repeat.getText().length()<Params.USER_PASSWORD_MIN_LENGTH) {
            password.requestFocus();
            password.setError(getString(R.string.enter_password_5_digits));
            return;
        }
        if(!password.getText().toString().equals(password_repeat.getText().toString())) {
            password_repeat.requestFocus();
            password_repeat.setError(getString(R.string.enter_same_passwords));
            return;
        }
        // TODO REGISTER NOW!
        // .....
    }

    // HELP TOUCHED
    public void help(View view) {
        // TODO HELP NOW!
        // .....
    }

    public void setAnimations() {
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in_normal);
        ((Button) findViewById(R.id.btn_register_register)).startAnimation(fadeIn);
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
        ((TextViewFont) findViewById(R.id.btn_register_help)).startAnimation(animationSetBtn);
    }
    /*public void setOnTextChangeListeners() {
        username.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {
                //username.setText(username.getText().toString().trim());
                username.setError(null);
                if(!username.getText().toString().matches(Params.USER_USERNAME_VALIDATION) || username.getText().length()<Params.USER_USERNAME_MIN_LENGTH) {
                    username.setError(getString(R.string.enter_valid_username));
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });
        name.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {
                //name.setText(name.getText().toString().trim());
                name.setError(null);
                if(!name.getText().toString().matches(Params.USER_NAME_VALIDATION) || name.getText().length()<Params.USER_NAME_MIN_LENGTH) {
                    name.setError(getString(R.string.enter_valid_name));
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });
        email.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {
                email.setError(null);
                if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                    email.setError(getString(R.string.enter_valid_email));
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });
        TextWatcher textWatcherPassword = new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (password.getText().length() < Params.USER_PASSWORD_MIN_LENGTH) {
                    password.setError(getString(R.string.enter_password_5_digits));
                    return;
                }
                if(password_repeat.isFocused() && !password.getText().toString().equals(password_repeat.getText().toString())) {
                    password_repeat.setError(getString(R.string.enter_same_passwords));
                    return;
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        };
        password.addTextChangedListener(textWatcherPassword);
        password_repeat.addTextChangedListener(textWatcherPassword);
    }*/
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.to_0_from_left, R.anim.to_right);
    }
    public void back(View v) {
        onBackPressed();
    }

}
