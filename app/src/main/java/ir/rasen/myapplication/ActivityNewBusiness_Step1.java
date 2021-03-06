package ir.rasen.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ArrowKeyMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
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
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import java.io.File;
import java.util.ArrayList;

import ir.rasen.myapplication.classes.Business;
import ir.rasen.myapplication.classes.Category;
import ir.rasen.myapplication.classes.SubCategory;
import ir.rasen.myapplication.helper.CropResult;
import ir.rasen.myapplication.helper.CustomeCamera;
import ir.rasen.myapplication.helper.Dialogs;
import ir.rasen.myapplication.helper.Image_M;
import ir.rasen.myapplication.helper.LoginInfo;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.PassingBusiness;
import ir.rasen.myapplication.helper.ServerAnswer;
import ir.rasen.myapplication.helper.TextProcessor;
import ir.rasen.myapplication.ui.EditTextFont;
import ir.rasen.myapplication.ui.ProgressDialogCustom;
import ir.rasen.myapplication.ui.TextViewFont;
import ir.rasen.myapplication.webservice.WebserviceResponse;
import ir.rasen.myapplication.webservice.business.GetBusinessGategories;
import ir.rasen.myapplication.webservice.business.GetBusinessProfileInfo;
import ir.rasen.myapplication.webservice.business.GetBusinessSubcategories;

public class ActivityNewBusiness_Step1 extends Activity implements WebserviceResponse,CropResult {
    private String TAG = "ActivityNewBusiness_Step1";

    ActivityNewBusiness_Step1 activityNewBusiness_step1;

    private Spinner spnCategory, spnSubcategory;
    private EditTextFont edtBusinessId, edtName, edtDescription;
    private boolean isEditing = false;
    private ImageButton imbProfilePicture;
    private String profilePictureFilePath;
    private WebserviceResponse webserviceResponse;
    private Context context;
    private ArrayList<Category> categoryList;
    public static Activity step1;
    ArrayList<SubCategory> subcategoryObjectList;


