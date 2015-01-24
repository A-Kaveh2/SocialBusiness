package ir.rasen.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import ir.rasen.myapplication.helper.CropResult;
import ir.rasen.myapplication.helper.CustomeCamera;


public class ActivityCamera extends Activity implements CropResult {

    private static String TAG = "CameraActivity";
    FrameLayout cameraPreview;
    CustomeCamera customeCamera;
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