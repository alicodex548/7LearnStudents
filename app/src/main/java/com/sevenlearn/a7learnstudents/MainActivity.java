package com.sevenlearn.a7learnstudents;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final Integer ADD_STUDENT_REQUEST_ID = 1001;
    private StudentAdapter studentAdapter;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar=findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        View addNewStudentBtn = findViewById(R.id.fab_main_addNewStudent);
        addNewStudentBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this,AddNewStudentFormActivity.class),1001);
            }
        });


        StringRequest request=new StringRequest(Request.Method.GET, "http://expertdevelopers.ir/api/v1/experts/student", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "onResponse: ");

                List<Student> students=new ArrayList<>();
                try {
                    JSONArray jsonArray=new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject studentJsonObject=jsonArray.getJSONObject(i);
                        Student student=new Student();
                        student.setId(studentJsonObject.getInt("id"));
                        student.setFirstName(studentJsonObject.getString("first_name"));
                        student.setLastName(studentJsonObject.getString("last_name"));
                        student.setScore(studentJsonObject.getInt("score"));
                        student.setCourse(studentJsonObject.getString("course"));
                        students.add(student);
                    }

                    Log.i(TAG, "onResponse: "+students.size());

                    recyclerView=findViewById(R.id.rv_main_students);
                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this,RecyclerView.VERTICAL,false));
                    studentAdapter = new StudentAdapter(students);
                    recyclerView.setAdapter(studentAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: "+error);
            }
        });

        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == ADD_STUDENT_REQUEST_ID && resultCode == Activity.RESULT_OK && recyclerView != null) {
            Student student = data.getParcelableExtra("student");
            if (data!= null && student != null) {
                studentAdapter.addStudent(student);
                recyclerView.smoothScrollToPosition(0);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