    private Business existedBusiness;
    FrameLayout cameraPreview ;
    Button captureButton ;
    private ProgressDialogCustom pd;
    RelativeLayout rl_camera_section;
    CustomeCamera customeCamera;
    LinearLayout ll_camera_cover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_business_step_1);

        webserviceResponse = this;
        context = this;
        step1 = this;
        pd = new ProgressDialogCustom(context);

        // SET VALUES
        edtBusinessId = (EditTextFont) findViewById(R.id.edt_business_step1_id);
        edtName = (EditTextFont) findViewById(R.id.edt_business_step1_name);
        spnCategory = (Spinner) findViewById(R.id.spinner_business_step1_category);
        spnSubcategory = (Spinner) findViewById(R.id.spinner_business_step1_subcategory);
        edtDescription = (EditTextFont) findViewById(R.id.edt_business_step1_description);
        imbProfilePicture = (ImageButton) findViewById(R.id.btn_register_picture_set);

        cameraPreview = (FrameLayout) findViewById(R.id.camera_preview);
        captureButton = (Button) findViewById(R.id.button_capture);
        rl_camera_section = (RelativeLayout)findViewById(R.id.rl_camera_section);
        ll_camera_cover = (LinearLayout)findViewById(R.id.ll_camera_cover);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        width = (width/4)*3;
        int height = displaymetrics.heightPixels;
        height = (height/4)*3;
        RelativeLayout.LayoutParams lp_camera_section = new RelativeLayout.LayoutParams(width, height);
        lp_camera_section.addRule(RelativeLayout.CENTER_IN_PARENT,RelativeLayout.TRUE);
        rl_camera_section.setLayoutParams(lp_camera_section);

        RelativeLayout.LayoutParams lp_cover = new RelativeLayout.LayoutParams(width, height-width);
        lp_cover.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE);
        ll_camera_cover.setLayoutParams(lp_cover);

        customeCamera = new CustomeCamera(this, cameraPreview,getResources().getInteger(R.integer.image_size),getResources().getInteger(R.integer.image_quality));
        customeCamera.delegate = this;
        captureButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            customeCamera.capturePhoto();
                        } catch (Exception e) {
                            Log.d(TAG, e.getMessage());
                        }

                    }
                }
        );

        // SET ANIMATIONS
        setAnimations();

        if (PassingBusiness.getInstance().getValue() != null) {
            // editing mode!
            isEditing = true;
            edtBusinessId.setEnabled(false);
            edtBusinessId.setBackgroundColor(Color.LTGRAY);
            edtName.requestFocus();
            ((TextViewFont) findViewById(R.id.txt_business_step1_title)).setText(R.string.profile_edit_business);

            Business business = PassingBusiness.getInstance().getValue();

            pd.show();
            new GetBusinessProfileInfo(business.id, ActivityNewBusiness_Step1.this).execute();

        }

        new GetBusinessGategories(webserviceResponse).execute();

        spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                pd.show();
                new GetBusinessSubcategories(categoryList.get(position).id
                        , webserviceResponse).execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        edtDescription.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                edtDescription.removeTextChangedListener(this);
                TextProcessor.processEdtHashtags(edtDescription.getText().toString(), edtDescription, ActivityNewBusiness_Step1.this);
                edtDescription.addTextChangedListener(this);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    // SET PICTURE
    public void setPicture(View view) {
        final String[] items = getResources().getStringArray(R.array.camera_or_gallery);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, items);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.choose_photo);
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                //pick from camera
                if (item == 0) {
                    rl_camera_section.setVisibility(View.VISIBLE);
                    //Intent myIntent = new Intent(getApplicationContext(), ActivityCamera.class);
                    //startActivityForResult(myIntent, ActivityCamera.CAPTURE_PHOTO);
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
                profilePictureFilePath = data.getStringExtra(ActivityCamera.FILE_PATH);
                displayCropedImage(profilePictureFilePath);
            } else if (requestCode == ActivityGallery.CAPTURE_GALLERY) {
                profilePictureFilePath = data.getStringExtra(ActivityGallery.FILE_PATH);
                displayCropedImage(profilePictureFilePath);
            } else if (requestCode == Params.ACTION_ADD_NEW_BUSIENSS_SUCCESS) {
                Intent i = getIntent();
                i.putExtra(Params.BUSINESS_ID, data.getIntExtra(Params.BUSINESS_ID, 0));
                i.putExtra(Params.BUSINESS_USER_NAME, data.getStringExtra(Params.BUSINESS_USER_NAME));
                setResult(RESULT_OK, i);
                finish();
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
        if (!isEditing) {
            if (!edtBusinessId.getText().toString().matches(Params.USER_USERNAME_VALIDATION) || edtBusinessId.getText().length() < Params.USER_USERNAME_MIN_LENGTH) {
                edtBusinessId.requestFocus();
                edtBusinessId.setErrorC(getString(R.string.enter_valid_username));
                return;
            }
        }
        if (!edtName.getText().toString().matches(Params.USER_NAME_VALIDATION) || edtName.getText().length() < Params.USER_NAME_MIN_LENGTH) {
            edtName.requestFocus();
            edtName.setErrorC(getString(R.string.enter_valid_name));
            return;
        }
        if (spnCategory.getSelectedItem() == null || spnSubcategory.getSelectedItem() == null) {
            showChooseCategoryDialog();
            return;
        }
        if (edtDescription.getText().length() < Params.BUSINESS_DESCRIPTION_MIN_LENGTH) {
            edtDescription.requestFocus();
            edtDescription.setErrorC(getString(R.string.enter_business_description));
            return;
        }
        // to next page
        // TODO PUT DATA IN PASSING BUSINESS
        Business business;
        if (PassingBusiness.getInstance().getValue() != null)
            business = PassingBusiness.getInstance().getValue();
        else
            business = new Business();
        business.userID = LoginInfo.getUserId(context);
        business.businessUserName = edtBusinessId.getText().toString();
        business.name = edtName.getText().toString();
        business.category = spnCategory.getSelectedItem().toString();
        business.categoryID = categoryList.get(spnCategory.getSelectedItemPosition()).id;
        business.subcategory = spnSubcategory.getSelectedItem().toString();
        business.subCategoryID = subcategoryObjectList.get(spnSubcategory.getSelectedItemPosition()).id;
        business.description = edtDescription.getText().toString();
        business.description = business.description.replace("\n", " ");
        business.hashtagList = TextProcessor.getHashtags(business.description);
        //remove hashtags from description
        business.description = business.description.replaceAll("#[a-z|A-Z|0-9|_]*","");
        if (profilePictureFilePath != null)
            business.profilePicture = Image_M.getBase64String(profilePictureFilePath);

        business.categoryID = categoryList.get(spnCategory.getSelectedItemPosition()).id;
        business.subCategoryID = subcategoryObjectList.get(spnSubcategory.getSelectedItemPosition()).id;

        PassingBusiness.getInstance().setValue(business);
        Intent intent = new Intent(getBaseContext(), ActivityNewBusiness_Step2.class);
        intent.putExtra(Params.EDIT_MODE, isEditing);

        startActivityForResult(intent, Params.ACTION_ADD_NEW_BUSIENSS_SUCCESS);
        //startActivity(intent);
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
        PassingBusiness.getInstance().setValue(null);
        finish();
        overridePendingTransition(R.anim.to_0_from_left, R.anim.to_right);
    }

    public void back(View v) {
        onBackPressed();
    }

    void showChooseCategoryDialog() {
        Dialogs dialogs = new Dialogs();
        dialogs.showChooseCategoryFirstPopup(ActivityNewBusiness_Step1.this);
    }

    @Override
    public void getResult(Object result) {
        pd.dismiss();
        try {
            if (result instanceof ArrayList) {
                //Business business = PassingBusiness.getInstance().getValue();
                if (categoryList == null) {
                    //result from executing GetBusinessCategories
                    categoryList = (ArrayList<Category>) result;
                    ArrayList<String> categoryListStr = new ArrayList<>();
                    for (Category category : categoryList)
                        categoryListStr.add(category.name);

                    ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, categoryListStr);
                    spnCategory.setAdapter(categoryAdapter);
                    if (isEditing) {
                        if (existedBusiness != null) {
                            for (int i = 0; i < categoryList.size(); i++) {
                                if (categoryList.get(i).name == existedBusiness.category)
                                    spnCategory.setSelection(i);
                            }
                        }
                        edtBusinessId.setEnabled(false);
                    }
                } else {
                    //result from executing GetBusinessSubcategories
                    ArrayList<String> subcategoryList = new ArrayList<>();
                    subcategoryObjectList = (ArrayList<SubCategory>) result;
                    for (int i = 0; i < subcategoryObjectList.size(); i++) {
                        subcategoryList.add(subcategoryObjectList.get(i).name);
                    }
                    ArrayAdapter<String> subcategoryAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, subcategoryList);
                    spnSubcategory.setAdapter(subcategoryAdapter);
                    if (isEditing) {
                        if (existedBusiness != null)
                            for (int i = 0; i < subcategoryList.size(); i++) {
                                if (subcategoryList.get(i).equals(existedBusiness.subcategory))
                                    spnSubcategory.setSelection(i);
                            }
                    }
                }
            } else if (result instanceof Business) {
                existedBusiness = (Business) result;
                edtBusinessId.setText(existedBusiness.businessUserName);
                String description = existedBusiness.description;
                for(String hashtag:existedBusiness.hashtagList){
                    description += "#"+hashtag+" ";
                }
                edtDescription.setText(existedBusiness.description);
                edtName.setText(existedBusiness.name);
                setSpnCategory(existedBusiness.category);
                try {
                    imbProfilePicture.setImageBitmap(
                            Image_M.getBitmapFromString(existedBusiness.profilePicture));
                } catch (Exception e) {Log.e(TAG, "Error in bitmap string");}

                PassingBusiness.getInstance().setValue(existedBusiness);
            }
        } catch (Exception e) {
            Log.e(TAG, Params.CLOSED_BEFORE_RESPONSE);
        }

    }

    private void setSpnCategory(String category) {
        if (categoryList == null || categoryList.size() == 0)
            return;
        for (int i = 0; i < categoryList.size(); i++) {
            if (categoryList.get(i).name == category)
                spnCategory.setSelection(i);
        }
    }

    @Override
    public void getError(Integer errorCode) {
        pd.dismiss();
        try {
            String errorMessage = ServerAnswer.getError(getBaseContext(), errorCode);
            Dialogs.showMessage(context, errorMessage, false);
        } catch (Exception e) {
            Log.e(TAG, Params.CLOSED_BEFORE_RESPONSE);
        }
    }

    @Override
    public void getResult(String filePath) {
        rl_camera_section.setVisibility(View.GONE);
        profilePictureFilePath = filePath;
        displayCropedImage(profilePictureFilePath);
    }
}
