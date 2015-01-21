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

import ir.rasen.myapplication.helper.Location_M;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.PassingWorkTime;
import ir.rasen.myapplication.helper.WorkTime;
import ir.rasen.myapplication.ui.ButtonFont;
import ir.rasen.myapplication.ui.TextViewFont;

public class ActivityNewBusiness_Step2 extends Activity {

    String name, category, subcategory, description;
    WorkTime workTime;
    Location_M locationM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_business_step_2);

        name = getIntent().getStringExtra(Params.NAME);
        category = getIntent().getStringExtra(Params.CATEGORY);
        subcategory = getIntent().getStringExtra(Params.SUBCATEGORY);
        description = getIntent().getStringExtra(Params.DESCRIPTION);

        // SET ANIMATIONS
        setAnimations();

        // TODO:: updating views after changing variables like workTime... (in edit mode for ex.)
        updateViews();
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
        /*if(!name.getText().toString().matches(Params.USER_NAME_VALIDATION) || name.getText().length()< Params.USER_NAME_MIN_LENGTH) {
            name.requestFocus();
            name.setError(getString(R.string.enter_valid_name));
            return;
        }*/
        // TODO SUBMIT NOW!
        Intent intent = new Intent(getBaseContext(), ActivityNewBusiness_Step2.class);
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
        /*name.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {
                //name.setText(name.getText().toString().trim());
                name.setError(null);
                if(!name.getText().toString().matches(Params.USER_NAME_VALIDATION) || name.getText().length()<Params.USER_NAME_MIN_LENGTH) {
                    name.setError(getString(R.string.enter_valid_name));
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });*/
    }
    public void onBackPressed() {
		finish();
		overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
    public void back(View v) {
        onBackPressed();
    }

    // open work time dialog
    public void setWorkTime(View v) {
        Intent intent = new Intent(getBaseContext(), ActivityWorkTime.class);
        startActivityForResult(intent, Params.INTENT_WORK_TIME);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    void updateViews() {
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
                    "\nزمان شروع به کار: " + ((int) workTime.time_open / 60) + ":" + (workTime.time_open % 60)
                            + "\nزمان پایان کار: " + ((int) workTime.time_close / 60) + ":" + (workTime.time_close % 60);
            ((ButtonFont) findViewById(R.id.edt_business_step2_workingTime)).setText(workTimeText);
        }
    }

    // open set location dialog
    public void setLocation(View v) {
        Intent intent = new Intent(ActivityNewBusiness_Step2.this, ActivityLocation.class);
        intent.putExtra(Params.SET_LOCATION_TYPE, Params.SEARCH);
        startActivityForResult(intent, Params.INTENT_LOCATION);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==Params.INTENT_WORK_TIME && resultCode==Params.INTENT_OK) {
            workTime = PassingWorkTime.getInstance().getValue();
            updateViews();
        }
        if (requestCode == Params.INTENT_LOCATION && resultCode == Params.INTENT_OK) {
            locationM = new Location_M(data.getStringExtra(Params.LOCATION_LATITUDE), data.getStringExtra(Params.LOCATION_LONGITUDE));
            ((ButtonFont) findViewById(R.id.edt_business_step2_address)).setText(
                getString(R.string.address_defined)
                +"\n"+getString(R.string.lat)
                +"\n"+locationM.getLongitude()
                +"\n"+getString(R.string.lon)
                +"\n"+locationM.getLongitude());
        }
    }

}
