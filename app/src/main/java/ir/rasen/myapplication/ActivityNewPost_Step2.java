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
import android.widget.ImageView;

import org.apache.http.cookie.SM;

import java.io.File;
import java.util.Calendar;

import ir.rasen.myapplication.classes.Post;
import ir.rasen.myapplication.helper.Dialogs;
import ir.rasen.myapplication.helper.Image_M;
import ir.rasen.myapplication.helper.LoginInfo;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.PassingPosts;
import ir.rasen.myapplication.helper.ResultStatus;
import ir.rasen.myapplication.helper.ServerAnswer;
import ir.rasen.myapplication.helper.TextProcessor;
import ir.rasen.myapplication.ui.EditTextFont;
import ir.rasen.myapplication.ui.ImageViewSquare;
import ir.rasen.myapplication.ui.TextViewFont;
import ir.rasen.myapplication.webservice.WebserviceResponse;
import ir.rasen.myapplication.webservice.post.AddPost;
import ir.rasen.myapplication.webservice.post.DeletePost;
import ir.rasen.myapplication.webservice.post.UpdatePost;
import ir.rasen.myapplication.webservice.user.RequestConfirmation;

public class ActivityNewPost_Step2 extends Activity implements WebserviceResponse {
    private String TAG = "ActivityNewPost_Step2";

    private EditTextFont name, description, price, code;
    private Context context;
    private String img;
    private boolean isEditing = false;
    private String businessId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_post_step_2);
        context = this;

        // SET VALUES
        name = ((EditTextFont) findViewById(R.id.edt_post_name));
        description = ((EditTextFont) findViewById(R.id.edt_post_text));
        price = ((EditTextFont) findViewById(R.id.edt_post_price));
        code = ((EditTextFont) findViewById(R.id.edt_post_code));

        // SET ANIMATIONS
        setAnimations();

        // check if we are in edit mode or not
        if (getIntent().getBooleanExtra(Params.EDIT_MODE, false)) {
            isEditing = true;
        }

        Post post = PassingPosts.getInstance().getValue().get(0);
        if (!post.picture.equals(post.picture))
            ((ImageView) findViewById(R.id.img_new_post_step2_post)).setImageBitmap(Image_M.getBitmapFromString(post.picture));
        name.setText(post.title);
        description.setText(post.description);
        price.setText(post.price);
        code.setText(post.code);

        Dialogs dialogs = new Dialogs();
        dialogs.showPostDeletePopup(this, 1, 1, this);


        description.addTextChangedListener(new TextWatcher() {
            String oldText;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                oldText = charSequence.toString();
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if(charSequence.toString().equals(oldText))
                    return;
                TextProcessor textProcessor = new TextProcessor(context);
                textProcessor.processEdtHashtags(description.getText().toString(), description);
            }
            @Override
            public void afterTextChanged(Editable editable) {
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
                String filePath = data.getStringExtra(ActivityGallery.FILE_PATH);
                displayCropedImage(filePath);
            }
        }

    }

    private void displayCropedImage(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            try {
                ((ImageViewSquare) findViewById(R.id.btn_post_picture_set))
                        .setImageBitmap(myBitmap);
                PassingPosts.getInstance().getValue().get(0).picture = Image_M.getBase64String(filePath);
            } catch (Exception e) {
                String s = e.getMessage();
            }
        }
    }

    // SUBMIT TOUCHED
    public void submit(View view) {

        saveInPassing();
        Post post = PassingPosts.getInstance().getValue().get(0);

        if (!name.getText().toString().matches(Params.USER_NAME_VALIDATION) || name.getText().length() < Params.USER_NAME_MIN_LENGTH) {
            name.requestFocus();
            name.setErrorC(getString(R.string.enter_valid_name));
            return;
        }

        if(!post.price.matches(Params.NUMERIC_VALIDATION)) {
            price.requestFocus();
            price.setErrorC(getString(R.string.enter_valid_price));
            return;
        }

        if (isEditing) {
            new UpdatePost(post, ActivityNewPost_Step2.this).execute();
        } else {
            //TODO assing business.id to the post
            //for the test
            post.businessID = 1004;
            new AddPost(post, ActivityNewPost_Step2.this).execute();
        }

    }

    // HELP TOUCHED
    public void help(View view) {
        // TODO HELP NOW!
        // .....
    }

    public void setAnimations() {
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in_normal);
        ((Button) findViewById(R.id.btn_post_submit)).startAnimation(fadeIn);
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
        ((TextViewFont) findViewById(R.id.btn_post_help)).startAnimation(animationSetBtn);
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
        if (result instanceof Post) {
            //TODO display success message
        }
    }

    @Override
    public void getError(Integer errorCode) {
        try {
            String errorMessage = ServerAnswer.getError(getBaseContext(), errorCode);
            Dialogs.showMessage(ActivityNewPost_Step2.this, errorMessage);
        } catch (Exception e) {
            Log.e(TAG, Params.CLOSED_BEFORE_RESPONSE);
        }
    }

    public void saveInPassing() {

        Post post = PassingPosts.getInstance().getValue().get(0);

        //TODO for the test
        isEditing = true;
        post.id = 2;

        if (isEditing) {
            //update existing post
            post.title = name.getText().toString();
            post.price = price.getText().toString();
            post.code = code.getText().toString();
            post.description = description.getText().toString();
            post.hashtagList = TextProcessor.getHashtags(post.description);

        } else {
            Calendar calander = Calendar.getInstance();
            String day = String.valueOf(calander.get(Calendar.DAY_OF_MONTH));
            String month = String.valueOf(calander.get(Calendar.MONTH) + 1);
            String year = String.valueOf(calander.get(Calendar.YEAR));
            post.creationDate = day + "/" + month + "/" + year;
            post.title = name.getText().toString();
            post.price = price.getText().toString();
            post.code = code.getText().toString();

            post.description = description.getText().toString();
            post.description = post.description.replace("\n", " ");
            post.hashtagList = TextProcessor.getHashtags(post.description);
        }
    }

}