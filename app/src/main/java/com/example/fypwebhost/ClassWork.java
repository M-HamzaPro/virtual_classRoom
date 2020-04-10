package com.example.fypwebhost;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ClassWork extends Fragment {

     Button buttonCreateAssignment;
     EditText editTextTitle, editTextDueDate, editTextPostDate;
    String assigTitle, assigDueDate, classCode;

    public ClassWork(String classCode)
    {
        this.classCode = classCode;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.class_work, container, false);

        buttonCreateAssignment = (Button) view.findViewById(R.id.buttonAddAssignment);
        buttonCreateAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showCreateDialog(classCode, "name");
            }
        });

        return view;
    }
    private void showCreateDialog(final String classId, String className)
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());

        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.create_assig_dialog,null);

        dialogBuilder.setView(dialogView);

        editTextTitle = (EditText) dialogView.findViewById(R.id.editTextTitle);
        editTextDueDate = (EditText) dialogView.findViewById(R.id.editTextDueDate);
        editTextPostDate = (EditText) dialogView.findViewById(R.id.editTextPostDate);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonCreateAssignment);

        dialogBuilder.setTitle("Updating Class:  "+className +" "+ classId);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                assigTitle = editTextTitle.getText().toString().trim();
                assigDueDate = editTextDueDate.getText().toString().trim();
// || TextUtils.isEmpty(newSubject)

                if (TextUtils.isEmpty(assigTitle)) {
                    editTextTitle.setError("Name Required");
                    return;
                }
                //progressBar.setVisibility(View.VISIBLE);
               // updateClass();
                Toast.makeText(getContext(), "wow", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });


    }
}
