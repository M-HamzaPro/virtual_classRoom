package com.example.fypwebhost;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.UUID;

public class UploadingAssignment extends AppCompatActivity {

    String encodedfile, fileName, TAG = "INFO", path, fileLink = "https://temp321.000webhostapp.com/connect/files/", fileNameNew;
    int PICKFILE_REQUEST_CODE = 100;
    SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploading_assignment);
dataEntry();
        ///////////////////

//        fileNameNew = fileName.replace(" ","%20");

        ///////////////////
        Dexter.withContext(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/txt");
                intent.addCategory(Intent.CATEGORY_OPENABLE);

                try {
                    startActivityForResult(
                            Intent.createChooser(intent, "Select a File to Upload"),
                            PICKFILE_REQUEST_CODE);
                } catch (android.content.ActivityNotFoundException ex) {
                    // Potentially direct the user to the Market with a Dialog
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Please install a File Manager.", Snackbar.LENGTH_LONG);
                    snackbar.setTextColor(Color.parseColor("#ff0000"));
                    snackbar.show();
                }
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();




    }@RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICKFILE_REQUEST_CODE && data != null && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            path = new PathFromUri().getPathFromUri(UploadingAssignment.this, uri);

            fileName = new PathFromUri().getFileName(UploadingAssignment.this,uri);


            uploadFile();
            Log.i(TAG, "data -- " + data.toString());
            Log.i(TAG, "uri -- " + ((Uri) uri).toString());
            Log.i(TAG, "file/path -- " + path);
            Log.i(TAG, "file name -- " + fileName);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadFile() {
        try {

            new MultipartUploadRequest(UploadingAssignment.this, UUID.randomUUID().toString(), "https://temp321.000webhostapp.com/connect/uploadfile.php")
                    .addFileToUpload(path, "file")
                    .addParameter("fileName", fileName)
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(5)
                    .startUpload();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    private void dataEntry()
    {
//        fileNameNew = fileName.replace(" ","%20");
        prefs = getSharedPreferences("LogIn", MODE_PRIVATE);
        String loginEmail = prefs.getString("email", "No name defined");
        String userId = prefs.getString("userId", "");

        String assignmentID = getIntent().getStringExtra("assignmentID");
        String classID = getIntent().getStringExtra("classID");
//        fileNameNew = fileName.replace(" ","%20");
//        fileLink = fileLink+fileNameNew;

        Toast.makeText(this, "userId = "+userId, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "assignmentID = "+assignmentID, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "classID = "+classID, Toast.LENGTH_SHORT).show();

    }

}