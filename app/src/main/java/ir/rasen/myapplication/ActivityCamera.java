package ir.rasen.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import ir.rasen.myapplication.helper.CropResult;
import ir.rasen.myapplication.helper.CustomeCamera;

public class ActivityCamera extends Activity implements CropResult {

    private static String TAG = "CameraActivity";
    FrameLayout cameraPreview;
    CustomeCamera customeCamera;
    LinearLayout ll_cover;
    ImageView picView;
    Button captureButton;
    public static String FILE_PATH = "file_path";
    public static Integer CAPTURE_PHOTO = 1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera);

        cameraPreview = (FrameLayout) findViewById(R.id.camera_preview);
        captureButton = (Button) findViewById(R.id.button_capture);
        ll_cover = (LinearLayout)findViewById(R.id.ll_camera_cover);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        int height = displaymetrics.heightPixels;
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(width, height-width);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE);
        ll_cover.setLayoutParams(lp);


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
    }

    @Override
    public void getResult(String filePath) {

        Intent i = getIntent();
        i.putExtra(ActivityCamera.FILE_PATH, filePath);
        setResult(RESULT_OK, i);
        finish();
    }
}