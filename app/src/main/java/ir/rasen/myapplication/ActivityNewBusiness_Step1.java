package ir.rasen.myapplication;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;

import ir.rasen.myapplication.classes.Business;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.ui.EditTextFont;
import ir.rasen.myapplication.ui.TextViewFont;

public class ActivityNewBusiness_Step1 extends Activity {

    private Spinner category, subcategory;
    private EditTextFont name, description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_business_step_1);

        // SET VALUES
        name = ((EditTextFont) findViewById(R.id.edt_business_step1_name));
        category = ((Spinner) findViewById(R.id.spinner_business_step1_category));
        subcategory = ((Spinner) findViewById(R.id.spinner_business_step1_subcategory));
        description = ((EditTextFont) findViewById(R.id.edt_business_step1_description));

        // TODO:: USE THESE CODES TO SET CATEGORY DATA AFTER LOADING THEM
        //ArrayList<String> categoryList = new ArrayList<String>();
        //ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categoryList);
        //category.setAdapter(categoryAdapter);

        // TODO:: USE THESE CODES TO SET SUB CATEGORY DATA AFTER LOADING THEM
        //ArrayList<String> subcategoryList = new ArrayList<String>();
        //ArrayAdapter<String> subcategoryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, subcategoryList);
        //subcategory.setAdapter(subcategoryAdapter);

        // SET ANIMATIONS
        setAnimations();
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
        // TODO NEXT PAGE...
        Business newBusiness = new Business();
        Intent intent = new Intent(getBaseContext(), ActivityNewBusiness_Step2.class);
        // TODO PUT DATA IN INTENT
        intent.putExtra(Params.NAME, name.getText().toString());
        //intent.putExtra(Params.CATEGORY, category.getSelectedItem().toString());
        //intent.putExtra(Params.SUBCATEGORY, subcategory.getSelectedItem().toString());
        intent.putExtra(Params.DESCRIPTION, description.getText().toString());
        // TODO: BUT BITMAP IN INTENT
        // TODO: ...
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    // HELP TOUCHED
    public void help(View view) {
        // TODO HELP NOW!
        // .....
    }

    public void setAnimations() {
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in_normal);
        findViewById(R.id.btn_business_submit).startAnimation(fadeIn);
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
        findViewById(R.id.btn_business_help).startAnimation(animationSetBtn);
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
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
    public void back(View v) {
        onBackPressed();
    }

}
