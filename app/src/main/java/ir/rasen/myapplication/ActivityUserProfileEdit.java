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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;

import java.io.File;

import ir.rasen.myapplication.classes.User;
import ir.rasen.myapplication.helper.Functions;
import ir.rasen.myapplication.helper.Image_M;
import ir.rasen.myapplication.helper.LoginInfo;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.ResultStatus;
import ir.rasen.myapplication.helper.ServerAnswer;
import ir.rasen.myapplication.ui.ButtonFont;
import ir.rasen.myapplication.ui.EditTextFont;
import ir.rasen.myapplication.ui.TextViewFont;
import ir.rasen.myapplication.webservice.WebserviceResponse;
import ir.rasen.myapplication.webservice.user.GetUserProfileInfo;
import ir.rasen.myapplication.webservice.user.UpdateUserProfile;

public class ActivityUserProfileEdit extends Activity implements WebserviceResponse {

    EditTextFont edtName, edtAboutMe;
    ImageButton imbProfilePicture;
    TextViewFont txtBirthDate;
    String filePath;
    User user;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_profile_edit);

        context = this;
        // SET VALUES
        edtName = (EditTextFont) findViewById(R.id.edt_profile_edit_name);
        edtAboutMe = (EditTextFont) findViewById(R.id.edt_profile_edit_about_me);
        imbProfilePicture = (ImageButton) findViewById(R.id.btn_profile_edit_picture_set);
        txtBirthDate = (TextViewFont) findViewById(R.id.edt_profile_edit_birthday);

        // SET ANIMATIONS
        setAnimations();

        new GetUserProfileInfo(LoginInfo.getUserId(this));
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
                String filePath = data.getStringExtra(ActivityCamera.FILE_PATH);
                displayCropedImage(filePath);
            } else if (requestCode == ActivityGallery.CAPTURE_GALLERY) {
                filePath = data.getStringExtra(ActivityGallery.FILE_PATH);
                displayCropedImage(filePath);
            }
        }

    }

    private void displayCropedImage(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            try {
                imbProfilePicture.setImageBitmap(myBitmap);
            } catch (Exception e) {
                String s = e.getMessage();
            }
        }
    }

    // PASSWORD TOUCHED
    public void changePassword(View view) {

        // TODO DISPLAY CHANGE PASSWORD DIALOG
        user.password = "new password";
    }

    // BIRTHDATE TOUCHED
    public void changeBirthDate(View view) {

        // TODO DISPLAY CHANGE BIRTHDATE DIALOG
        user.birthDate = "new birthdate";
    }


    // SAVE TOUCHED
    public void save(View view) {

        // CHECK INPUT DATA
        if (!edtName.getText().toString().matches(Params.USER_NAME_VALIDATION) || edtName.getText().length() < Params.USER_NAME_MIN_LENGTH) {
            edtName.requestFocus();
            edtName.setError(getString(R.string.enter_valid_name));
            return;
        }

        user.name = edtName.getText().toString();
        user.aboutMe = edtAboutMe.getText().toString();
        user.birthDate = txtBirthDate.getText().toString();
        if (filePath != null)
            user.profilePicture = Image_M.getBase64String(filePath);

        //TODO user.sex

        new UpdateUserProfile(user).execute();
    }


    public void setAnimations() {
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in_normal);
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
        ((ButtonFont) findViewById(R.id.btn_profile_edit_save)).startAnimation(animationSetBtn);
    }

    public void setOnTextChangeListeners() {
        edtName.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                //name.setText(name.getText().toString().trim());
                edtName.setError(null);
                if (!edtName.getText().toString().matches(Params.USER_NAME_VALIDATION) || edtName.getText().length() < Params.USER_NAME_MIN_LENGTH) {
                    edtName.setError(getString(R.string.enter_valid_name));
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
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
        if (result instanceof User) {
            // result from executing GetUserProfileInfo
            //initialing profile info
            user = (User) result;
            edtName.setText(user.name);
            edtAboutMe.setText(user.aboutMe);
            byte[] decodedProfilePicture = Base64.decode(user.profilePicture, Base64.DEFAULT);
            Bitmap bitmapProfilePicture = BitmapFactory.decodeByteArray(decodedProfilePicture, 0, decodedProfilePicture.length);
            imbProfilePicture.setImageBitmap(bitmapProfilePicture);
            txtBirthDate.setText(user.birthDate);
        } else if (result instanceof ResultStatus) {
            // result from executing UpdateUserProfileInfo
            Functions.showMessage(context, context.getResources().getString(R.string.dialog_update_success));
        }
    }

    @Override
    public void getError(Integer errorCode) {
        String errorMessage = ServerAnswer.getError(getApplicationContext(), errorCode);
        Functions.showMessage(context, errorMessage);
    }
}
