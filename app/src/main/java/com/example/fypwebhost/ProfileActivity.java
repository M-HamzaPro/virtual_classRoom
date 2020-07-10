package com.example.fypwebhost;

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

public class ProfileActivity extends AppCompatActivity {

    Button buttonSetChanges;
    EditText editTextName, editTextPassword;
//    String name, password, userId;

    SharedPreferences prefs;
    String loginEmail, userId, userName, userPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        editTextName = findViewById(R.id.editTextTextPersonName);
        editTextPassword = findViewById(R.id.editTextTextPersonPassword);

        prefs = getSharedPreferences("LogIn", MODE_PRIVATE);
        loginEmail = prefs.getString("email", "No name defined");
        userId = prefs.getString("userId", "");
        userName = prefs.getString("name", "");
        userPassword = prefs.getString("password", "");

//        name = getIntent().getStringExtra("name");
//        password = getIntent().getStringExtra("password");
//        userId = getIntent().getStringExtra("id");

        editTextName.setText(userName);
        editTextPassword.setText(userPassword);

        buttonSetChanges = findViewById(R.id.buttonSetChanges);
        buttonSetChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileChange();
            }
        });
    }
    
    
    
    private void profileChange() {

        final String newName = editTextName.getText().toString().trim();
        final String newPassword = editTextPassword.getText().toString().trim();

        if(newPassword.isEmpty() || newName.isEmpty())
        {
            editTextName.setError("This field is mandatory");
            editTextPassword.setError("This field is mandatory");
            Toast.makeText(this, "fill all text" , Toast.LENGTH_SHORT).show();
        }
        else
        {
            // progressBar.setVisibility(View.VISIBLE);
            StringRequest request = new StringRequest(Request.Method.POST, "https://temp321.000webhostapp.com/connect/forgetPasswordChange.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response.equalsIgnoreCase("Profile updated"))
                            {
                                Toast.makeText(getApplicationContext(), "Profile updated", Toast.LENGTH_SHORT).show();

                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String > params = new HashMap<String, String>();

                    params.put("id", userId);
                    params.put("password", newPassword);
                    params.put("name", newName);

                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(ProfileActivity.this);
            requestQueue.add(request);
        }
    }
}