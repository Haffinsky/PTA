package com.haffa.rafal.pta.Utilities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.haffa.rafal.pta.MainActivity;

import static com.haffa.rafal.pta.Utilities.RetriveMyApplicationContext.getAppContext;

/**
 * Created by Rafal on 8/10/2017.
 */

public class PermissionCheck {

    public  static final int PERMISSIONS_MULTIPLE_REQUEST = 123;
    private Activity activity;

    public PermissionCheck(){
    }

    public void checkForPermissions(Activity activity){


        this.activity = activity;
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.CAMERA) +
                ContextCompat.checkSelfPermission(activity,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) +
                ContextCompat.checkSelfPermission(activity,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.CAMERA) ||
                    (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) || (
                            ActivityCompat.shouldShowRequestPermissionRationale(activity,
                                    Manifest.permission.READ_EXTERNAL_STORAGE))))  {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.CAMERA,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSIONS_MULTIPLE_REQUEST);


            }
        }
    }
}
