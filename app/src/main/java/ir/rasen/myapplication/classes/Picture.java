package ir.rasen.myapplication.classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

import ir.rasen.myapplication.R;

/**
 * Created by android on 12/29/2014.
 */
public class Picture {

    public static String getBase64Image(Context context, String imageFileName) {
        Bitmap bm = BitmapFactory.decodeFile(
                Environment.getExternalStorageState() +
                        context.getString(R.string.image_file_path) +
                        imageFileName);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
    }
}
