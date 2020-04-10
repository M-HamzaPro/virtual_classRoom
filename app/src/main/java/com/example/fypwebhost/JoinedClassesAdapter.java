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

public class JoinedClassesAdapter extends ArrayAdapter<JoinedClassesModel> {

    Context context;



    List<JoinedClassesModel> arrayListJoinedClasses;

    public JoinedClassesAdapter(@NonNull Context context, ArrayList<JoinedClassesModel> arrayListJoinedClasses) {
        super(context, R.layout.custom_list_joinedclasses, arrayListJoinedClasses);

        this.context = context;
        this.arrayListJoinedClasses = arrayListJoinedClasses;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list_joinedclasses, null,true);
        TextView textViewName = view.findViewById(R.id.textViewName);

        TextView textViewSubject = view.findViewById(R.id.textViewSubject);
        TextView textViewId = view.findViewById(R.id.textViewId);


        textViewName.setText(arrayListJoinedClasses.get(position).getName());
        textViewSubject.setText(arrayListJoinedClasses.get(position).getSubject());
        textViewId.setText(arrayListJoinedClasses.get(position).getId());
        return view;
    }
}