package com.example.fypwebhost;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

public class JoinClassFragment extends Fragment {

    Button buttonJoinClass;
    EditText editTextClassName, editTextClassCode;
    String studentID;

    public JoinClassFragment(String studentID)
    {
        this.studentID = studentID;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.join_class_fragment, container, false);

        Toast.makeText(getContext(), "student id = "+studentID, Toast.LENGTH_SHORT).show();
        editTextClassName = view.findViewById(R.id.editTextClassName);
        editTextClassCode = view.findViewById(R.id.editTextClassCode);

        buttonJoinClass = view.findViewById(R.id.buttonJoinClass);
        buttonJoinClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinClass();
            }
        });
        return view;


    }

    private void joinClass()
    {
        final String studentIdPhp = studentID;
        final String className = editTextClassName.getText().toString();
        final String classCode = editTextClassCode.getText().toString();

        if(className.isEmpty() || classCode.isEmpty())
        {
            Toast.makeText(getContext(), "fill all text" , Toast.LENGTH_SHORT).show();
        }
        else
        {

            StringRequest request = new StringRequest(Request.Method.POST, "https://temp321.000webhostapp.com/connect/joinClassNew.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
//                            if(response.equalsIgnoreCase("1"))
                            String finalResponse = response.trim();
                            if(finalResponse.contains("yahoo"))
                            {
                                Toast.makeText(getContext(), "Class Joined", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(getContext(), CurrentClass.class);
                                startActivity(intent);


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

                    params.put("className", className);
                    params.put("studentId", studentIdPhp);
                    params.put("classCode", classCode);

                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(request);
        }


    }
}
