package ir.rasen.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import ir.rasen.myapplication.classes.Business;
import ir.rasen.myapplication.helper.Dialogs;
import ir.rasen.myapplication.helper.PassingBusiness;
import ir.rasen.myapplication.helper.ResultStatus;
import ir.rasen.myapplication.webservice.WebserviceResponse;

public class ActivityBusinessSettings extends Activity implements WebserviceResponse {

    private Business business;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_business_settings);

        context = this;
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

        //TODO remove test parts
        //for the test
        business = new Business();
        business.id =5;
        try {
            dialogs.showBusinessDeletePopup(context, business.id, ActivityBusinessSettings.this);
        }
        catch (Exception e){
            String s = e.getMessage();
        }
    }

    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.to_0_from_left, R.anim.to_right);
    }

    public void back(View view) {
        onBackPressed();
    }

    @Override
    public void getResult(Object result) {
        if(result instanceof ResultStatus){
            //TODO display message
            //delete business was successful
        }
    }

    @Override
    public void getError(Integer errorCode) {
        //TODO display error message
    }
}
