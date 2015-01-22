package ir.rasen.myapplication;

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
import android.widget.Spinner;

import ir.rasen.myapplication.classes.Business;
import ir.rasen.myapplication.helper.Location_M;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.PassingBusiness;
import ir.rasen.myapplication.helper.PassingWorkTime;
import ir.rasen.myapplication.helper.WorkTime;
import ir.rasen.myapplication.ui.ButtonFont;
import ir.rasen.myapplication.ui.EditTextFont;
import ir.rasen.myapplication.ui.TextViewFont;

public class ActivityNewBusiness_Step2 extends Activity {

    private EditTextFont phone, website, email, mobile;
    WorkTime workTime;
    Location_M locationM;
    boolean isEditing=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_business_step_2);

        // check if we are in edit mode or not
        if(getIntent().getBooleanExtra(Params.EDIT_MODE,false)) {
            isEditing = true;
            ((TextViewFont) findViewById(R.id.txt_business_step2_title)).setText(R.string.profile_edit_business);
        }

        phone = ((EditTextFont) findViewById(R.id.edt_business_step2_phone));
        website = ((EditTextFont) findViewById(R.id.edt_business_step2_website));
        email = ((EditTextFont) findViewById(R.id.edt_business_step2_email));
        mobile = ((EditTextFont) findViewById(R.id.edt_business_step2_mobile));

        // SET ANIMATIONS
        setAnimations();

        // TODO:: updating views after changing variables like workTime... (in edit mode for ex.)
        updateViews();
    }

    // SUBMIT TOUCHED
    public void submit(View view) {
        // SET ON TEXT CHANGE LISTENERS (FOR ERRORS)
        //setOnTextChangeListeners();
        // CHECK INPUT DATA
        /*if(!name.getText().toString().matches(Params.USER_NAME_VALIDATION) || name.getText().length()< Params.USER_NAME_MIN_LENGTH) {
            name.requestFocus();
            name.setError(getString(R.string.enter_valid_name));
            return;
        }*/
        // TODO SUBMIT NOW!
        putDataInPassingBusiness();
        Business business = PassingBusiness.getInstance().getValue();
        if(business.location_m==null)
        // TODO CREATE OR SUBMIT BUSINESS NOW ( from PassingBusiness.get... !!
        if(isEditing) {
            // TODO: EDIT
        } else {
            // TODO: CREATE NEW
        }
        PassingBusiness.getInstance().setValue(null);
        finish();
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
    /*public void setOnTextChangeListeners() {
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
    }*/
    public void onBackPressed() {
        // TODO PUT DATA IN PASSING BUSINESS
        putDataInPassingBusiness();
        finish();
        overridePendingTransition(R.anim.to_0_from_left, R.anim.to_right);
    }
    public void back(View v) {
        onBackPressed();
    }

    // open work time dialog
    public void setWorkTime(View v) {
        if(workTime!=null) {
            PassingWorkTime.getInstance().setValue(workTime);
        }
        Intent intent = new Intent(getBaseContext(), ActivityWorkTime.class);
        startActivityForResult(intent, Params.INTENT_WORK_TIME);
        overridePendingTransition(R.anim.to_0, R.anim.to_left);
    }

    void updateViews() {
        Business business = PassingBusiness.getInstance().getValue();
        phone.setText(business.phone);
        website.setText(business.webSite);
        email.setText(business.email);
        mobile.setText(business.mobile);
        workTime = business.workTime;
        locationM = business.location_m;
        if(workTime!=null) {
            boolean[] workDays = workTime.getWorkDays();
            String workTimeText = "";
            if (workDays[0])
                workTimeText += "ش, ";
            for (int i = 1; i < 6; i++) {
                if (workDays[i])
                    workTimeText += i + "ش, ";
            }
            if (workDays[6])
                workTimeText += "جمعه";
            workTimeText +=
                    "\nزمان شروع به کار: " + two_char(((int) workTime.time_open / 60)) + ":" + two_char((workTime.time_open % 60))
                            + "\nزمان پایان کار: " + two_char(((int) workTime.time_close / 60)) + ":" + two_char((workTime.time_close % 60));
            ((ButtonFont) findViewById(R.id.edt_business_step2_workingTime)).setText(workTimeText);
        }
        if(locationM!=null) {
            ((ButtonFont) findViewById(R.id.edt_business_step2_address)).setText(
                    getString(R.string.address_defined)
                            +"\n"+getString(R.string.lat)
                            +"\n"+locationM.getLongitude()
                            +"\n"+getString(R.string.lon)
                            +"\n"+locationM.getLongitude());
        }
    }
    private String two_char(int x) {
        if (Integer.toString(x).length()==1)
            return "0"+Integer.toString(x);
        return Integer.toString(x);
    }

    // open set location dialog
    public void setLocation(View v) {
        Intent intent = new Intent(ActivityNewBusiness_Step2.this, ActivityLocation.class);
        intent.putExtra(Params.SET_LOCATION_TYPE, Params.SEARCH);
        if(locationM!=null) {
            intent.putExtra(Params.LOCATION_LATITUDE, locationM.getLatitude());
            intent.putExtra(Params.LOCATION_LONGITUDE, locationM.getLongitude());
        }
        startActivityForResult(intent, Params.INTENT_LOCATION);
        overridePendingTransition(R.anim.to_0, R.anim.to_left);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==Params.INTENT_WORK_TIME && resultCode==Params.INTENT_OK) {
            workTime = PassingWorkTime.getInstance().getValue();
            PassingWorkTime.getInstance().setValue(null);
            putDataInPassingBusiness();
            updateViews();
        }
        if (requestCode == Params.INTENT_LOCATION && resultCode == Params.INTENT_OK) {
            locationM = new Location_M(data.getStringExtra(Params.LOCATION_LATITUDE), data.getStringExtra(Params.LOCATION_LONGITUDE));
            putDataInPassingBusiness();
            updateViews();
        }
    }

    void putDataInPassingBusiness() {
        Business business;
        business = PassingBusiness.getInstance().getValue();
        business.phone = phone.getText().toString();
        business.webSite = website.getText().toString();
        business.email = email.getText().toString();
        business.mobile = mobile.getText().toString();
        business.workTime = workTime;
        business.location_m = locationM;
        PassingBusiness.getInstance().setValue(business);
    }

}
