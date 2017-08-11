package com.haffa.rafal.pta;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.haffa.rafal.pta.ImageProcessing.TesseractMethods;
import com.haffa.rafal.pta.Utilities.PermissionCheck;

import java.io.File;

import static com.haffa.rafal.pta.ImageProcessing.TesseractMethods.SINGLE_IMAGE_PATH;

public class MainActivity extends AppCompatActivity {
    Uri mUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PermissionCheck permissionCheck = new PermissionCheck();
        permissionCheck.checkForPermissions(this);
        final TesseractMethods tesseractMethods = new TesseractMethods(this);
        Button btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                tesseractMethods.openCamera();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //permission granted, the application runs

                } else {

                    //permission denied, the application closes

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        this.finishAndRemoveTask();
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            this.finishAffinity();
                        }
                    }
                }
                return;
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1024) {
            if (resultCode == Activity.RESULT_OK) {
                final TesseractMethods tesseractMethods = new TesseractMethods(this);
                mUri = Uri.fromFile(new File(SINGLE_IMAGE_PATH));
                Log.v("SAY AYYY", "AYYY");

                tesseractMethods.copyTesseractData();
                tesseractMethods.doOCR(mUri);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Result canceled.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Activity result failed.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
