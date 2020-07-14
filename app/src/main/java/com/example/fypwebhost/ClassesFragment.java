package com.example.fypwebhost;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ClassesFragment extends Fragment {


    ListView listView;
    MyAdapter adapter;
    ProgressBar progressBar;

    String userEmail, newSubject, newName , class_code, userIdOld, userName, userPassword;

    EditText editTextNewName, editTextNewSubject;


    public static String URL="https://temp321.000webhostapp.com/connect/getClass.php";

    public static ArrayList<Classes> classesArrayList = new ArrayList<>();


    public ClassesFragment(String email, String userIdOld, String userName, String userPassword) {

        this.userEmail = email;
        this.userIdOld = userIdOld;
        this.userName = userName;
        this.userPassword = userPassword;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.classes_fragment, container, false);
        listView = view.findViewById(R.id.mylistview);

        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        Sprite circle = new Circle();
        progressBar.setIndeterminateDrawable(circle);

        progressBar.setVisibility(View.INVISIBLE);

        retrieveData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Classes classes = classesArrayList.get(position);
                    Toast.makeText(getContext(), "Class_id"+classes.getClassCode(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(), CurrentClass.class);

                    intent.putExtra("Class_name", classes.getName());
                    intent.putExtra("Class_id", classes.getClassCode());

                    startActivity(intent);
            }
        });

       listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
           @Override
           public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {


               Classes classes = classesArrayList.get(position);
               class_code = classes.getClassCode(); // to use in sending data for update
               showUpdateDialog(class_code, classes.getName(), classes.getClassID());


               return true;
           }
       });

        return view;
    }



    public void retrieveData() {
        progressBar.setVisibility(View.VISIBLE);
        classesArrayList.clear();

        final String userId = userIdOld;


        StringRequest request = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            String sucess = jsonObject.getString("success");
                            JSONArray jsonArray=jsonObject.getJSONArray("data");

                            if(sucess.equals("1")){
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(getContext(), "1", Toast.LENGTH_SHORT).show();
                                for(int i=0; i< jsonArray.length(); i++){
                                    if(sucess.equals("1")){
                                        JSONObject object=jsonArray.getJSONObject(i);

                                        String classID = object.getString("classId");
                                        String classCode = object.getString("Class_id");
                                        String className = object.getString("class");
                                        String classSubject = object.getString("subject");

                                        classesArrayList.add(
                                                new Classes(classID, classCode,className ,classSubject)
                                        );
                                    }
                                    adapter=new MyAdapter(getContext() ,classesArrayList);
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
                params.put("classTeacher", userId);
                return params;
            }
        };


        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);
    }

    private void showUpdateDialog(final String classId, String className, final String classID)
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());

        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.update_dialog,null);

        dialogBuilder.setView(dialogView);

        editTextNewName = (EditText) dialogView.findViewById(R.id.editTextName);
        editTextNewSubject = (EditText) dialogView.findViewById(R.id.editTextSubject);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdate);
        final Button buttonRemove = (Button) dialogView.findViewById(R.id.buttonRemove);

        dialogBuilder.setTitle("Manage Class:  "+className +" "+ classId);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteClass(classID);
            }
        });
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    newName = editTextNewName.getText().toString().trim();
                    newSubject = editTextNewSubject.getText().toString().trim();
// || TextUtils.isEmpty(newSubject)

                    if (TextUtils.isEmpty(newName)) {
                        editTextNewName.setError("Name Required");
                        return;
                    }
                progressBar.setVisibility(View.VISIBLE);
                updateClass();
                    alertDialog.dismiss();
            }
        });


    }

    private void updateClass()
    {
        StringRequest request = new StringRequest(Request.Method.POST, "https://temp321.000webhostapp.com/connect/updateClass.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equalsIgnoreCase("Class Updated"))
                        {
                            Toast.makeText(getContext(), "Class Updated", Toast.LENGTH_SHORT).show();
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

                final String userId = userIdOld;


                params.put("Name", newName);
                params.put("class_teacher", String.valueOf(userId));
                params.put("class_id", class_code);
                params.put("class_subject", newSubject);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);
    }

    private void deleteClass(final String classId)
    {
            StringRequest request = new StringRequest(Request.Method.POST, "https://temp321.000webhostapp.com/connect/removeStudent.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response.equalsIgnoreCase("Student Removed"))
                            {
                                Toast.makeText(getActivity(), "Student Removed", Toast.LENGTH_SHORT).show();

                            }
                            else
                            {
                                Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String > params = new HashMap<String, String>();

                    params.put("classID", classId);

                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(request);


    }

}