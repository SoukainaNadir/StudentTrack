package com.example.studenttrack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    EditText username, password;
    Button signin;

    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username1);
        password = findViewById(R.id.password1);
        signin = findViewById(R.id.signin1);

        // Retrieve the message from the intent
        String message = getIntent().getStringExtra("message");

        // Display the success message if available
        if (message != null) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }


        DB = new DBHelper(this);


        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = username.getText().toString();
                String pass = password.getText().toString();

                if (TextUtils.isEmpty(user) || TextUtils.isEmpty(pass)) {
                    Toast.makeText(LoginActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                } else {
                    String userType = DB.getUserType(user); // Get the user type from the DBHelper

                    if (userType.equals("Professor")) {
                        Boolean checkuserpass = DB.checkUsernamePassword(user, pass);
                        if (checkuserpass) {

                            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), HomeProfActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                        }
                    } else if (userType.equals("Student")) {
                        Boolean checkuserpass = DB.checkUsernamePassword(user, pass);
                        if (checkuserpass) {
                            String apogeeValue = DB.getApogee(user);
                            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), HomeStudentActivity.class);
                            intent.putExtra("username", user);
                            intent.putExtra("apogee", apogeeValue);
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });



    }
}