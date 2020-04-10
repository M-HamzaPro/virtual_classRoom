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

    String teacherEmail, newSubject, newName , class_code;

    EditText editTextNewName, editTextNewSubject;
    ProgressBar progressBar;

    public static String URL="https://temp321.000webhostapp.com/connect/getClass.php";

    public static ArrayList<Classes> classesArrayList = new ArrayList<>();



    public ClassesFragment(String email) {
        this.teacherEmail = email;
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

                    Intent intent = new Intent(getContext(), CurrentClass.class);
                    Classes classes = classesArrayList.get(position);
                    intent.putExtra("Class_name", classes.getName());
                    intent.putExtra("Class_id", classes.getId());

                    startActivity(intent);
            }
        });

       listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
           @Override
           public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {


               Classes classes = classesArrayList.get(position);
               class_code = classes.getId(); // to use in sending data for update
               showUpdateDialog(class_code, classes.getName());


               return true;
           }
       });

        return view;
    }



    public void retrieveData() {
        progressBar.setVisibility(View.VISIBLE);
        classesArrayList.clear();
        final String teacherName = teacherEmail;
        final char type = teacherName.charAt(0);
        final char userId = teacherName.charAt(1);
        final String mail = teacherName.substring(2);

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
                                        String Class_id = object.getString("Class_id");
                                        String Class=object.getString("class");
                                        String Subject=object.getString("subject");

                                        classesArrayList.add(
                                                new Classes(Class_id,Class ,Subject)
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
                params.put("classTeacher", String.valueOf(userId));
                return params;
            }
        };


        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);
    }

    private void showUpdateDialog(final String classId, String className)
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());

        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.update_dialog,null);

        dialogBuilder.setView(dialogView);

        editTextNewName = (EditText) dialogView.findViewById(R.id.editTextName);
        editTextNewSubject = (EditText) dialogView.findViewById(R.id.editTextSubject);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdate);

        dialogBuilder.setTitle("Updating Class:  "+className +" "+ classId);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

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

                final char userId = teacherEmail.charAt(1);


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


}