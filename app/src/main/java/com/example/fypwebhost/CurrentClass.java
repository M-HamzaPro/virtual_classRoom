package com.example.fypwebhost;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CurrentClass extends AppCompatActivity {
    SharedPreferences prefs;
    String classCode, loginEmail, userId, userName, userPassword, userType, classID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_class);



        prefs = getSharedPreferences("LogIn", MODE_PRIVATE);
        loginEmail = prefs.getString("email", "No name defined");
        userId = prefs.getString("userId", "");
        userName = prefs.getString("name", "");
        userPassword = prefs.getString("password", "");
        userType = prefs.getString("userType", "");
        Intent intent = getIntent();
        classCode = intent.getStringExtra("Class_id");
        classID = intent.getStringExtra("classID");
//

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation_class);

        bottomNav.setOnNavigationItemSelectedListener(navListener);


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new ClassStream()).commit();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.navigation_stream:
                            selectedFragment = new ClassStream();
                            break;
                        case R.id.navigation_classWork:
                            selectedFragment = new ClassWork(classCode, userType, classID);
                            break;
                        case R.id.navigation_members:
                            selectedFragment = new ClassMembers(classCode, classID, userType);
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();

                    return true;
                }
            };
}