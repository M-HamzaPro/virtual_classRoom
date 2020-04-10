package com.example.fypwebhost;

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
import java.util.Random;

public class AddFragment extends Fragment {


    EditText editTextId, editTextName, editTextSubject;
    Button buttonCreateClass, buttonIdGen;
    String loginEmail;


    public AddFragment(String email) {
        this.loginEmail = email;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add, container, false);
        editTextId = (EditText) view.findViewById(R.id.editTextCode);
        editTextName = (EditText) view.findViewById(R.id.editTextName);
        editTextSubject = (EditText) view.findViewById(R.id.editTextSubject);
        buttonCreateClass = (Button) view.findViewById(R.id.buttonCreateClass);
        buttonCreateClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createClass();
            }
        });

        buttonIdGen = (Button) view.findViewById(R.id.buttonIdGen);
        buttonIdGen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int min = 000001;
                final int max = 999999;
                final int random = new Random().nextInt((max - min) + 1) + min;

                String name = editTextName.getText().toString().trim();
                String section = editTextSubject.getText().toString().trim();

                String random_id = String.valueOf(random);
                String id = name + random_id + section ;
                editTextId.setText(id);
            }
        });
        return view;
    }

    private void createClass() {


        final String teacherName = loginEmail;

        final char type = teacherName.charAt(0);
        final char userId = teacherName.charAt(1);
        final String mail = teacherName.substring(2);

        final String id = editTextId.getText().toString().trim();
        final String name = editTextName.getText().toString().trim();
        final String class_subject = editTextSubject.getText().toString().trim();

        if(name.isEmpty() || id.isEmpty() || class_subject.isEmpty())
        {
            Toast.makeText(getContext(), "fill all text" , Toast.LENGTH_SHORT).show();
        }
        else
        {

            StringRequest request = new StringRequest(Request.Method.POST, "https://temp321.000webhostapp.com/connect/create_class.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response.equalsIgnoreCase("Class Created"))
                            {
                                Toast.makeText(getContext(), "Class Created", Toast.LENGTH_SHORT).show();
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

                    params.put("Name", name);
                    params.put("class_teacher", String.valueOf(userId));
                    params.put("class_id", id);
                    params.put("class_subject", class_subject);

                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(request);
        }


    }
}
