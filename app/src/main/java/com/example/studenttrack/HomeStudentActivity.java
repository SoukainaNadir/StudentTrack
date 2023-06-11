package com.example.studenttrack;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class HomeStudentActivity extends AppCompatActivity {

    private TextView welcome_text;

    String username;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_student);
        welcome_text = findViewById(R.id.welcome_text);

        username = getIntent().getStringExtra("username");

        welcome_text.setText("Welcome " + username + " !");
    }
}