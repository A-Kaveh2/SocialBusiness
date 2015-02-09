package ir.rasen.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import ir.rasen.myapplication.classes.Business;
import ir.rasen.myapplication.helper.Dialogs;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.PassingBusiness;
import ir.rasen.myapplication.helper.ResultStatus;
import ir.rasen.myapplication.helper.ServerAnswer;
import ir.rasen.myapplication.ui.ProgressDialogCustom;
import ir.rasen.myapplication.webservice.WebserviceResponse;

public class ActivityBusinessSettings extends Activity implements WebserviceResponse {
    private static String TAG = "ActivityBusinessSettings";

    private Business business;
    private Context context;

    private ProgressDialogCustom pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_business_settings);

        context = this;
        pd=new ProgressDialogCustom(context);
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

        dialogs.showBusinessDeletePopup(context, business.id, ActivityBusinessSettings.this, pd);

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
        pd.dismiss();
        if(result instanceof ResultStatus){
            Toast.makeText(getBaseContext(), R.string.business_deleted, Toast.LENGTH_LONG).show();
            ActivityMain.activityMain.removeBusiness(business.id);
            ActivityMain.activityMain.backToRoot();
            finish();
        }
    }

    @Override
    public void getError(Integer errorCode) {
        pd.dismiss();
        try {
            String errorMessage = ServerAnswer.getError(ActivityBusinessSettings.this, errorCode);
            Dialogs.showMessage(ActivityBusinessSettings.this, errorMessage);
        } catch(Exception e) {
            Log.e(TAG, Params.CLOSED_BEFORE_RESPONSE);
        }
    }
}