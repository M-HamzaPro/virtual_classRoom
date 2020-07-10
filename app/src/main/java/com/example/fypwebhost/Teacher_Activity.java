package com.example.fypwebhost;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Teacher_Activity extends AppCompatActivity {

    SharedPreferences prefs;
    String loginEmail, userId, userName, userPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_);


        prefs = getSharedPreferences("LogIn", MODE_PRIVATE);
        loginEmail = prefs.getString("email", "No name defined");
        userId = prefs.getString("userId", "");
        userName = prefs.getString("name", "");
        userPassword = prefs.getString("password", "");


        Toast.makeText(getApplicationContext(),"->"+loginEmail, Toast.LENGTH_SHORT).show();
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        //I added this if statement to keep the selected fragment when rotating the device
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment(loginEmail, userId, userName, userPassword)).commit();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            selectedFragment = new HomeFragment(loginEmail, userId, userName, userPassword);
                            break;
                        case R.id.navigation_add:
                            selectedFragment = new AddFragment(loginEmail);
                            break;
                        case R.id.navigation_classes:
                            selectedFragment = new ClassesFragment(loginEmail, userId, userName, userPassword);
                            break;
                        default:
                            selectedFragment = new HomeFragment(loginEmail, userId, userName, userPassword);
                            break;

                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();

                    return true;
                }
            };

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.menu_profile:
                Intent intent = new Intent(getApplicationContext(), Student_Activity.class);
                startActivity(intent);

                Toast.makeText(getApplicationContext(), "profile pressed", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_logout:
                Toast.makeText(getApplicationContext(), "logout pressed", Toast.LENGTH_SHORT).show();
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("LogIn", Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();

                // teacherEmail = null;
                Intent intent1 = new Intent(getApplicationContext(), login.class);
                startActivity(intent1);
                break;

        }
        return true;
    }
}