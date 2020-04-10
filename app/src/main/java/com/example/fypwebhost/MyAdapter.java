package com.example.fypwebhost;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends ArrayAdapter<Classes> {

    Context context;



    List<Classes> arrayListClasses;

    public MyAdapter(@NonNull Context context, ArrayList<Classes> arrayListClasses) {
        super(context, R.layout.custom_list_item, arrayListClasses);

        this.context = context;
        this.arrayListClasses = arrayListClasses;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list_item, null,true);
        TextView textViewName = view.findViewById(R.id.textViewName);

        TextView textViewSubject = view.findViewById(R.id.textViewSubject);
        TextView textViewId = view.findViewById(R.id.textViewId);


        textViewName.setText(arrayListClasses.get(position).getName());
        textViewSubject.setText(arrayListClasses.get(position).getSubject());
        textViewId.setText(arrayListClasses.get(position).getId());
        return view;
    }
}
