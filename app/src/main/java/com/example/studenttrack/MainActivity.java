package com.example.studenttrack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText username, password,repassword, apogee, field, course,email ;
    TextView signin;
    Button register;
    Spinner spinner;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
        repassword=findViewById(R.id.repassword);
        email=findViewById(R.id.email);
        spinner = findViewById(R.id.spinner);
        apogee = findViewById(R.id.apogee);
        field = findViewById(R.id.field);
        course = findViewById(R.id.course);
        register=findViewById(R.id.register);
        signin = findViewById(R.id.signin);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.usertype, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        DB= new DBHelper(this);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedUserType = parent.getItemAtPosition(position).toString();
                if (selectedUserType.equals("Student")) {
                    course.setVisibility(View.GONE);
                    apogee.setVisibility(View.VISIBLE);
                    field.setVisibility(View.VISIBLE);
                } else if (selectedUserType.equals("Professor")) {
                    course.setVisibility(View.VISIBLE);
                    apogee.setVisibility(View.GONE);
                    field.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                String repass = repassword.getText().toString();
                String emailValue = email.getText().toString();
                String userType = spinner.getSelectedItem().toString();

                String apogeeValue = apogee.getText().toString();
                String fieldValue = field.getText().toString();
                String courseValue = course.getText().toString();

                if (TextUtils.isEmpty(user) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(repass) || TextUtils.isEmpty(emailValue)) {
                    Toast.makeText(MainActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                } else if (!pass.equals(repass)) {
                    Toast.makeText(MainActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                } else {
                    if (userType.equals("Professor")) {
                        if (TextUtils.isEmpty(courseValue)) {
                            Toast.makeText(MainActivity.this, "Course is required", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } else if (userType.equals("Student")) {
                        if (TextUtils.isEmpty(apogeeValue)) {
                            Toast.makeText(MainActivity.this, "Apogee is required", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (TextUtils.isEmpty(fieldValue)) {
                            Toast.makeText(MainActivity.this, "Field is required", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    if (DB.checkUsername(user)) {
                        Toast.makeText(MainActivity.this, "Username already exists", Toast.LENGTH_SHORT).show();
                    } else {
                        Boolean insert = DB.insertData(user, pass, userType, fieldValue, apogeeValue, courseValue, emailValue);

                        if (insert) {
                            Toast.makeText(MainActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                            if (userType.equals("Professor")) {
                                Intent intent = new Intent(getApplicationContext(), HomeProfActivity.class);
                                startActivity(intent);
                            } else if (userType.equals("Student")) {
                                Intent intent = new Intent(getApplicationContext(), HomeStudentActivity.class);
                                intent.putExtra("username", user);
                                startActivity(intent);
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}