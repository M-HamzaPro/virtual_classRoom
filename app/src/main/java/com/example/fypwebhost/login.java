package com.example.fypwebhost;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.Circle;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class login extends AppCompatActivity {

    TextView textViewSignup, textViewForgetPassword;
    EditText editTextEmail, editTextPassword;
    Button buttonLogin;
    //String userType;
    String type;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        Sprite circle = new Circle();
        progressBar.setIndeterminateDrawable(circle);

        progressBar.setVisibility(View.INVISIBLE);


        textViewSignup = findViewById(R.id.textViewSignup);
        textViewSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login.this, MainActivity.class);
                startActivity(intent);
            }
        });

        editTextEmail =  findViewById(R.id.editTextEmail);
        editTextPassword =  findViewById(R.id.editTextPassword);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userLogin();

            }
        });

        textViewForgetPassword = findViewById(R.id.textViewForgetPassword);
        textViewForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = editTextEmail.getText().toString().trim();
                if(email.isEmpty())
                {
                    editTextEmail.setError("This field is mandatory");
                    editTextPassword.setError("This field is mandatory");
                    Toast.makeText(getApplicationContext(), "Email must be entered" , Toast.LENGTH_SHORT).show();
                }
                else {
                    final int min = 000001;
                    final int max = 999999;
                    final int random = new Random().nextInt((max - min) + 1) + min;
                    final String verificationCode = String.valueOf(random).trim();

                    StringRequest request = new StringRequest(Request.Method.POST, "https://temp321.000webhostapp.com/connect/verificationCodeSender.php",
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if(response.equalsIgnoreCase("code sent to email"))
                                    {
                                        Toast.makeText(getApplicationContext(), "code sent to email", Toast.LENGTH_SHORT).show();
//                                        Intent intent = new Intent(getApplicationContext(), ChangePasswordCode.class);
//                                        intent.putExtra("verificationCode ", verificationCode);
//                                        startActivity(intent);
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

                            params.put("email", email);
                            params.put("code", verificationCode);


                            return params;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    requestQueue.add(request);

                    Toast.makeText(getApplicationContext(), "Code = "+verificationCode, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), ChangePasswordCode.class);
                    intent.putExtra("verificationCode", verificationCode);
                    intent.putExtra("email", email);
                    startActivity(intent);
                }

            }
        });
    }






    private void userLogin() {


        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();

        if(email.isEmpty() || password.isEmpty())
        {
            editTextEmail.setError("This field is mandatory");
            editTextPassword.setError("This field is mandatory");
            Toast.makeText(this, "fill all text" , Toast.LENGTH_SHORT).show();
        }
        else
        {
            progressBar.setVisibility(View.VISIBLE);
            StringRequest request = new StringRequest(Request.Method.POST, "https://temp321.000webhostapp.com/connect/loginTest.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            String[] data = response.split(",");
                            Toast.makeText(getApplicationContext(), "response = "+response, Toast.LENGTH_SHORT).show();
                            if(isValidEmail(data[2].trim()))
                            {
                                Toast.makeText(login.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                                SharedPreferences.Editor editor = getSharedPreferences("LogIn", MODE_PRIVATE).edit();


                                editor.putString("userType", data[0]);
                                editor.putString("userId", data[1]);
                                editor.putString("email", data[2]);
                                editor.putString("name", data[3]);
                                editor.putString("password", data[4]);
                                editor.apply();

                              //  Toast.makeText(login.this, userType, Toast.LENGTH_SHORT).show();
                                type = data[0];
                                
                                if(type.contains("1"))
                                {
                                    Intent intent = new Intent(login.this, Teacher_Activity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else
                                {
                                    Intent intent = new Intent(login.this, Student_Activity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                            else
                            {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(login.this, "Logged in Fail", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(login.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String > params = new HashMap<String, String>();

                    params.put("Email", email);
                    params.put("Password", password);

                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(login.this);
            requestQueue.add(request);
        }
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}


