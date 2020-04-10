package com.example.fypwebhost;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

public class Create_Class extends AppCompatActivity {


    EditText editTextId, editTextName, editTextSection, editTextSubject;
    Button buttonCreateClass, buttonIdGen, buttonViewClass;
    SharedPreferences prefs;
    String loginEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__class);

        Intent intent = getIntent();
        String className = intent.getStringExtra("Class_name");

        Toast.makeText(getApplicationContext(), className, Toast.LENGTH_LONG).show();

        editTextId = findViewById(R.id.editTextId);
        editTextName = findViewById(R.id.editTextName);
        editTextSection = findViewById(R.id.editTextSection);
        editTextSubject = findViewById(R.id.editTextSubject);

        buttonCreateClass = findViewById(R.id.buttonCreateClass);
        buttonCreateClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // createClass();
            }
        });

        buttonViewClass = findViewById(R.id.buttonViewClass);
        buttonViewClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefs = getSharedPreferences("LogIn", MODE_PRIVATE);
                loginEmail = prefs.getString("email", "No name defined");



                Intent intent = new Intent(Create_Class.this, Teacher_Activity.class);
                startActivity(intent);
            }
        });
        buttonIdGen = findViewById(R.id.buttonIdGen);
        buttonIdGen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int min = 000001;
                final int max = 999999;
                final int random = new Random().nextInt((max - min) + 1) + min;

                String name = editTextName.getText().toString().trim();
                String section = editTextSection.getText().toString().trim();

                String random_id = String.valueOf(random);
                String id = name + random_id + section ;
                editTextId.setText(id);
            }
        });




    }

    private void createClass() {

        prefs = getSharedPreferences("LogIn", MODE_PRIVATE);
        loginEmail = prefs.getString("email", "No name defined");

//        SharedPreferences.Editor editor = getSharedPreferences("LogIn", MODE_PRIVATE).edit();
//        editor.putString("email", loginEmail);
//        editor.apply();

        final String teacherName = loginEmail;

        final String id = editTextId.getText().toString().trim();
        final String name = editTextName.getText().toString().trim();
        final String section = editTextSection.getText().toString().trim();
        final String class_subject = editTextSubject.getText().toString().trim();

        if(name.isEmpty() || id.isEmpty() || section.isEmpty() || class_subject.isEmpty())
        {
            Toast.makeText(this, "fill all text" , Toast.LENGTH_SHORT).show();
        }
        else
        {

            StringRequest request = new StringRequest(Request.Method.POST, "https://temp321.000webhostapp.com/connect/create_class.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response.equalsIgnoreCase("Class Created"))
                            {
                                Toast.makeText(Create_Class.this, "Class Created", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(Create_Class.this, response, Toast.LENGTH_SHORT).show();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Create_Class.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String > params = new HashMap<String, String>();

                    params.put("Name", name);
                    params.put("class_teacher", teacherName);
                    params.put("class_id", id);
                    params.put("section", section);
                    params.put("class_subject", class_subject);

                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(Create_Class.this);
            requestQueue.add(request);
        }


    }

}


