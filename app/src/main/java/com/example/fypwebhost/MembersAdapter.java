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

public class MembersAdapter extends ArrayAdapter<MembersModelClass> {

    Context context;

    List<MembersModelClass> arrayListMembers;

    public MembersAdapter(@NonNull Context context, List<MembersModelClass> arrayListMembers) {
        super(context, R.layout.custom_list_class_members, arrayListMembers);

        this.context = context;
        this.arrayListMembers = arrayListMembers;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list_class_members, null,true);

        TextView textViewName = view.findViewById(R.id.textViewName);
        TextView textViewEmail = view.findViewById(R.id.textViewEmail);

        textViewName.setText(arrayListMembers.get(position).getMemberName());
        textViewEmail.setText(arrayListMembers.get(position).getMemberEmail());

        return view;
    }
}
