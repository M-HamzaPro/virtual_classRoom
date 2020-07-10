package com.example.fypwebhost;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
public class ClassWork extends Fragment {

     Button buttonCreateAssignment, buttonUpload, uploadAssignment;
     EditText editTextTitle, editTextDueDate, editTextPostDate;
     String assigTitle, assigDueDate, assigPostDate, classCode, userType, classID;

    ListView listView;
    AssignmentAdapter adapter;
//    ProgressBar progressBar;
    public static ArrayList<AssignmentModelClass> assignmentArrayList = new ArrayList<>();
    public static String URL="https://temp321.000webhostapp.com/connect/getAssignmentsInfo.php";
    String encodedfile, fileName, TAG = "INFO", path;
    int PICKFILE_REQUEST_CODE = 100;
    public ClassWork(String classCode, String userType, String classID)
    {
        this.classCode = classCode;
        this.userType = userType;
        this.classID = classID;
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.class_work, container, false);
        listView = view.findViewById(R.id.mylistview);


        uploadAssignment = view.findViewById(R.id.uploadAssignment);
        uploadAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Dexter.withContext(getContext()).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
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
                            Snackbar snackbar = Snackbar.make(v.findViewById(android.R.id.content), "Please install a File Manager.", Snackbar.LENGTH_LONG);
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

            }
        });




        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                final char type = studentEmail.charAt(0);


                AssignmentModelClass assignmentModelClass = assignmentArrayList.get(position);
                Intent intent = new Intent(getContext(), UploadingAssignment.class);
                intent.putExtra("assignmentID", assignmentModelClass.getAssignmentID());
                intent.putExtra("classID", classID);
                startActivity(intent);
            }
        });











//        buttonUpload = view.findViewById(R.id.buttonUpload);
//        buttonUpload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.setType("text/txt");
//                intent.addCategory(Intent.CATEGORY_OPENABLE);
//
//                try {
//                    startActivityForResult(
//                            Intent.createChooser(intent, "Select a File to Upload"),
//                            10);
//                } catch (android.content.ActivityNotFoundException ex) {
//                    // Potentially direct the user to the Market with a Dialog
//                    Snackbar snackbar = Snackbar.make(v.findViewById(android.R.id.content), "Please install a File Manager.", Snackbar.LENGTH_LONG);
//                    snackbar.setTextColor(Color.parseColor("#ff0000"));
//                    snackbar.show();
//                }
//            }
//        });
        buttonCreateAssignment = (Button) view.findViewById(R.id.buttonAddAssignment);

        if(userType.contains("0"))
        {
            buttonCreateAssignment.setVisibility(View.INVISIBLE);
        }

        buttonCreateAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showCreateDialog(classCode);
            }
        });

        retrieveAssignmentInfo();

        return view;
    }
    private void showCreateDialog(final String classId)
    {
    //    Toast.makeText(getContext(), "class code = "+classCode, Toast.LENGTH_SHORT).show();



        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());

        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.create_assig_dialog,null);

        dialogBuilder.setView(dialogView);

        editTextTitle = (EditText) dialogView.findViewById(R.id.editTextTitle);
        editTextDueDate = (EditText) dialogView.findViewById(R.id.editTextDueDate);
        editTextPostDate = (EditText) dialogView.findViewById(R.id.editTextPostDate);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonCreateAssignment);

     //   dialogBuilder.setTitle("Updating Class:  "+className +" "+ classId);
        dialogBuilder.setTitle("Creating New Assignment");

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                assigTitle = editTextTitle.getText().toString().trim();
                assigDueDate = editTextDueDate.getText().toString().trim();
                assigPostDate = editTextPostDate.getText().toString().trim();
// || TextUtils.isEmpty(newSubject)

                if (TextUtils.isEmpty(assigTitle)) {
                    editTextTitle.setError("Name Required");
                    return;
                }
//                progressBar.setVisibility(View.VISIBLE);
                createAssignment();
//                Toast.makeText(getContext(), "wow", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });


    }
    private void createAssignment()
    {
        final String title  = editTextTitle.getText().toString().trim();
        final String dueDate = editTextDueDate.getText().toString().trim();
        final String postDate = editTextPostDate.getText().toString().trim();

        if(title.isEmpty() || dueDate.isEmpty() || postDate.isEmpty())
        {
            Toast.makeText(getContext(), "fill all text" , Toast.LENGTH_SHORT).show();
        }
        else
        {


            StringRequest request = new StringRequest(Request.Method.POST, "https://temp321.000webhostapp.com/connect/createAssignment.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response.contains("Assignment Created success"))
                            {
                                Toast.makeText( getContext(), "Assignment Created", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String > params = new HashMap<String, String>();

                    params.put("title", title);
                    params.put("dueDate", dueDate);
                    params.put("postDate", postDate);
                    params.put("classCode", String.valueOf(classCode));

                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(request);
        }
    }
    public void retrieveAssignmentInfo()
    {

        assignmentArrayList.clear();


        StringRequest request = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray=jsonObject.getJSONArray("data");

                            if(success.equals("1")){
//                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(getContext(), "1", Toast.LENGTH_SHORT).show();
                                for(int i=0; i< jsonArray.length(); i++){
                                    if(success.equals("1")){
                                        JSONObject object=jsonArray.getJSONObject(i);
                                        String assignmentID = object.getString("assignmentID");
                                        String assignmentTitle = object.getString("assignmentTitle");
                                        String assignmentDueDate =object.getString("assignmentDueDate");
                                        String assignmentPostDate =object.getString("assignmentPostDate");

                                        assignmentArrayList.add(
                                                new AssignmentModelClass(assignmentTitle,assignmentDueDate ,assignmentPostDate, assignmentID)
                                        );
                                    }
                                    adapter=new AssignmentAdapter(getActivity() ,assignmentArrayList);
                                    adapter.notifyDataSetChanged();
                                    listView.setAdapter(adapter);
                                }
                            }
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("classCode", String.valueOf(classCode));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICKFILE_REQUEST_CODE && data != null && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            path = new PathFromUri().getPathFromUri(getContext(), uri);

            fileName = new PathFromUri().getFileName(getContext(),uri);
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
            new MultipartUploadRequest(getContext(), UUID.randomUUID().toString(), "https://temp321.000webhostapp.com/connect/uploadfile.php")
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

}