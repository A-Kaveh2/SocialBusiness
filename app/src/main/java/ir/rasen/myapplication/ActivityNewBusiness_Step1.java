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
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import ir.rasen.myapplication.classes.Business;
import ir.rasen.myapplication.helper.Functions;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.PassingBusiness;
import ir.rasen.myapplication.ui.EditTextFont;
import ir.rasen.myapplication.ui.TextViewFont;

public class ActivityNewBusiness_Step1 extends Activity {

    private Spinner category, subcategory;
    private EditTextFont businessId, name, description;
    private boolean isEditing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_business_step_1);

        // SET VALUES
        businessId = ((EditTextFont) findViewById(R.id.edt_business_step1_id));
        name = ((EditTextFont) findViewById(R.id.edt_business_step1_name));
        category = ((Spinner) findViewById(R.id.spinner_business_step1_category));
        subcategory = ((Spinner) findViewById(R.id.spinner_business_step1_subcategory));
        description = ((EditTextFont) findViewById(R.id.edt_business_step1_description));

        // TODO:: USE THESE CODES TO SET CATEGORY DATA AFTER LOADING THEM
        ArrayList<String> categoryList = new ArrayList<String>();
        categoryList.add(0,"کسب و کار محلی");
        categoryList.add(0,"شرکت");
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categoryList);
        category.setAdapter(categoryAdapter);

        // TODO:: USE THESE CODES TO SET SUB CATEGORY DATA AFTER LOADING THEM
        ArrayList<String> subcategoryList = new ArrayList<String>();
        subcategoryList.add(0,"هایپر");
        subcategoryList.add(0,"مغازه");
        ArrayAdapter<String> subcategoryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, subcategoryList);
        subcategory.setAdapter(subcategoryAdapter);

        // SET ANIMATIONS
        setAnimations();

        if(PassingBusiness.getInstance().getValue()!=null) {
            // editing mode!
            isEditing = true;
            ((TextViewFont) findViewById(R.id.txt_business_step1_title)).setText(R.string.profile_edit_business);

            Business business = PassingBusiness.getInstance().getValue();
            PassingBusiness.getInstance().setValue(null);
            businessId.setText(business.businessID);
            name.setText(business.name);
            description.setText(business.description);

            // TODO:: AFTER LOADING CATEGORIES::
            /*for(int i=0; i<category.getCount(); i++) {
                if(categoryList.get(i).equals(business.category)) {
                    category.setSelection(i);
                    break;
                }
            }
            for(int i=0; i<subcategory.getCount(); i++) {
                if(subcategoryList.get(i).equals(business.subcategory)) {
                    subcategory.setSelection(i);
                    break;
                }
            }*/
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
        if(!businessId.getText().toString().matches(Params.USER_USERNAME_VALIDATION) || businessId.getText().length()<Params.USER_USERNAME_MIN_LENGTH) {
            businessId.requestFocus();
            businessId.setError(getString(R.string.enter_valid_username));
            return;
        }
        if(!name.getText().toString().matches(Params.USER_NAME_VALIDATION) || name.getText().length()< Params.USER_NAME_MIN_LENGTH) {
            name.requestFocus();
            name.setError(getString(R.string.enter_valid_name));
            return;
        }
        if(category.getSelectedItem()==null || subcategory.getSelectedItem()==null) {
            showChooseCategoryDialog();
            return;
        }
        if(description.getText().length()<Params.BUSINESS_DESCRIPTION_MIN_LENGTH) {
            description.requestFocus();
            description.setError(getString(R.string.enter_business_description));
            return;
        }
        // to next page
        // TODO PUT DATA IN PASSING BUSINESS
        Business business;
        if(PassingBusiness.getInstance().getValue()!=null)
            business = PassingBusiness.getInstance().getValue();
        else
            business = new Business();
        business.businessID = businessId.getText().toString();
        business.name = name.getText().toString();
        business.category = category.getSelectedItem().toString();
        business.subcategory = subcategory.getSelectedItem().toString();
        business.description = description.getText().toString();
        // TODO: BUT BITMAP IN business... ::
        // TODO...
        PassingBusiness.getInstance().setValue(business);
        Intent intent = new Intent(getBaseContext(), ActivityNewBusiness_Step2.class);
        intent.putExtra(Params.EDIT_MODE, isEditing);
        startActivity(intent);
        overridePendingTransition(R.anim.to_0, R.anim.to_left);
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
        PassingBusiness.getInstance().setValue(null);
        finish();
        overridePendingTransition(R.anim.to_0_from_left, R.anim.to_right);
    }
    public void back(View v) {
        onBackPressed();
    }

    void showChooseCategoryDialog() {
        Functions functions = new Functions();
        functions.showChooseCategoryFirstPopup(ActivityNewBusiness_Step1.this);
    }
}
