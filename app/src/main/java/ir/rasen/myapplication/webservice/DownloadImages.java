package ir.rasen.myapplication.webservice;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

import ir.rasen.myapplication.R;
import ir.rasen.myapplication.helper.Image_M;
import ir.rasen.myapplication.helper.LoginInfo;
import ir.rasen.myapplication.helper.Params;
import ir.rasen.myapplication.helper.ResultStatus;
import ir.rasen.myapplication.helper.ServerAnswer;
import ir.rasen.myapplication.helper.URLs;

/**
 * Created by android on 1/31/2015.
 */
public class DownloadImages {

    //this class download images with thread pool and cache them after download.


    //key: image name
    //value: image bitmap
    Hashtable<Integer, Bitmap> images = new Hashtable<>();

    private boolean isDownloadStarted;
    private String storagePath;
    private Context context;
    private ArrayList<DownloadImage> downloadQueue;

    public DownloadImages(Context context) {
        isDownloadStarted = false;
        this.context = context;
        storagePath = Environment.getExternalStorageDirectory().getAbsolutePath() +
                context.getResources().getString(R.string.download_storage_path);
        downloadQueue = new ArrayList<>();

    }

    public void download(int imageID, int imageSize, ImageView imageView) {
        //imageSize: 1=large, 2=medium, 3= small

        //if image is already used
        if (images.containsKey(imageID)) {
            imageView.setImageBitmap(images.get(imageID));
            return;
        }

        if (isImageInStorage(imageID, imageSize)) {
            Bitmap bitmap = BitmapFactory.decodeFile(storagePath + imageID+"_"+imageSize);
            images.put(imageID, bitmap);
            imageView.setImageBitmap(bitmap);
            return;
        }

        if (!isDownloadStarted) {
            isDownloadStarted = true;
            downloadQueue.add(new DownloadImage(imageID, imageSize, imageView));
            new DownloadImageThread(context).execute();
        } else {
            downloadQueue.add(new DownloadImage(imageID, imageSize, imageView));
        }


    }

    private boolean isImageInStorage(int imageID, int imageSize) {
        File file = new File(storagePath, String.valueOf(imageID) + "_" + String.valueOf(imageSize)+".jpg");
        if (file.exists())
            return true;
        return false;
    }

    private class DownloadImage {
        int imageID;
        int imageSize;
        ImageView imageView;

        public DownloadImage(int imageID, int imageSize, ImageView imageView) {
            this.imageID = imageID;
            this.imageSize = imageSize;
            this.imageView = imageView;
        }
    }

    private class DownloadImageThread extends AsyncTask<Void, Void, String> {
        private static final String TAG = "DownloadImage";
        private ServerAnswer serverAnswer;
        private Context context;

        public DownloadImageThread(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(Void... voids) {
            if (downloadQueue.size() == 0) {
                isDownloadStarted = false;
                return null;
            }

            WebserviceGET webserviceGET = new WebserviceGET(URLs.DOWNLOAD_IMAGE, new ArrayList<>(
                    Arrays.asList(String.valueOf(downloadQueue.get(0).imageID),
                            String.valueOf(downloadQueue.get(0).imageSize))));
            try {
                serverAnswer = webserviceGET.execute();
                if (serverAnswer.getSuccessStatus()) {
                    JSONObject jsonObject = serverAnswer.getResult();
                    if (jsonObject != null) {
                        return jsonObject.getString(Params.IMAGE);
                    }
                } else
                    return null;
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (!isDownloadStarted)
                return;

            if (result == null) {
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.no_image);
                images.put(downloadQueue.get(0).imageID, bitmap);
                downloadQueue.get(0).imageView.setImageBitmap(bitmap);
            } else {
                Bitmap bitmap = Image_M.getBitmapFromString(result);
                images.put(downloadQueue.get(0).imageID, bitmap);
                downloadQueue.get(0).imageView.setImageBitmap(bitmap);
                Image_M.saveBitmap(storagePath, downloadQueue.get(0).imageID + "_" + downloadQueue.get(0).imageSize+".jpg", bitmap);
            }

            downloadQueue.remove(0);

            new DownloadImageThread(context).execute();

        }
    }
}
