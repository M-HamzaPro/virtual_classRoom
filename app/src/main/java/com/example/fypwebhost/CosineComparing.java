package com.example.fypwebhost;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class CosineComparing extends AppCompatActivity {

    EditText editText1, editText2;
    Button buttonCompare;
    TextView textViewResult;
    String URL = "https://temp321.000webhostapp.com/connect/cosineAlgo.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cosine_comparing);

        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);
        textViewResult = findViewById(R.id.textViewResult);
        buttonCompare = findViewById(R.id.buttonCompare);
        buttonCompare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compareText();
            }
        });
    }
    private void compareText()
    {
        final String text1 = editText1.getText().toString();
        final String text2 = editText2.getText().toString();

        if(text1.isEmpty() || text2.isEmpty())
        {
            Toast.makeText(this, "fill all text" , Toast.LENGTH_SHORT).show();
        }
        else
        {

            StringRequest request = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            if(response.contains("%"))
                            {
                                textViewResult.setText(response);


                            }
                            else
                            {
                                Toast.makeText(CosineComparing.this, response, Toast.LENGTH_SHORT).show();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(CosineComparing.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String > params = new HashMap<String, String>();

                    params.put("value1", text1);
                    params.put("value2", text2);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(CosineComparing.this);
            requestQueue.add(request);
        }


    }
}