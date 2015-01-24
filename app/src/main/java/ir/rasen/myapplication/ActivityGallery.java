package ir.rasen.myapplication;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ActivityGallery extends Activity  {

    private static String TAG = "GalleryActivity";
    final int GALLERY_CAPTURE = 3;
    final int PIC_CROP = 4;


    private int size;
    private int quality;

    public static String FILE_PATH = "file_path";
    public static Integer CAPTURE_GALLERY =2;
    public static String SIZE ="size";
    public static String QUALITY ="quality";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        size = getResources().getInteger(R.integer.image_size);
        quality = getResources().getInteger(R.integer.image_quality);
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        try {
            intent.putExtra("return-data", true);
            startActivityForResult(intent, GALLERY_CAPTURE);
        } catch (ActivityNotFoundException e) {
            Log.d(TAG, e.getMessage());

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == GALLERY_CAPTURE) {
                performCrop(data.getData());
            } else if (requestCode == PIC_CROP) {
                Bundle extras = data.getExtras();

                Bitmap bitmap = extras.getParcelable("data");
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, size, size, true);
                File file = getOutputMediaFile();
                FileOutputStream out = null;

                try {
                    out = new FileOutputStream(file);
                    resizedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, out);
                    out.flush();
                    out.close();
                } catch (Exception e) {

                }

                Intent i = getIntent();
                i.putExtra(ActivityGallery.FILE_PATH, file.getAbsolutePath());
                setResult(RESULT_OK, i);
                finish();

            }
        }

    }

    private void performCrop(Uri uri) {
        try {
            //call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(uri, "image");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            //indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            //retrieve data on return
            cropIntent.putExtra("return-data", true);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        } catch (ActivityNotFoundException anfe) {
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }


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
                "IMG_" + timeStamp + ".jpg");
        ;

        return mediaFile;
    }

}