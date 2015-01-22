package ir.rasen.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;

import ir.rasen.myapplication.classes.Post;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.PassingPosts;
import ir.rasen.myapplication.ui.EditTextFont;
import ir.rasen.myapplication.ui.TextViewFont;

public class ActivityNewPost extends Activity {

    private EditTextFont name, description, price, code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_post);

        // SET VALUES
        name = ((EditTextFont) findViewById(R.id.edt_post_name));
        description = ((EditTextFont) findViewById(R.id.edt_post_text));
        price = ((EditTextFont) findViewById(R.id.edt_post_price));
        code = ((EditTextFont) findViewById(R.id.edt_post_code));

        // SET ANIMATIONS
        setAnimations();

        if(PassingPosts.getInstance().getValue()!=null) {
            Post post = PassingPosts.getInstance().getValue().get(0);
            PassingPosts.getInstance().setValue(null);
            name.setText(post.title);
            description.setText(post.description);
            price.setText(post.price);
            code.setText(post.code);
        }
    }

    // SET PICTURE
    public void setPicture(View view) {
        // TODO SET PICTURE NOW
    }

    // SUBMIT TOUCHED
    public void submit(View view) {
        // SET ON TEXT CHANGE LISTENERS (FOR ERRORS)
        setOnTextChangeListeners();
        // CHECK INPUT DATA
        if(!name.getText().toString().matches(Params.USER_NAME_VALIDATION) || name.getText().length()< Params.USER_NAME_MIN_LENGTH) {
            name.requestFocus();
            name.setError(getString(R.string.enter_valid_name));
            return;
        }
        // TODO SUBMIT NOW!
        // .....
    }

    // HELP TOUCHED
    public void help(View view) {
        // TODO HELP NOW!
        // .....
    }

    public void setAnimations() {
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in_normal);
        ((Button) findViewById(R.id.btn_post_submit)).startAnimation(fadeIn);
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
        ((TextViewFont) findViewById(R.id.btn_post_help)).startAnimation(animationSetBtn);
    }
    public void setOnTextChangeListeners() {
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
    }
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.to_0_from_left, R.anim.to_right);
    }
    public void back(View v) {
        onBackPressed();
    }

}
