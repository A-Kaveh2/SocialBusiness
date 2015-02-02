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
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.io.File;
import java.util.ArrayList;

import ir.rasen.myapplication.classes.User;
import ir.rasen.myapplication.helper.Dialogs;
import ir.rasen.myapplication.helper.Image_M;
import ir.rasen.myapplication.helper.LoginInfo;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.PersianDate;
import ir.rasen.myapplication.helper.ResultStatus;
import ir.rasen.myapplication.helper.ServerAnswer;
import ir.rasen.myapplication.helper.Sex;
import ir.rasen.myapplication.ui.ButtonFont;
import ir.rasen.myapplication.ui.EditTextFont;
import ir.rasen.myapplication.ui.TextViewFont;
import ir.rasen.myapplication.webservice.WebserviceResponse;
import ir.rasen.myapplication.webservice.user.GetUserProfileInfo;
import ir.rasen.myapplication.webservice.user.UpdateUserProfile;

public class ActivityUserProfileEdit extends Activity implements WebserviceResponse {
    private String TAG = "ActivityUserProfileEdit";

    EditTextFont edtName, edtAboutMe;
    ImageButton imbProfilePicture;
    TextViewFont txtBirthDate;
    Spinner spinnerSex;
    String filePath;
    User user = new User();
    Context context;
    boolean sex;

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
        spinnerSex = (Spinner) findViewById(R.id.spinner_profile_edit_sex);

        ArrayList<String> sexList = new ArrayList<>();
        sexList.add(getString(R.string.sex_unknown));
        sexList.add(getString(R.string.sex_male));
        sexList.add(getString(R.string.sex_female));
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(context, R.layout.layout_item_text, sexList);
        spinnerSex.setAdapter(categoryAdapter);
        spinnerSex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // TODO: SET USER'S SEX
                //user.sex = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // SET ANIMATIONS
        setAnimations();

        try {
            new GetUserProfileInfo(LoginInfo.getUserId(getApplicationContext()), ActivityUserProfileEdit.this).execute();
        }
        catch (Exception e){
            String s = e.getMessage();
        }
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
        Dialog dialog = new Dialog(ActivityUserProfileEdit.this, R.style.AppTheme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_change_password);
        final EditTextFont pass_old = (EditTextFont) dialog.findViewById(R.id.edt_profile_edit_password_old);
        final EditTextFont pass1 = (EditTextFont) dialog.findViewById(R.id.edt_profile_edit_password_new);
        final EditTextFont pass2 = (EditTextFont) dialog.findViewById(R.id.edt_profile_edit_password_new2);
        dialog.findViewById(R.id.btn_profile_edit_password_change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!user.password.equals(pass_old.getText().toString())) {
                    pass_old.requestFocus();
                    pass_old.setErrorC(getString(R.string.err_profile_edit_password_old_incorrect));
                    return;
                }
                if (!pass1.getText().toString().equals(pass2.getText().toString())) {
                    pass2.requestFocus();
                    pass2.setErrorC(getString(R.string.enter_same_passwords));
                    return;
                }
                user.password = pass1.getText().toString();
                Dialogs.showMessage(context, getString(R.string.password_will_change));
            }
        });
        dialog.show();
    }

    // BIRTHDATE TOUCHED
    public void changeBirthDate(View view) {
        // TODO DISPLAY CHANGE BIRTHDATE DIALOG
        final Dialog dialog = new Dialog(ActivityUserProfileEdit.this, R.style.AppTheme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_change_birthday);
        final EditTextFont year = (EditTextFont) dialog.findViewById(R.id.edt_profile_edit_birthday_year);
        final EditTextFont month = (EditTextFont) dialog.findViewById(R.id.edt_profile_edit_birthday_month);
        final EditTextFont day = (EditTextFont) dialog.findViewById(R.id.edt_profile_edit_birthday_day);
        dialog.findViewById(R.id.btn_profile_edit_birthday_change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            try {
                if(!PersianDate.validateDayBaseOnMonth(Integer.parseInt(day.getText().toString()), Integer.parseInt(month.getText().toString()))) {
                    Dialogs.showMessage(context, getString(R.string.invalid_birthday));
                    return;
                }
            } catch(Exception e) {
                Dialogs.showMessage(context, getString(R.string.invalid_birthday));
                return;
            }
            user.birthDate = year.getText().toString()+"/"+month.getText().toString()+"/"+day.getText().toString();
            txtBirthDate.setText(user.birthDate);
            dialog.dismiss();
            }
        });
        dialog.show();
    }

    // SAVE TOUCHED
    public void save(View view) {

        // CHECK INPUT DATA
        if (!edtName.getText().toString().matches(Params.USER_NAME_VALIDATION) || edtName.getText().length() < Params.USER_NAME_MIN_LENGTH) {
            edtName.requestFocus();
            edtName.setErrorC(getString(R.string.enter_valid_name));
            return;
        }

        user.name = edtName.getText().toString();
        user.aboutMe = edtAboutMe.getText().toString();
        user.birthDate = txtBirthDate.getText().toString();
        if (filePath != null)
            user.profilePicture = Image_M.getBase64String(filePath);
        else
            user.profilePicture = "";

        if(spinnerSex.getSelectedItemPosition()!= 0){
            switch (spinnerSex.getSelectedItemPosition()){
                case 1:
                    user.sex = Sex.MALE;
                    break;
                case 2:
                    user.sex = Sex.FEMALE;
            }
        }

        //TODO for the test becuase getProfileInfo doesn't work
        user.email= "";
        user.password = "";
        user.coverPicture = "";

        try {
            new UpdateUserProfile(user, ActivityUserProfileEdit.this).execute();
        }
        catch (Exception e){
            String s = e.getMessage();
        }
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
                edtName.setErrorC(null);
                if (!edtName.getText().toString().matches(Params.USER_NAME_VALIDATION) || edtName.getText().length() < Params.USER_NAME_MIN_LENGTH) {
                    edtName.setErrorC(getString(R.string.enter_valid_name));
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
        try {
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
                Dialogs.showMessage(context, context.getResources().getString(R.string.dialog_update_success));
            }
        } catch (Exception e) {
            Log.e(TAG, Params.CLOSED_BEFORE_RESPONSE);
        }
    }

    @Override
    public void getError(Integer errorCode) {
        try {
            String errorMessage = ServerAnswer.getError(getApplicationContext(), errorCode);
            Dialogs.showMessage(context, errorMessage);
        } catch (Exception e) {
            Log.e(TAG, Params.CLOSED_BEFORE_RESPONSE);
        }
    }
}
