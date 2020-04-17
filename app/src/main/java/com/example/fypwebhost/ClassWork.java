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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class ClassWork extends Fragment {

     Button buttonCreateAssignment;
     EditText editTextTitle, editTextDueDate, editTextPostDate;
    String assigTitle, assigDueDate, assigPostDate, classCode;

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
               showCreateDialog(classCode);
            }
        });

        return view;
    }
    private void showCreateDialog(final String classId)
    {
    //    Toast.makeText(getContext(), "class code = "+classCode, Toast.LENGTH_SHORT).show();



        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());

        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.create_assig_dialog,null);

        dialogBuilder.setView(dialogView);

        editTextTitle = (EditText) dialogView.findViewById(R.id.editTextTitle);
        editTextDueDate = (EditText) dialogView.findViewById(R.id.editTextDueDate);
        editTextPostDate = (EditText) dialogView.findViewById(R.id.editTextPostDate);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonCreateAssignment);

     //   dialogBuilder.setTitle("Updating Class:  "+className +" "+ classId);
        dialogBuilder.setTitle("Creating New Assignment");

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                assigTitle = editTextTitle.getText().toString().trim();
                assigDueDate = editTextDueDate.getText().toString().trim();
                assigPostDate = editTextPostDate.getText().toString().trim();
// || TextUtils.isEmpty(newSubject)

                if (TextUtils.isEmpty(assigTitle)) {
                    editTextTitle.setError("Name Required");
                    return;
                }
//                progressBar.setVisibility(View.VISIBLE);
                createAssignment();
//                Toast.makeText(getContext(), "wow", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });


    }
    private void createAssignment()
    {
        final String title  = editTextTitle.getText().toString().trim();
        final String dueDate = editTextDueDate.getText().toString().trim();
        final String postDate = editTextPostDate.getText().toString().trim();

        if(title.isEmpty() || dueDate.isEmpty() || postDate.isEmpty())
        {
            Toast.makeText(getContext(), "fill all text" , Toast.LENGTH_SHORT).show();
        }
        else
        {


            StringRequest request = new StringRequest(Request.Method.POST, "https://temp321.000webhostapp.com/connect/createAssignment.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response.contains("Assignment Created success"))
                            {
                                Toast.makeText( getContext(), "Assignment Created", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(getContext(), response, Toast.LENGTH_SHORT).show();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String > params = new HashMap<String, String>();

                    params.put("title", title);
                    params.put("dueDate", dueDate);
                    params.put("postDate", postDate);
                    params.put("classCode", String.valueOf(classCode));

                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(request);
        }
    }
}
