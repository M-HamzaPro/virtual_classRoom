package com.example.fypwebhost;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ClassMembers extends Fragment {

    public static ArrayList<MembersModelClass> membersArrayList = new ArrayList<>();
    MembersAdapter adapter;
    ListView listView;
    String classCode, classID, studentID, userType;

    public ClassMembers(String classCode, String classID, String userType)
    {
        this.classCode = classCode;
        this.classID = classID;
        this.userType = userType;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.class_members, container, false);
        listView = view.findViewById(R.id.myListview);

        retrieveMembers();

        ///////////

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

            if(userType.contains("1"))
            {
              MembersModelClass membersModelClass = membersArrayList.get(position);
              studentID = membersModelClass.getStudentID(); // to use in sending data for update
              showRemoveDialog(studentID, classID);
            }

            else
                {
                  Toast.makeText(getContext(), "do nothing", Toast.LENGTH_SHORT).show();
                }



                return true;
            }
        });


        return view;
    }

    public void retrieveMembers()
    {

        membersArrayList.clear();


        StringRequest request = new StringRequest(Request.Method.POST, "https://temp321.000webhostapp.com/connect/getMembers.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("wwww",response);
                            JSONObject jsonObject=new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray=jsonObject.getJSONArray("data");
                            Toast.makeText(getContext(),"success"+success, Toast.LENGTH_SHORT).show();
                            if(success.equals("1")){
//                                progressBar.setVisibility(View.INVISIBLE);
//                                Toast.makeText(getContext(), "membersworking", Toast.LENGTH_SHORT).show();
                                for(int i=0; i< jsonArray.length(); i++){
                                    if(success.contains("1")){
                                        JSONObject object=jsonArray.getJSONObject(i);
//                                      Toast.makeText(getContext(), "checking ", Toast.LENGTH_SHORT).show();
                                        String studentID = object.getString("studentID");
                                        String memberName = object.getString("memberName");
                                        String memberEmail =object.getString("memberEmail");

//                                        Toast.makeText(getContext(),"name"+memberName, Toast.LENGTH_SHORT).show();
//                                        Toast.makeText(getContext(),"email"+memberEmail, Toast.LENGTH_SHORT).show();
                                        membersArrayList.add(
                                                new MembersModelClass(memberName,memberEmail, studentID)
                                        );
                                    }
                                    adapter=new MembersAdapter(getActivity() ,membersArrayList);
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

////////
    private void showRemoveDialog(final String studentIDR, final String classID)
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());

        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.remove_student,null);

        dialogBuilder.setView(dialogView);


        final Button buttonRemove = (Button) dialogView.findViewById(R.id.buttonRemove);

        dialogBuilder.setTitle("remove student:");

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



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

                            params.put("studentID", studentIDR);
                            params.put("classID", classID);

                            return params;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                    requestQueue.add(request);








                alertDialog.dismiss();
            }
        });


    }

}
