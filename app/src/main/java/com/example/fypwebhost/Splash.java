package com.example.fypwebhost;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Splash extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 4000;

    SharedPreferences prefs;
    String loginEmail, userType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        prefs = getSharedPreferences("LogIn", MODE_PRIVATE);
        loginEmail = prefs.getString("email", "No name defined");
        userType = prefs.getString("userType", "");
        Toast.makeText(Splash.this,loginEmail, Toast.LENGTH_LONG).show();
        if(loginEmail== "No name defined")
        {
            /* New Handler to start the Menu-Activity
             * and close this Splash-Screen after some seconds.*/
            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                    /* Create an Intent that will start the Menu-Activity. */
                    Intent mainIntent = new Intent(Splash.this,login.class);
                    Splash.this.startActivity(mainIntent);
                    Splash.this.finish();
                }
            }, SPLASH_DISPLAY_LENGTH);
        }
        else
        {
            String  id = userType;
            if(id.contains("1"))
            {
                /* New Handler to start the Menu-Activity
                 * and close this Splash-Screen after some seconds.*/
                new Handler().postDelayed(new Runnable(){
                    @Override
                    public void run() {
                        /* Create an Intent that will start the Menu-Activity. */
                        Intent mainIntent = new Intent(Splash.this,Teacher_Activity.class);
                        Splash.this.startActivity(mainIntent);
                        Splash.this.finish();
                    }
                }, SPLASH_DISPLAY_LENGTH);
            }
            else
            {
                /* New Handler to start the Menu-Activity
                 * and close this Splash-Screen after some seconds.*/
                new Handler().postDelayed(new Runnable(){
                    @Override
                    public void run() {
                        /* Create an Intent that will start the Menu-Activity. */
                        Intent mainIntent = new Intent(Splash.this,Student_Activity.class);
                        Splash.this.startActivity(mainIntent);
                        Splash.this.finish();
                    }
                }, SPLASH_DISPLAY_LENGTH);
            }
        }

    }
}