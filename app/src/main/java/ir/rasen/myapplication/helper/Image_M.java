package ir.rasen.myapplication.helper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.ArrayAdapter;

import java.io.ByteArrayOutputStream;

import ir.rasen.myapplication.ActivityCamera;
import ir.rasen.myapplication.ActivityGallery;
import ir.rasen.myapplication.R;

/**
 * Created by android on 1/20/2015.
 */
public class Image_M {

    public static String getBase64String(String imageFilePath, int imageQuality) {
        Bitmap bm = BitmapFactory.decodeFile(imageFilePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, imageQuality, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encodedImage.replace("\n","");
    }


}
