package ir.rasen.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import ir.rasen.myapplication.classes.Business;
import ir.rasen.myapplication.helper.Dialogs;
import ir.rasen.myapplication.helper.PassingBusiness;

public class ActivityBusinessSettings extends Activity {

    private Business business;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_business_settings);

        business = PassingBusiness.getInstance().getValue();
        PassingBusiness.getInstance().setValue(null);

    }

    public void edit(View view) {
        PassingBusiness.getInstance().setValue(business);
        Intent intent = new Intent(ActivityBusinessSettings.this, ActivityNewBusiness_Step1.class);
        startActivity(intent);
        overridePendingTransition(R.anim.to_0, R.anim.to_left);
    }

    public void delete(View view) {
        // SHOWING POPUP WINDOW
        Dialogs dialogs = new Dialogs();
        dialogs.showBusinessDeletePopup(ActivityBusinessSettings.this, business.id);
    }

    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.to_0_from_left, R.anim.to_right);
    }

    public void back(View view) {
        onBackPressed();
    }

}
