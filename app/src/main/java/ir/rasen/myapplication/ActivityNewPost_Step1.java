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

import java.io.File;
import java.util.ArrayList;

import ir.rasen.myapplication.classes.Post;
import ir.rasen.myapplication.helper.Dialogs;
import ir.rasen.myapplication.helper.Image_M;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.PassingPosts;
import ir.rasen.myapplication.helper.ServerAnswer;
import ir.rasen.myapplication.ui.ImageViewSquare;
import ir.rasen.myapplication.ui.TextViewFont;
import ir.rasen.myapplication.webservice.DownloadImages;
import ir.rasen.myapplication.webservice.WebserviceResponse;
import ir.rasen.myapplication.webservice.post.DeletePost;

public class ActivityNewPost_Step1 extends Activity {
    private String TAG = "ActivityNewPost_Step1";

    private boolean isEditing = false, picChoosed = false;
    private DownloadImages downloadImages;

    public static ActivityNewPost_Step1 step1;
    String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_post_step_1);

        step1 = this;
        downloadImages = new DownloadImages(this);

        // SET ANIMATIONS
        setAnimations();

        if (PassingPosts.getInstance().getValue() != null) {
            // TODO EDIT THIS POST
            if (PassingPosts.getInstance().getValue().get(0).pictureId != 0)
                downloadImages.download(PassingPosts.getInstance().getValue().get(0).pictureId, Image_M.getImageSize(Image_M.ImageSize.LARGE), (ImageViewSquare) findViewById(R.id.btn_post_picture_set));
                /*((ImageViewSquare) findViewById(R.id.btn_post_picture_set))
                    .setImageBitmap(Image_M.getBitmapFromString(PassingPosts.getInstance().getValue().get(0).picture));*/
            isEditing = true;
        } else {
            ArrayList<Post> post = new ArrayList<>();
            Post postTemp = new Post();
            postTemp.businessID = getIntent().getIntExtra(Params.BUSINESS_ID, 0);
            post.add(postTemp);
            PassingPosts.getInstance().setValue(post);
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
                filePath = data.getStringExtra(ActivityCamera.FILE_PATH);
                displayCropedImage(filePath);
                picChoosed = true;
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
                ((ImageViewSquare) findViewById(R.id.btn_post_picture_set))
                        .setImageBitmap(myBitmap);
                String s = Image_M.getBase64String(filePath);
                int i = s.length();
                PassingPosts.getInstance().getValue().get(0).picture = s;
            } catch (Exception e) {
                String s = e.getMessage();
            }
        }
    }

    // SUBMIT TOUCHED
    public void submit(View view) {
        // next step
        if (!isEditing && !picChoosed) {
            Dialogs.showMessage(ActivityNewPost_Step1.this, getString(R.string.err_set_picture), false);
            return;
        }

        Intent intent = new Intent(getBaseContext(), ActivityNewPost_Step2.class);
        intent.putExtra(Params.EDIT_MODE, isEditing);
        if (filePath != null)
            intent.putExtra(Params.FILE_PATH, filePath);
        else
            intent.putExtra(Params.FILE_PATH, "null");
        intent.putExtra(Params.BUSINESS_ID, getIntent().getIntExtra(Params.BUSINESS_ID, 0));
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
        findViewById(R.id.btn_post_submit).startAnimation(fadeIn);
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
        findViewById(R.id.btn_post_help).startAnimation(animationSetBtn);
    }

    public void onBackPressed() {
        PassingPosts.getInstance().setValue(null);
        finish();
        overridePendingTransition(R.anim.to_0_from_left, R.anim.to_right);
    }


    public void back(View v) {
        onBackPressed();
    }


}
