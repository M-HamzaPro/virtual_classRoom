package com.example.fypwebhost;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {

    ListView listView;
    MyAdapterUser adapter;
    Button buttonLogout;
    SharedPreferences prefs;
    String teacherEmail ;
    private SharedPreferences.Editor mEditor;
    public static String URL="https://temp321.000webhostapp.com/connect/getProfile.php";

    public static ArrayList<UserModelClass> arrayListUser = new ArrayList<UserModelClass>();



    public HomeFragment(String email) {
        this.teacherEmail = email;
       }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        listView = view.findViewById(R.id.mylistview);


        retrieveData();

        buttonLogout = view.findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("LogIn", Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();

               // teacherEmail = null;
                Intent intent = new Intent(getContext(), login.class);
                startActivity(intent);
            }
        });
        return view;
    }


    public void retrieveData() {

        Toast.makeText(getContext(), "Method call", Toast.LENGTH_SHORT).show();

        arrayListUser.clear();

        final String teacherName = teacherEmail;
        final char type = teacherName.charAt(0);
        final char userId = teacherName.charAt(1);

        final String mail = teacherName.substring(2);

        Toast.makeText(getContext(), mail, Toast.LENGTH_SHORT).show();

        StringRequest request = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            String sucess = jsonObject.getString("success");
                            JSONArray jsonArray=jsonObject.getJSONArray("data");

                            if(sucess.equals("1")){
                                Toast.makeText(getContext(), "1", Toast.LENGTH_SHORT).show();
                                for(int i=0; i< jsonArray.length(); i++){
                                    if(sucess.equals("1")){
                                        JSONObject object=jsonArray.getJSONObject(i);
                                        String UserId = object.getString("UserId");
                                        String UserName =object.getString("UserName");
                                        String UserEmail =object.getString("UserEmail");
                                        String UserPassword =object.getString("UserPassword");

                                        //Toast.makeText(getContext(), Class+Subject+Section, Toast.LENGTH_SHORT).show();

                                        arrayListUser.add(
                                                new UserModelClass(UserId, UserName, UserEmail, UserPassword)
                                        );
                                    }
                                    adapter=new MyAdapterUser(getContext() ,arrayListUser);
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