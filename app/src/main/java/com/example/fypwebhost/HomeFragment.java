package com.example.fypwebhost;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
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

  //  EditText editTextName, editTextEmail, editTextPassword;
    TextView  textViewName, textViewEmail,textViewPassword, textViewTest;
    Button buttonEditProfile;
    ListView listView;
    MyAdapterUser adapter;

    SharedPreferences prefs;
    String userEmail , UserId, userIdOld, userName, userPassword;

    private SharedPreferences.Editor mEditor;
    public static String URL="https://temp321.000webhostapp.com/connect/getProfile.php";

    public static ArrayList<UserModelClass> arrayListUser = new ArrayList<UserModelClass>();

    public HomeFragment(String email, String userIdOld, String userName, String userPassword) {

        this.userEmail = email;
        this.userIdOld = userIdOld;
        this.userName = userName;
        this.userPassword = userPassword;
       }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Toast.makeText(getContext(), "checkin"+userEmail, Toast.LENGTH_SHORT).show();
        Toast.makeText(getContext(), "checkin"+userIdOld, Toast.LENGTH_SHORT).show();

        buttonEditProfile = view.findViewById(R.id.buttonEditProfile);
        buttonEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getContext(), ProfileActivity.class);
//                intent.putExtra("name", textViewName.getText());
//                intent.putExtra("password", textViewPassword.getText());
//                intent.putExtra("id", UserId);
                startActivity(intent);
            }
        });

//        editTextName = view.findViewById(R.id.editTextName);
//        editTextEmail = view.findViewById(R.id.editTextEmail);
//        editTextPassword = view.findViewById(R.id.editTextPassword);
//
//        editTextEmail.setEnabled(false);
//        editTextName.setEnabled(false);
//        editTextPassword.setEnabled(false);

        textViewTest = view.findViewById(R.id.textViewTest);
        textViewName = view.findViewById(R.id.textViewName);
        textViewEmail = view.findViewById(R.id.textViewEmail);
        textViewPassword = view.findViewById(R.id.textViewPassword);

        listView = view.findViewById(R.id.mylistview);

        textViewName.setText(userName);
        textViewEmail.setText(userEmail);
        textViewPassword.setText(userPassword);

       // retrieveData();               //to get person's data

        return view;
    }


    public void retrieveData() {

        //Toast.makeText(getContext(), "Method call", Toast.LENGTH_SHORT).show();

        arrayListUser.clear();

 //       final String teacherName = teacherEmail;
//        final char type = teacherName.charAt(0);
//        final char userId1 = teacherName.charAt(1);
//        final char userId2 = teacherName.charAt(2);

       // final String userId = teacherName.substring(1, 3);
       // final String mail = teacherName.substring(3);

        //Toast.makeText(getContext(), "mail check"+teacherEmail, Toast.LENGTH_SHORT).show();

//        final String[] data = teacherEmail.split(",");
//        //Toast.makeText(getContext(), "id check"+data[1], Toast.LENGTH_SHORT).show();

        StringRequest request = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            String sucess = jsonObject.getString("success");
                            JSONArray jsonArray=jsonObject.getJSONArray("data");

                            if(sucess.equals("1")){
                                //Toast.makeText(getContext(), "1", Toast.LENGTH_SHORT).show();
                                for(int i=0; i< jsonArray.length(); i++){
                                    if(sucess.equals("1")){
                                        JSONObject object=jsonArray.getJSONObject(i);
                                        UserId = object.getString("UserId");
                                        String UserName =object.getString("UserName");
                                        String UserEmail =object.getString("UserEmail");
                                        String UserPassword =object.getString("UserPassword");



                                        textViewName.setText(UserName);
                                        textViewEmail.setText(UserEmail);
                                        textViewPassword.setText(UserPassword);

//                                        arrayListUser.add(
//                                                new UserModelClass(UserId, UserName, UserEmail, UserPassword)
//                                        );
                                    }
//                                    adapter=new MyAdapterUser(getContext() ,arrayListUser);
//                                    adapter.notifyDataSetChanged();
//                                    listView.setAdapter(adapter);

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
                params.put("classTeacher", userIdOld);
                return params;
            }
        };


        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);
    }
}