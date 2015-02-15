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
import ir.rasen.myapplication.helper.InnerFragment;
import ir.rasen.myapplication.helper.LoginInfo;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.PassingActiveRole;
import ir.rasen.myapplication.helper.PassingBusiness;
import ir.rasen.myapplication.helper.PassingPosts;
import ir.rasen.myapplication.helper.ResultStatus;
import ir.rasen.myapplication.helper.ServerAnswer;
import ir.rasen.myapplication.helper.TextProcessor;
import ir.rasen.myapplication.ui.EditTextFont;
import ir.rasen.myapplication.ui.ImageViewSquare;
import ir.rasen.myapplication.ui.ProgressDialogCustom;
import ir.rasen.myapplication.ui.TextViewFont;
import ir.rasen.myapplication.webservice.DownloadImages;
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
    String filePath;
    private ImageView imgPost;

    private ProgressDialogCustom pd;
    private DownloadImages downloadImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_post_step_2);
        context = this;
        pd = new ProgressDialogCustom(context);
        downloadImages = new DownloadImages(this);
        // SET VALUES
        name = ((EditTextFont) findViewById(R.id.edt_post_name));
        description = ((EditTextFont) findViewById(R.id.edt_post_text));
        price = ((EditTextFont) findViewById(R.id.edt_post_price));
        code = ((EditTextFont) findViewById(R.id.edt_post_code));
        imgPost = (ImageView)findViewById(R.id.img_new_post_step2_post);
        // SET ANIMATIONS
        setAnimations();

        // check if we are in edit mode or not
        if (getIntent().getBooleanExtra(Params.EDIT_MODE, false)) {
            isEditing = true;
        }

        filePath = getIntent().getStringExtra(Params.FILE_PATH);

        Post post = PassingPosts.getInstance().getValue().get(0);
        // TODO :: SET PICTURE
        //if (!post.picture.equals(post.picture))
        //    downloadImages.

        if(!filePath.equals("null"))
            //user choose new picture in step 1
            imgPost.setImageBitmap(Image_M.readBitmapFromStorate(filePath));
        else if(post.pictureId != 0)
            downloadImages.download(post.pictureId,Image_M.getImageSize(Image_M.ImageSize.LARGE),imgPost);


        name.setText(post.title);
        description.setText(post.description);
        price.setText(post.price);
        code.setText(post.code);




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
                TextProcessor.processEdtHashtags(description.getText().toString(), description, ActivityNewPost_Step2.this);
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

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
            new AddPost(post, ActivityNewPost_Step2.this).execute();
        }
        pd.show();

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
        pd.dismiss();
        if (result instanceof Post) {
            //result of executing AddPost
            Dialogs.showMessage(context, context.getResources().getString(R.string.dialog_update_success));



            //TODO force close here
            ActivityMain.activityMain.onBackPressed();
            InnerFragment innerFragment = new InnerFragment(ActivityMain.activityMain);
            innerFragment.newProfile(context, Params.ProfileType.PROFILE_USER, true, getIntent().getIntExtra(Params.BUSINESS_ID, 0));
            ActivityNewPost_Step1.step1.finish();
            finish();
        }
        else if(result instanceof ResultStatus){
            //result of executing UpdatePost
            Image_M.deletePictureById(context,PassingPosts.getInstance().getValue().get(0).pictureId);
        }
    }

    @Override
    public void getError(Integer errorCode) {
        pd.dismiss();
        try {
            String errorMessage = ServerAnswer.getError(getBaseContext(), errorCode);
            Dialogs.showMessage(ActivityNewPost_Step2.this, errorMessage);
        } catch (Exception e) {
            Log.e(TAG, Params.CLOSED_BEFORE_RESPONSE);
        }
    }

    public void saveInPassing() {

        Post post = PassingPosts.getInstance().getValue().get(0);


        if (isEditing) {
            //update existing post
            post.title = name.getText().toString();
            post.price = price.getText().toString();
            post.code = code.getText().toString();
            post.description = description.getText().toString();
            post.hashtagList = TextProcessor.getHashtags(post.description);

            if(!filePath.equals("null"))
                post.picture = Image_M.getBase64String(filePath);

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