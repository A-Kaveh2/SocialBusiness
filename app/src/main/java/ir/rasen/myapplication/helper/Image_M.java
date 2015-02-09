package ir.rasen.myapplication.helper;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Base64;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import ir.rasen.myapplication.ActivityCamera;
import ir.rasen.myapplication.ActivityGallery;
import ir.rasen.myapplication.R;

/**
 * Created by android on 1/20/2015.
 */
public class Image_M {

    public enum ImageSize {LARGE, MEDIUM, SMALL}

    public static int getImageSize(ImageSize imageSize) {
        switch (imageSize) {
            case LARGE:
                return 1;
            case MEDIUM:
                return 2;
            case SMALL:
                return 3;
        }
        return 3;
    }

    public static String getBase64String(String imageFilePath) {
        Bitmap bm = BitmapFactory.decodeFile(imageFilePath);
        int si = Image_M.sizeOf(bm);
        if (Image_M.sizeOf(bm) > 1000000) {
            BitmapFactory.Options ops = new BitmapFactory.Options();
            ops.inSampleSize = 4;
            bm = BitmapFactory.decodeFile(imageFilePath, ops);
        }
        si = Image_M.sizeOf(bm);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encodedImage.replace("\n", "");
    }

    public static Bitmap getBitmapFromString(String codedImage) {
        byte[] decodedString = Base64.decode(codedImage, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    public static void saveBitmap(String path, String imageName, Bitmap bitmap) {
        File checkDirectory = new File(path);
        if (!checkDirectory.exists())
            checkDirectory.mkdirs();

        File file = new File(path, imageName);
        file.mkdir();
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (Exception e) {
            String s = e.getMessage();
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    public static int sizeOf(Bitmap data) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {
            return data.getRowBytes() * data.getHeight();
        } else {
            return data.getByteCount();
        }
    }


}
