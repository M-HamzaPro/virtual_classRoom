package com.example.fypwebhost;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;


public class MyAdapterUser extends ArrayAdapter<UserModelClass> {

    Context context;


    List<UserModelClass> arrayListUser;

    public MyAdapterUser(@NonNull Context context, List<UserModelClass> arrayListUser) {
        super(context, R.layout.custom_list_user, arrayListUser);

        this.context = context;
        this.arrayListUser = arrayListUser;
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list_user, null,true);
       // TextView textViewId = view.findViewById(R.id.textViewId);
        TextView textViewName = view.findViewById(R.id.textViewName);
        TextView textViewEmail = view.findViewById(R.id.textViewEmail);
       // TextView textViewPassword = view.findViewById(R.id.textViewPassword);


        //textViewId.setText(arrayListUser.get(position).getId());
        textViewName.setText(arrayListUser.get(position).getName());
        textViewEmail.setText(arrayListUser.get(position).getEmail());
        //textViewPassword.setText(arrayListUser.get(position).getPassword());

        return view;
    }
}
