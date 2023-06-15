package com.example.studenttrack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class HomeStudentActivity extends AppCompatActivity {

    private TextView welcome_text;

    String username;
    String apogeeValue;
    Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_student);
        welcome_text = findViewById(R.id.welcome_text);

        username = getIntent().getStringExtra("username");
        apogeeValue = getIntent().getStringExtra("apogee");


        welcome_text.setText("Welcome " + username + " !");

        setToolbar();
    }

    private void setToolbar() {
        toolbar = findViewById(R.id.toolbar);
        TextView title = toolbar.findViewById(R.id.title_toolbar);
        TextView subtitle = toolbar.findViewById(R.id.subtitle_toolbar);
        ImageButton back = toolbar.findViewById(R.id.back);
        ImageButton save = toolbar.findViewById(R.id.save);
        ImageButton logout = toolbar.findViewById(R.id.logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle logout button click
                performLogout();
            }
        });

        title.setText("StudentTrack");
        subtitle.setVisibility(View.GONE);
        back.setVisibility(View.INVISIBLE);
        save.setVisibility(View.INVISIBLE);
    }

    private void performLogout() {


        Intent intent = new Intent(HomeStudentActivity.this, LoginActivity.class);
        intent.putExtra("message", "Logout successful");
        startActivity(intent);

        // Finish the current activity (optional)
        finish();
    }

    public void onCheckAbsencesClicked(View view) {

        Intent intent = new Intent(this, CheckAbsencesActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("apogee", apogeeValue);
        startActivity(intent);
    }
}