package ir.rasen.myapplication.helper;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * Created by android on 1/11/2015.
 */
public class CustomeCamera {

    Activity activity;
    private Camera mCamera;
    private CameraPreview mPreview;
    private static String TAG = "CameraActivity";

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;





    File pictureFile;

    public CropResult delegate = null;
    private int size, quality;
    private boolean autoFocusEnabled = false;


    public CustomeCamera(Activity activity, FrameLayout cameraPreviewLayout, Integer size, Integer quality) {
        this.activity = activity;
        this.size = size;
        this.quality = quality;

        //shape camera preview as square window
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(width, width);
        cameraPreviewLayout.setLayoutParams(lp);

        // Create an instance of Camera
        mCamera = getCameraInstance();
        try {
            mCamera.setDisplayOrientation(90);
        }
        catch (Exception e){
            Log.d(TAG,e.getMessage());
        }

        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_BACK, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break; //Natural orientation
            case Surface.ROTATION_90:
                degrees = 90;
                break; //Landscape left
            case Surface.ROTATION_180:
                degrees = 180;
                break;//Upside down
            case Surface.ROTATION_270:
                degrees = 270;
                break;//Landscape right
        }
        int rotate = (info.orientation - degrees + 360) % 360;

        //STEP #2: Set the 'rotation' parameter
        Camera.Parameters params = mCamera.getParameters();
        params.setRotation(rotate);

        List<String> focusModes = params.getSupportedFocusModes();
        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO))
        {
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        }
        if (params.getFocusMode() == Camera.Parameters.FOCUS_MODE_AUTO)
            autoFocusEnabled = true;
        else
            autoFocusEnabled = false;

        mCamera.setParameters(params);

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(activity, mCamera);
        cameraPreviewLayout.addView(mPreview);
    }


    private static File getOutputMediaFile() {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "SocialBusiness Temp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");;

        return mediaFile;
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            pictureFile = getOutputMediaFile();
            if (pictureFile == null) {
                Log.d(TAG, "Error creating media file, check storage permissions: ");
                return;
            }

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
                camera.release();
                cropAndResize();
            } catch (FileNotFoundException e) {
                Log.d(TAG, "File not found: " + e.getMessage());
            } catch (Exception e) {
                Log.d(TAG, "Error accessing file: " + e.getMessage());
            }
        }
    };

    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    /**
     * A safe way to get an instance of the Camera object.
     */
    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
            Log.d(TAG, "Camera is not available (in use or does not exist)");
        }
        return c; // returns null if camera is unavailable
    }

    public void capturePhoto() throws Exception {
        if (autoFocusEnabled) {
            mCamera.autoFocus(new Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean success, Camera camera) {
                    if (success)
                        mCamera.takePicture(null, null, mPicture);
                }
            });
        } else {
            mCamera.takePicture(null, null, mPicture);
        }
        //mCamera.takePicture(null, null, mPicture);
    }


    private void cropAndResize() throws Exception {

        if (pictureFile.exists()) {

            Bitmap bitmap = decodeSampledBitmapFromFile(pictureFile.getAbsolutePath(), size, size);

            int width = bitmap.getWidth();
            int height = bitmap.getHeight();

            int currentSize = (width > height) ? height : width;
            Bitmap croppedBitmap = crop(bitmap, currentSize);
            bitmap.recycle();
            Bitmap resizedBitmap = resize(croppedBitmap, size);

            Bitmap finalBitmap = null;
            //if picture is landscape
            if (width > height)
                finalBitmap = rotate(resizedBitmap, 90);
            else
                finalBitmap = resizedBitmap;


            pictureFile.delete();

            try {
                FileOutputStream out = new FileOutputStream(pictureFile);
                finalBitmap.compress(Bitmap.CompressFormat.JPEG, quality, out);
                out.flush();
                out.close();
                finalBitmap.recycle();
                resizedBitmap.recycle();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        delegate.getResult(pictureFile.getPath());
    }


    private Bitmap rotate(Bitmap bitmap, int degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        //Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        //return Bitmap.createScaledBitmap(rotatedBitmap, rotatedBitmap.getWidth() * 2, rotatedBitmap.getHeight() * 2, true);
    }

    private Bitmap crop(Bitmap bitmap, int size) {
        return Bitmap.createBitmap(bitmap, 0, 0, size, size);

    }

    private Bitmap resize(Bitmap bitmap, int newSize) {
        return Bitmap.createScaledBitmap(bitmap, newSize, newSize, true);
    }

    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.

            if (width > height) {//picture is landscape
                while ((halfHeight / inSampleSize) > reqHeight) {
                    inSampleSize *= 2;
                }
            } else {//picture is portrait
                while ((halfWidth / inSampleSize) > reqWidth) {
                    inSampleSize *= 2;
                }
            }
        }

        return inSampleSize;
    }

    private static Bitmap decodeSampledBitmapFromFile(String filePath,
                                                     int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        //BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }





}
