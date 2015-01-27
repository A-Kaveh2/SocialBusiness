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
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import ir.rasen.myapplication.classes.Business;
import ir.rasen.myapplication.helper.Functions;
import ir.rasen.myapplication.helper.Image_M;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.PassingBusiness;
import ir.rasen.myapplication.helper.ServerAnswer;
import ir.rasen.myapplication.ui.EditTextFont;
import ir.rasen.myapplication.ui.TextViewFont;
import ir.rasen.myapplication.webservice.WebserviceResponse;
import ir.rasen.myapplication.webservice.business.GetBusinessGategories;
import ir.rasen.myapplication.webservice.business.GetBusinessSubcategories;

public class ActivityNewBusiness_Step1 extends Activity implements WebserviceResponse {

    private Spinner spnCategory, spnSubcategory;
    private EditTextFont edtBusinessId, edtName, edtDescription;
    private boolean isEditing = false;
    private ImageButton imbProfilePicture;
    private String profilePictureFilePath;
    private WebserviceResponse webserviceResponse;
    private Context context;
    private ArrayList<String> categoryList;
    public static Activity step1;
    private boolean closed=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_business_step_1);

        webserviceResponse = this;
        context = this;
        step1=this;

        // SET VALUES
        edtBusinessId = (EditTextFont) findViewById(R.id.edt_business_step1_id);
        edtName = (EditTextFont) findViewById(R.id.edt_business_step1_name);
        spnCategory = (Spinner) findViewById(R.id.spinner_business_step1_category);
        spnSubcategory = (Spinner) findViewById(R.id.spinner_business_step1_subcategory);
        edtDescription = (EditTextFont) findViewById(R.id.edt_business_step1_description);
        imbProfilePicture = (ImageButton) findViewById(R.id.btn_register_picture_set);

        // SET ANIMATIONS
        setAnimations();

        if (PassingBusiness.getInstance().getValue() != null) {
            // editing mode!
            isEditing = true;
            ((TextViewFont) findViewById(R.id.txt_business_step1_title)).setText(R.string.profile_edit_business);

            Business business = PassingBusiness.getInstance().getValue();
            PassingBusiness.getInstance().setValueStep1(null);
            edtBusinessId.setText(business.businessID);
            edtName.setText(business.name);
            edtDescription.setText(business.description);

            //spnCategory and spnSubcategory will initiate after executing GetBusinessGategories and GetBusinessSubcategories

        }

        new GetBusinessGategories(webserviceResponse).execute();

        spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                new GetBusinessSubcategories(parentView.getItemAtPosition(position).toString()
                        , webserviceResponse).execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
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
                profilePictureFilePath = data.getStringExtra(ActivityGallery.FILE_PATH);
                displayCropedImage(profilePictureFilePath);
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

    // SUBMIT TOUCHED
    public void submit(View view) {

        // CHECK INPUT DATA
        if (!edtBusinessId.getText().toString().matches(Params.USER_USERNAME_VALIDATION) || edtBusinessId.getText().length() < Params.USER_USERNAME_MIN_LENGTH) {
            edtBusinessId.requestFocus();
            edtBusinessId.setError(getString(R.string.enter_valid_username));
            return;
        }
        if (!edtName.getText().toString().matches(Params.USER_NAME_VALIDATION) || edtName.getText().length() < Params.USER_NAME_MIN_LENGTH) {
            edtName.requestFocus();
            edtName.setError(getString(R.string.enter_valid_name));
            return;
        }
        if (spnCategory.getSelectedItem() == null || spnSubcategory.getSelectedItem() == null) {
            showChooseCategoryDialog();
            return;
        }
        if (edtDescription.getText().length() < Params.BUSINESS_DESCRIPTION_MIN_LENGTH) {
            edtDescription.requestFocus();
            edtDescription.setError(getString(R.string.enter_business_description));
            return;
        }
        // to next page
        // TODO PUT DATA IN PASSING BUSINESS
        Business business;
        if (PassingBusiness.getInstance().getValue() != null)
            business = PassingBusiness.getInstance().getValue();
        else
            business = new Business();
        business.businessID = edtBusinessId.getText().toString();
        business.name = edtName.getText().toString();
        business.category = spnCategory.getSelectedItem().toString();
        business.subcategory = spnSubcategory.getSelectedItem().toString();
        business.description = edtDescription.getText().toString();
        if (profilePictureFilePath != null)
            business.profilePicture = Image_M.getBase64String(profilePictureFilePath);

        PassingBusiness.getInstance().setValueStep1(business);
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

    public void onBackPressed() {
        PassingBusiness.getInstance().setValueStep1(null);
        finish();
        overridePendingTransition(R.anim.to_0_from_left, R.anim.to_right);
    }

    public void back(View v)
    {
        closed=true;
        onBackPressed();
    }

    void showChooseCategoryDialog() {
        Functions functions = new Functions();
        functions.showChooseCategoryFirstPopup(ActivityNewBusiness_Step1.this);
    }

    @Override
    public void getResult(Object result) {
        if(closed) return;

        if (result instanceof ArrayList) {
            Business business = PassingBusiness.getInstance().getValue();
            if(categoryList == null) {
                //result from executing GetBusinessCategories
                categoryList = (ArrayList<String>) result;
                ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, categoryList);
                spnCategory.setAdapter(categoryAdapter);
                if(isEditing){

                    for (int i = 0;i<categoryList.size();i++){
                        if(categoryList.get(i).equals(business.category))
                            spnCategory.setSelection(i);
                    }
                }
            }
            else{
                //result from executing GetBusinessSubcategories
                ArrayList<String> subcategoryList = (ArrayList<String>) result;
                ArrayAdapter<String> subcategoryAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, subcategoryList);
                spnSubcategory.setAdapter(subcategoryAdapter);
                if(isEditing){
                    for (int i = 0;i<subcategoryList.size();i++){
                        if(subcategoryList.get(i).equals(business.subcategory))
                            spnSubcategory.setSelection(i);
                    }
                }
            }
        }

    }

    @Override
    public void getError(Integer errorCode) {
        if(closed) return;

        String errorMessage = ServerAnswer.getError(getApplicationContext(), errorCode);
        Functions.showMessage(context, errorMessage);
    }
}
