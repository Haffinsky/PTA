package com.haffa.rafal.pta.ImageProcessing;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.googlecode.tesseract.android.TessBaseAPI;
import com.haffa.rafal.pta.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static com.haffa.rafal.pta.Utilities.RetriveMyApplicationContext.getAppContext;

/**
 * Created by Rafal on 8/10/2017.
 */

public class TesseractMethods {


    private static final String TAG = TesseractMethods.class.getSimpleName();
    //string use to create external directory to store images
    public static final String TARGET_DIRECTORY_PATH = Environment
            .getExternalStorageDirectory()
            .toString()
            + "/tessdata";

    public static final String GENERAL_DIRECTORY_PATH = Environment
            .getExternalStorageDirectory()
            .toString();

    public static final String IMAGES_PATH = GENERAL_DIRECTORY_PATH + "/tessimages";

    public static final String SINGLE_IMAGE_PATH = IMAGES_PATH + "/ocr.jpg";
    /*
    files used to recognize languages
    will be place in tessdata directory
    more info can be found here:
    https://github.com/rmtheis/tess-two
     */
    public static final String LANG_FILES_DATA = "/tessdata";

    private TextView mTextView;
    private TessBaseAPI mTessBaseAPI;
    public Uri mUri;
    private Intent snapshotIntent;
    Activity activity;

    public TesseractMethods(Activity activity){
        this.activity = activity;
    }

    /*
    this activity initiates the camera
    and creates the directory for image files
    */
    public void openCamera(){
        File targetDirectory = new File(IMAGES_PATH);

        if(!targetDirectory.exists()) {
            targetDirectory.mkdir();
        }

        mUri = Uri.fromFile(new File(SINGLE_IMAGE_PATH));
        snapshotIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        snapshotIntent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
        if(snapshotIntent.resolveActivity(getAppContext().getPackageManager() ) != null){
            activity.startActivityForResult(snapshotIntent, 1024);
        }
    }
    public void copyTesseractData(){

        String fileList[] = new String[0];
        try {
            fileList = getAppContext().getAssets().list("");

        for(String fileName : fileList){
            String pathToDataFile = TARGET_DIRECTORY_PATH + "/" + fileName;

            if(!(new File(pathToDataFile)).exists()){

                InputStream in = getAppContext().getAssets().open(fileName);
                OutputStream out = new FileOutputStream(pathToDataFile);
                byte [] buff = new byte[1024];
                int length;
                while(( length = in.read(buff)) > 0){
                    out.write(buff, 0 ,length);
                }
                in.close();
                out.close();
            }
        }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void doOCR(Uri imageUri){
        try{
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 7;
            Bitmap bitmap = BitmapFactory.decodeFile(imageUri.getPath(), options);

            Log.v("IMAGE PATH", imageUri.getPath().toString());

            String result = this.getText(bitmap);
            mTextView = (TextView) activity.findViewById(R.id.output_text);
            mTextView.setText(result);
        }catch (Exception e){
            Log.e(TAG, e.getMessage());
        }
    }
    private String getText(Bitmap bitmap){
        try{
            mTessBaseAPI = new TessBaseAPI();
        }catch (Exception e){
            Log.e(TAG, e.getMessage());
        }
        mTessBaseAPI.init(GENERAL_DIRECTORY_PATH, "eng");
        mTessBaseAPI.setImage(bitmap);
        String retStr = "No result";
        try{
            retStr = mTessBaseAPI.getUTF8Text();
        }catch (Exception e){
            Log.e(TAG, e.getMessage());
        }
        mTessBaseAPI.end();
        return retStr;
    }

}
