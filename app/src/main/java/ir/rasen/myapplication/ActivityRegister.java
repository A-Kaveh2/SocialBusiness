package ir.rasen.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;

import java.io.File;

import ir.rasen.myapplication.classes.User;
import ir.rasen.myapplication.helper.Dialogs;
import ir.rasen.myapplication.helper.Image_M;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.ServerAnswer;
import ir.rasen.myapplication.ui.EditTextFont;
import ir.rasen.myapplication.ui.ProgressDialogCustom;
import ir.rasen.myapplication.webservice.WebserviceResponse;
import ir.rasen.myapplication.webservice.user.RegisterUser;

public class ActivityRegister extends Activity implements WebserviceResponse {
    private String TAG = "ActivityRegister";

    EditTextFont username, name, email, password, password_repeat;
    ImageButton btn_register_picture_set;
    String filePath;
    Context context;

    ProgressDialogCustom pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_register);
        context = this;

        // SET VALUES
        username = ((EditTextFont) findViewById(R.id.edt_register_username));
        name = ((EditTextFont) findViewById(R.id.edt_register_name));
        email = ((EditTextFont) findViewById(R.id.edt_register_email));
        password = ((EditTextFont) findViewById(R.id.edt_register_password));
        password_repeat = ((EditTextFont) findViewById(R.id.edt_register_password_repeat));
        btn_register_picture_set = (ImageButton) findViewById(R.id.btn_register_picture_set);
        pd = new ProgressDialogCustom(ActivityRegister.this);

        // SET ANIMATIONS
        setAnimations();
    }

    // SET PICTURE
    public void setPicture(View view) {
        final String[] items = getResources().getStringArray(R.array.camera_or_gallery);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, items);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.choose_photo);
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) { //pick from camera
                if (item == 0) {
                    Intent myIntent = new Intent(getApplicationContext(), ActivityCamera.class);
                    startActivityForResult(myIntent, ActivityCamera.CAPTURE_PHOTO);
                } else { //pick from file
                    Intent myIntent = new Intent(getApplicationContext(), ActivityGallery.class);
                    startActivityForResult(myIntent, ActivityGallery.CAPTURE_GALLERY);
                }
            }
        });

        Dialog d = builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == ActivityCamera.CAPTURE_PHOTO) {
                filePath = data.getStringExtra(ActivityCamera.FILE_PATH);
                displayCropedImage(filePath);
            } else if (requestCode == ActivityGallery.CAPTURE_GALLERY) {
                filePath = data.getStringExtra(ActivityGallery.FILE_PATH);
                displayCropedImage(filePath);
            }
        }

    }

    private void displayCropedImage(String filePath) {
        File file = new File(filePath);
        Bitmap myBitmap;
        if (file.exists()) {
            myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            if(Image_M.sizeOf(myBitmap)>1000000){
                BitmapFactory.Options ops = new BitmapFactory.Options();
                ops.inSampleSize = 2;
                myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath(),ops);
            }

            try {
                btn_register_picture_set.setImageBitmap(myBitmap);
            } catch (Exception e) {
                String sss = e.getMessage();
            }
        }
    }

    // REGISTER TOUCHED
    public void register(View view) {

        // CHECK INPUT DATA
        if (!username.getText().toString().matches(Params.USER_USERNAME_VALIDATION) || username.getText().length() < Params.USER_USERNAME_MIN_LENGTH) {
            username.requestFocus();
            username.setErrorC(getString(R.string.enter_valid_username));
            return;
        }
        if (username.getText().length() > Params.USER_USERNAME_MAX_LENGTH) {
            username.requestFocus();
            username.setErrorC(getString(R.string.enter_is_too_long));
            return;
        }
        if (!name.getText().toString().matches(Params.USER_NAME_VALIDATION) || name.getText().length() < Params.USER_NAME_MIN_LENGTH) {
            name.requestFocus();
            name.setErrorC(getString(R.string.enter_valid_name));
            return;
        }
        if (name.getText().length() > Params.USER_NAME_MAX_LENGTH) {
            name.requestFocus();
            name.setErrorC(getString(R.string.enter_is_too_long));
            return;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            email.requestFocus();
            email.setErrorC(getString(R.string.enter_valid_email));
            return;
        }
        if (password.getText().length() < Params.USER_PASSWORD_MIN_LENGTH) {
            password.requestFocus();
            password.setErrorC(getString(R.string.enter_password_5_digits));
            return;
        }
        if (password_repeat.getText().length() < Params.USER_PASSWORD_MIN_LENGTH) {
            password.requestFocus();
            password.setErrorC(getString(R.string.enter_password_5_digits));
            return;
        }
        if (!password.getText().toString().equals(password_repeat.getText().toString())) {
            password_repeat.requestFocus();
            password_repeat.setErrorC(getString(R.string.enter_same_passwords));
            return;
        }
        try {
            User user = new User();
            user.userName = username.getText().toString();
            user.name = name.getText().toString();
            user.email = email.getText().toString();
            user.password = password.getText().toString();


            if (filePath != null)
                user.profilePicture = Image_M.getBase64String(filePath);
            else
                user.profilePicture = "";

            pd.show();
            new RegisterUser(context, user, ActivityRegister.this).execute();
        } catch (Exception e) {
            Log.e(TAG, Params.CLOSED_BEFORE_RESPONSE);
        }
    }

    // HELP TOUCHED
    public void help(View view) {
        // TODO HELP NOW!
        // .....
    }

    public void setAnimations() {
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in_normal);
        findViewById(R.id.btn_register_register).startAnimation(fadeIn);
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
        findViewById(R.id.btn_register_help).startAnimation(animationSetBtn);
    }

    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.to_0_from_left, R.anim.to_right);
    }

    public void back(View v) {
        onBackPressed();
    }

    @Override
    public void getResult(Object result) {
        try {
            pd.dismiss();
            startActivity(new Intent(context, ActivityMain.class));
            finish();
        } catch (Exception e) {}
    }

    @Override
    public void getError(Integer errorCode) {
        try {
            pd.dismiss();
            String errorMessage = ServerAnswer.getError(getApplicationContext(), errorCode);
            Dialogs.showMessage(ActivityRegister.this, errorMessage);
        } catch (Exception e) {
            Log.e(TAG, Params.CLOSED_BEFORE_RESPONSE);
        }
    }
}
