package ir.rasen.myapplication;

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

import java.util.ArrayList;
import java.util.Arrays;

import ir.rasen.myapplication.classes.Business;
import ir.rasen.myapplication.helper.Dialogs;
import ir.rasen.myapplication.helper.InnerFragment;
import ir.rasen.myapplication.helper.Location_M;
import ir.rasen.myapplication.helper.LoginInfo;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.PassingBusiness;
import ir.rasen.myapplication.helper.PassingWorkTime;
import ir.rasen.myapplication.helper.ServerAnswer;
import ir.rasen.myapplication.helper.WorkTime;
import ir.rasen.myapplication.ui.ButtonFont;
import ir.rasen.myapplication.ui.EditTextFont;
import ir.rasen.myapplication.ui.ProgressDialogCustom;
import ir.rasen.myapplication.ui.TextViewFont;
import ir.rasen.myapplication.webservice.WebserviceResponse;
import ir.rasen.myapplication.webservice.business.RegisterBusiness;
import ir.rasen.myapplication.webservice.business.UpdateBusinessProfileInfo;

public class ActivityNewBusiness_Step2 extends Activity implements WebserviceResponse {
    private String TAG = "ActivityNewBusiness_Step2";

    private EditTextFont edtPhone, edtWebsite, edtEmail, edtMobile;
    WorkTime workTime;
    Location_M locationM;
    boolean isEditing = false;
    private WebserviceResponse webserviceResponse;
    private Context context;

    ProgressDialogCustom pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_business_step_2);

        webserviceResponse = this;
        context = this;
        pd = new ProgressDialogCustom(context);

        // check if we are in edit mode or not
        if (getIntent().getBooleanExtra(Params.EDIT_MODE, false)) {
            isEditing = true;
            ((TextViewFont) findViewById(R.id.txt_business_step2_title)).setText(R.string.profile_edit_business);
        }

        edtPhone = ((EditTextFont) findViewById(R.id.edt_business_step2_phone));
        edtWebsite = ((EditTextFont) findViewById(R.id.edt_business_step2_website));
        edtEmail = ((EditTextFont) findViewById(R.id.edt_business_step2_email));
        edtMobile = ((EditTextFont) findViewById(R.id.edt_business_step2_mobile));

        // SET ANIMATIONS
        setAnimations();

        // updating views after changing variables like workTime... (in edit mode for ex.)
        updateViews();
    }

    // SUBMIT TOUCHED
    public void submit(View view) {

        // CHECK INPUT DATA
        // TODO SUBMIT NOW!
        putDataInPassingBusiness();
        Business business = PassingBusiness.getInstance().getValue();

        if (business.location_m == null) {
            Dialogs.showMessage(context, getString(R.string.err_location));
            return;
        }

        business.userID = LoginInfo.getUserId(context);

        if (isEditing) {
            new UpdateBusinessProfileInfo(business, webserviceResponse).execute();
        } else {
            new RegisterBusiness(business, webserviceResponse).execute();
        }
        pd.show();

        PassingBusiness.getInstance().setValue(business);
        ActivityNewBusiness_Step1.step1.finish();
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
                name.setErrorC(null);
                if(!name.getText().toString().matches(Params.USER_NAME_VALIDATION) || name.getText().length()<Params.USER_NAME_MIN_LENGTH) {
                    name.setErrorC(getString(R.string.enter_valid_name));
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });
    }*/
    public void onBackPressed() {
        putDataInPassingBusiness();
        finish();
        overridePendingTransition(R.anim.to_0_from_left, R.anim.to_right);
    }

    public void back(View v) {
        onBackPressed();
    }

    // open work time dialog
    public void setWorkTime(View v) {
        if (workTime != null) {
            PassingWorkTime.getInstance().setValue(workTime);
        }
        Intent intent = new Intent(getBaseContext(), ActivityWorkTime.class);
        startActivityForResult(intent, Params.INTENT_WORK_TIME);
        overridePendingTransition(R.anim.to_0, R.anim.to_left);
    }

    void updateViews() {
        Business business = PassingBusiness.getInstance().getValue();
        edtPhone.setText(business.phone);
        edtWebsite.setText(business.webSite);
        edtEmail.setText(business.email);
        edtMobile.setText(business.mobile);
        workTime = business.workTime;
        locationM = business.location_m;

        // TODO modify persian string hard code
        if (workTime != null) {
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
                    "\nزمان شروع به کار: " + two_char((workTime.time_open / 60)) + ":" + two_char((workTime.time_open % 60))
                            + "\nزمان پایان کار: " + two_char((workTime.time_close / 60)) + ":" + two_char((workTime.time_close % 60));
            ((ButtonFont) findViewById(R.id.edt_business_step2_workingTime)).setText(workTimeText);
        }
        if (locationM != null) {
            ((ButtonFont) findViewById(R.id.edt_business_step2_address)).setText(
                    getString(R.string.address_defined)
                            + "\n" + getString(R.string.lat)
                            + "\n" + locationM.getLongitude()
                            + "\n" + getString(R.string.lon)
                            + "\n" + locationM.getLongitude());
        }
    }

    private String two_char(int x) {
        if (Integer.toString(x).length() == 1)
            return "0" + Integer.toString(x);
        return Integer.toString(x);
    }

    // open set location dialog
    public void setLocation(View v) {
        Intent intent = new Intent(ActivityNewBusiness_Step2.this, ActivityLocation.class);
        intent.putExtra(Params.SET_LOCATION_TYPE, Params.SEARCH);
        if (locationM != null) {
            intent.putExtra(Params.LOCATION_LATITUDE, locationM.getLatitude());
            intent.putExtra(Params.LOCATION_LONGITUDE, locationM.getLongitude());
        }
        startActivityForResult(intent, Params.INTENT_LOCATION);
        overridePendingTransition(R.anim.to_0, R.anim.to_left);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Params.INTENT_WORK_TIME && resultCode == Params.INTENT_OK) {
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
        business.phone = edtPhone.getText().toString();
        business.webSite = edtWebsite.getText().toString();
        business.email = edtEmail.getText().toString();
        business.mobile = edtMobile.getText().toString();
        business.workTime = workTime;
        business.location_m = locationM;
        PassingBusiness.getInstance().setValue(business);
    }

    @Override
    public void getResult(Object result) {
        pd.dismiss();
        try {
            Dialogs.showMessage(context, context.getResources().getString(R.string.dialog_update_success));
            InnerFragment innerFragment = new InnerFragment(ActivityMain.activityMain);
            innerFragment.newProfile(context, Params.ProfileType.PROFILE_BUSINESS, true, PassingBusiness.getInstance().getValue().id);
            ActivityMain.activityMain.addBusiness(PassingBusiness.getInstance().getValue());
            PassingBusiness.getInstance().setValue(null);
            ActivityNewBusiness_Step1.step1.finish();
            finish();
        } catch (Exception e) {
            Log.e(TAG, Params.CLOSED_BEFORE_RESPONSE);
        }
    }

    @Override
    public void getError(Integer errorCode) {
        pd.dismiss();
        try {
            String errorMessage = ServerAnswer.getError(ActivityNewBusiness_Step2.this, errorCode);
            Dialogs.showMessage(context, errorMessage);
        } catch (Exception e) {
            Log.e(TAG, Params.CLOSED_BEFORE_RESPONSE);
        }
    }
}
