package com.example.fypwebhost;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class ChangePasswordCode extends AppCompatActivity {

    TextView textView;
    EditText editTextCode,editTextNewPassword, editTextNewPasswordConfirm;
    Button buttonVerify, buttonSetNewPassword;
    String oldCode, userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password_code);

        textView = findViewById(R.id.textView2);
        editTextCode = findViewById(R.id.editTextCode);
        buttonVerify = findViewById(R.id.buttonVerify);

        editTextNewPassword = findViewById(R.id.editTextNewPassword);
        editTextNewPasswordConfirm = findViewById(R.id.editTextNewPasswordConfrim);
        buttonSetNewPassword = findViewById(R.id.buttonSetNewPassword);
        buttonSetNewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               passwordChange();
            }
        });


        oldCode = getIntent().getStringExtra("verificationCode");
        userEmail = getIntent().getStringExtra("email");
        Toast.makeText(getApplicationContext(), "new "+oldCode, Toast.LENGTH_SHORT).show();

        buttonVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newCode  = editTextCode.getText().toString().trim();


                int newCodeInt = Integer. parseInt(newCode);
                int oldCodeInt = Integer. parseInt(oldCode);

                if(newCodeInt == oldCodeInt)
                {
                    Toast.makeText(getApplicationContext(), "same codes", Toast.LENGTH_SHORT).show();
                    editTextCode.setVisibility(View.INVISIBLE);
                    textView.setVisibility(View.INVISIBLE);
                    buttonVerify.setVisibility(View.INVISIBLE);

                    editTextNewPassword.setVisibility(View.VISIBLE);
                    editTextNewPasswordConfirm.setVisibility(View.VISIBLE);
                    buttonSetNewPassword.setVisibility(View.VISIBLE);

                }
                else
                {
                    Toast.makeText(getApplicationContext(), "not same codes", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }





    private void passwordChange() {

        final String newPassword = editTextNewPassword.getText().toString().trim();
        final String newPasswordConfirm = editTextNewPasswordConfirm.getText().toString().trim();

        if(newPassword.isEmpty() || newPasswordConfirm.isEmpty())
        {
            editTextNewPassword.setError("This field is mandatory");
            editTextNewPasswordConfirm.setError("This field is mandatory");
            Toast.makeText(this, "fill all text" , Toast.LENGTH_SHORT).show();
        }
        else
        {
           // progressBar.setVisibility(View.VISIBLE);
            StringRequest request = new StringRequest(Request.Method.POST, "https://temp321.000webhostapp.com/connect/forgetPasswordChange.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response.equalsIgnoreCase("password changed"))
                            {
                                Toast.makeText(getApplicationContext(), "password changed", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(getApplicationContext(), login.class);
                                startActivity(intent);
                                finish();
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

                    params.put("email", userEmail);
                    params.put("password", newPassword);

                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(ChangePasswordCode.this);
            requestQueue.add(request);
        }
    }





}
