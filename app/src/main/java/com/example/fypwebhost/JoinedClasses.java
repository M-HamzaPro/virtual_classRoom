package com.example.fypwebhost;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class JoinedClasses extends Fragment {


    ListView listView;
    JoinedClassesAdapter adapter;
    ProgressBar progressBar;
    String studentEmail;

    public static ArrayList<JoinedClassesModel> joinedClassesArrayList = new ArrayList<>();

    public JoinedClasses(String email) {
        this.studentEmail = email;
    }


    public static String URL="https://temp321.000webhostapp.com/connect/getJoinedClass.php";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.joined_class_frgment, container, false);
        listView = view.findViewById(R.id.mylistview);

        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        Sprite circle = new Circle();
        progressBar.setIndeterminateDrawable(circle);
        retrieveData();
        return view;
    }







    public void retrieveData() {
        Toast.makeText(getContext(), "doing it", Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.INVISIBLE);
        joinedClassesArrayList.clear();
        final String teacherName = studentEmail;
        final char type = teacherName.charAt(0);
        final char userId = teacherName.charAt(1);
        final String mail = teacherName.substring(2);
        Toast.makeText(getContext(), "doing it " +userId+" ok", Toast.LENGTH_SHORT).show();
        StringRequest request = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Toast.makeText(getContext(), "doing it 1", Toast.LENGTH_SHORT).show();
                            JSONObject jsonObject=new JSONObject(response);
                            String sucess = jsonObject.getString("success");
                            JSONArray jsonArray=jsonObject.getJSONArray("data");
                            Toast.makeText(getContext(), sucess, Toast.LENGTH_SHORT).show();
                            if(sucess.contains("1")){

                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(getContext(), "1", Toast.LENGTH_SHORT).show();
                                for(int i=0; i< jsonArray.length(); i++){
                                    if(sucess.equals("1")){
                                        JSONObject object=jsonArray.getJSONObject(i);
                                        String Class_id = object.getString("Class_id");
                                        String Class=object.getString("class");
                                        String Subject=object.getString("subject");
                                        joinedClassesArrayList.add(
                                                new JoinedClassesModel(Class_id, Class, Subject)
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
}
