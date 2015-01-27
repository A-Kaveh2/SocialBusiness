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
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import ir.rasen.myapplication.classes.Post;
import ir.rasen.myapplication.helper.Functions;
import ir.rasen.myapplication.helper.Image_M;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.PassingPosts;
import ir.rasen.myapplication.helper.TextProcessor;
import ir.rasen.myapplication.ui.EditTextFont;
import ir.rasen.myapplication.ui.ImageViewSquare;
import ir.rasen.myapplication.ui.TextViewFont;
import ir.rasen.myapplication.webservice.WebserviceResponse;
import ir.rasen.myapplication.webservice.post.AddPost;
import ir.rasen.myapplication.webservice.post.UpdatePost;

public class ActivityNewPost_Step2 extends Activity implements WebserviceResponse {

    private EditTextFont name, description, price, code;
    private Context context;
    private String img;
    private boolean isEditing = false;
    private String businessId;
    private boolean canceled=true;

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
        if(!post.picture.equals(post.picture))
            ((ImageView) findViewById(R.id.img_new_post_step2_post)).setImageBitmap(Image_M.getBitmapFromString(post.picture));
        name.setText(post.title);
        description.setText(post.description);
        price.setText(post.price);
        code.setText(post.code);

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
        // next page
        // SET ON TEXT CHANGE LISTENERS (FOR ERRORS)
//        setOnTextChangeListeners();
        // CHECK INPUT DATA
        if (!name.getText().toString().matches(Params.USER_NAME_VALIDATION) || name.getText().length() < Params.USER_NAME_MIN_LENGTH) {
            name.requestFocus();
            name.setError(getString(R.string.enter_valid_name));
            return;
        }

        Post post = PassingPosts.getInstance().getValue().get(0);

        if (isEditing) {
            //update existing post
            post.title = name.getText().toString();
            post.price = price.getText().toString();
            post.code = code.getText().toString();
            post.description = description.getText().toString();
            post.hashtagList = TextProcessor.getHashtags(description.getText().toString());

            new UpdatePost(post, ActivityNewPost_Step2.this).execute();

        } else {
            Calendar calander = Calendar.getInstance();
            String day = String.valueOf(calander.get(Calendar.DAY_OF_MONTH));
            String month = String.valueOf(calander.get(Calendar.MONTH) + 1);
            String year = String.valueOf(calander.get(Calendar.YEAR));
            post.creationDate = day + "/" + month + "/" + year;
            post.title = name.getText().toString();
            post.price = price.getText().toString();
            post.code = code.getText().toString();

            //TODO where is hashtag list
            post.hashtagList = TextProcessor.getHashtags(description.getText().toString());

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
        canceled=true;
        onBackPressed();
    }

    @Override
    public void getResult(Object result) {
        if(canceled) return;
    }

    @Override
    public void getError(Integer errorCode) {
        if(canceled) return;
    }
}
