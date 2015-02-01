package ir.rasen.myapplication;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import eu.janmuller.android.simplecropimage.CropImage;

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
                String path = data.getStringExtra(CropImage.IMAGE_PATH);

                // if nothing received
                if (path == null) {
                    Intent i = getIntent();
                    i.putExtra(ActivityGallery.FILE_PATH, path);
                    setResult(RESULT_CANCELED, i);
                    finish();
                    return;
                }

                Intent i = getIntent();
                i.putExtra(ActivityGallery.FILE_PATH, path);
                setResult(RESULT_OK, i);
                finish();

            }
        } else finish();

    }

    private void performCrop(Uri uri) {
        try {
            // create explicit intent
            Intent intent = new Intent(this, CropImage.class);

            // tell CropImage activity to look for image to crop
            String filePath = getRealPathFromURI(uri);
            intent.putExtra(CropImage.IMAGE_PATH, filePath);

            // allow CropImage activity to rescale image
            intent.putExtra(CropImage.SCALE, true);

            // if the aspect ratio is fixed to ratio 1/1
            intent.putExtra(CropImage.ASPECT_X, 1);
            intent.putExtra(CropImage.ASPECT_Y, 1);

            // start activity CropImage with certain request code and listen
            // for result
            startActivityForResult(intent, PIC_CROP);

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

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }
}