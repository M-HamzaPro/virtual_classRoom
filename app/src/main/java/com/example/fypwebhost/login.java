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

public class login extends AppCompatActivity {

    TextView textViewSignup;
    EditText editTextEmail, editTextPassword;
    Button buttonLogin;
    String userType;
    char type;
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
            StringRequest request = new StringRequest(Request.Method.POST, "https://temp321.000webhostapp.com/connect/loginCode.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(isValidEmail(response))
                            {
                                Toast.makeText(login.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                                SharedPreferences.Editor editor = getSharedPreferences("LogIn", MODE_PRIVATE).edit();
                                editor.putString("userType", response);
                                editor.putString("userId", response);
                                editor.putString("email", response);
                                editor.apply();

                                Toast.makeText(login.this, userType, Toast.LENGTH_SHORT).show();
                                type = response.charAt(0);
                                if(type == '1')
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


