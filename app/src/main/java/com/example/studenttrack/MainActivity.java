package com.example.studenttrack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText username, password,repassword;
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
        spinner = findViewById(R.id.spinner);
        register=findViewById(R.id.register);
        signin = findViewById(R.id.signin);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.usertype, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        DB= new DBHelper(this);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                String repass = repassword.getText().toString();
                String userType = spinner.getSelectedItem().toString();

                Log.d("Registration", "User: " + user);
                Log.d("Registration", "Password: " + pass);
                Log.d("Registration", "Repassword: " + repass);
                Log.d("Registration", "User Type: " + userType);

                if (TextUtils.isEmpty(user) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(repass)) {
                    Toast.makeText(MainActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                } else if (!pass.equals(repass)) {
                    Toast.makeText(MainActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                } else {
                    if (DB.checkUsername(user)) {
                        Toast.makeText(MainActivity.this, "Username already exists", Toast.LENGTH_SHORT).show();
                    } else {
                        Boolean insert = DB.insertData(user, pass, userType);
                        if (insert) {
                            Toast.makeText(MainActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                            if (userType.equals("Professor")) {
                                Intent intent = new Intent(getApplicationContext(), HomeProfActivity.class);
                                startActivity(intent);
                            } else if (userType.equals("Student")) {
                                Intent intent = new Intent(getApplicationContext(), HomeStudentActivity.class);
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