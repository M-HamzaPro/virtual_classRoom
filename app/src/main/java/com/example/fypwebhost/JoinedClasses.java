package com.example.fypwebhost;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

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

public class JoinedClasses extends Fragment {


    ListView listView;
    JoinedClassesAdapter adapter;
    ProgressBar progressBar;
    String userEmail, userIdOld, userName, userPassword;;

    public static ArrayList<JoinedClassesModel> joinedClassesArrayList = new ArrayList<>();

    public JoinedClasses(String email, String userIdOld, String userName, String userPassword) {

        this.userEmail = email;
        this.userIdOld = userIdOld;
        this.userName = userName;
        this.userPassword = userPassword;
    }


    public static String URL="https://temp321.000webhostapp.com/connect/getJoinedClass.php";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.joined_class_frgment, container, false);
        listView = view.findViewById(R.id.mylistview);

//        //Toast.makeText(getContext()," email = "+studentEmail, Toast.LENGTH_SHORT).show();
//        studentEmail = studentEmail.substring(3);
        //Toast.makeText(getContext()," email == "+userEmail, Toast.LENGTH_SHORT).show();
        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        Sprite circle = new Circle();
        progressBar.setIndeterminateDrawable(circle);
        retrieveData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                final char type = studentEmail.charAt(0);


                JoinedClassesModel joinedClassesModel = joinedClassesArrayList.get(position);
                //Toast.makeText(getContext(), "Class_id"+joinedClassesModel.getId(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), CurrentClass.class);

                intent.putExtra("Class_name", joinedClassesModel.getName());
                intent.putExtra("Class_id", joinedClassesModel.getId());
                intent.putExtra("classID", joinedClassesModel.getClassID());
                //intent.putExtra("userType", type);
                startActivity(intent);
            }
        });

        return view;
    }


    public void retrieveData() {
        //Toast.makeText(getContext(), "doing it", Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.INVISIBLE);
        joinedClassesArrayList.clear();

//        final String[] data = userEmail.split(",");
//        //Toast.makeText(getContext(), "id check"+data[1], Toast.LENGTH_SHORT).show();
        final String userId = userIdOld;

//        final String teacherName = studentEmail;
//        final char type = teacherName.charAt(0);
//        //Toast.makeText(getContext(),"charAt"+type, Toast.LENGTH_SHORT).show();
//
////        final char userId = teacherName.charAt(1);
//        final String userId = teacherName.substring(1,3);
//
////        //Toast.makeText(getContext(),"subString"+userId, Toast.LENGTH_SHORT).show();
//        final String mail = teacherName.substring(2);
//        //Toast.makeText(getContext(), "doing it " +userId+" ok", Toast.LENGTH_SHORT).show();


        StringRequest request = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                           // //Toast.makeText(getActivity(), "doing it 1", Toast.LENGTH_SHORT).show();
                            JSONObject jsonObject=new JSONObject(response);
                            String sucess = jsonObject.getString("success");
                            JSONArray jsonArray=jsonObject.getJSONArray("data");
                           // //Toast.makeText(getContext(), sucess, Toast.LENGTH_SHORT).show();
                            if(sucess.contains("1")){

                                progressBar.setVisibility(View.INVISIBLE);
                                //Toast.makeText(getContext(), "1", Toast.LENGTH_SHORT).show();
                                for(int i=0; i< jsonArray.length(); i++){
                                    if(sucess.equals("1")){
                                        JSONObject object=jsonArray.getJSONObject(i);
                                        String classID = object.getString("classID");
                                        String classCode = object.getString("classCode");
                                        String className=object.getString("class");
                                        String classSubject=object.getString("subject");
                                        joinedClassesArrayList.add(
                                                new JoinedClassesModel(classCode, className, classSubject, classID)
                                        );
                                    }
                                    adapter = new JoinedClassesAdapter(getContext() ,joinedClassesArrayList);
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
                //Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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
}
