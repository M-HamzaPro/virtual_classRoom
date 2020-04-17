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

public class AssignmentAdapter extends ArrayAdapter<AssignmentModelClass> {

    Context context;


    List<AssignmentModelClass> arrayListAssignment;

    public AssignmentAdapter(@NonNull Context context, List<AssignmentModelClass> arrayListAssignment) {
        super(context, R.layout.custom_list_assignment, arrayListAssignment);

        this.context = context;
        this.arrayListAssignment = arrayListAssignment;
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list_assignment, null,true);

        TextView textViewTitle = view.findViewById(R.id.textViewTitle);
        TextView textViewDueDate = view.findViewById(R.id.textViewDueDate);
        TextView textViewPostDate = view.findViewById(R.id.textViewPostDate);

        textViewTitle.setText(arrayListAssignment.get(position).getTitle());
        textViewDueDate.setText(arrayListAssignment.get(position).getDueDate());
        textViewPostDate.setText(arrayListAssignment.get(position).getPostDate());

        return view;
    }
}

