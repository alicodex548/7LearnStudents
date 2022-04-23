package com.sevenlearn.a7learnstudents;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;


public class AddNewStudentFormActivity extends AppCompatActivity {
    private RequestQueue requestQueue;
    private static final String TAG = "AddNewStudentFormActivi";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_student_form);
        requestQueue = Volley.newRequestQueue(this);

        Toolbar toolbar=findViewById(R.id.toolbar_addNewStudent);
        setSupportActionBar(toolbar);


        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_white_24dp);

        TextInputEditText firstNameEt = (TextInputEditText) findViewById(R.id.et_addNewStudent_firstName);
        TextInputEditText lastNameEt = (TextInputEditText) findViewById(R.id.et_addNewStudent_lastName);
        TextInputEditText courseEt = (TextInputEditText) findViewById(R.id.et_addNewStudent_course);
        TextInputEditText scoreEt = (TextInputEditText) findViewById(R.id.et_addNewStudent_score);

        View saveBtn = findViewById(R.id.fab_addNewStudent_save);
        saveBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (firstNameEt.length() > 0 &&
                lastNameEt.length() > 0 &&
                courseEt.length() > 0 &&
                scoreEt.length() > 0){
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("first_name",firstNameEt.getText().toString());
                        jsonObject.put("last_name",lastNameEt.getText().toString());
                        jsonObject.put("course",courseEt.getText().toString());
                        jsonObject.put("score",scoreEt.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonObjectRequest request= new JsonObjectRequest(Request.Method.POST,
                            "http://expertdevelopers.ir/api/v1/experts/student",
                            jsonObject,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.i(TAG, "onResponse: "+response);

                                    Student student = new Student();
                                    try {
                                        student.setId(response.getInt("id"));
                                        student.setFirstName(response.getString("first_name"));
                                        student.setLastName(response.getString("last_name"));
                                        student.setCourse(response.getString("course"));
                                        student.setScore(response.getInt("score"));
                                        Intent intent = new Intent();
                                        intent.putExtra("student", student);
                                        setResult(Activity.RESULT_OK,intent);
                                        finish();

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });

                    requestQueue.add(request);
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
