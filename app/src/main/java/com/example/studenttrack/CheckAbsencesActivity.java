package com.example.studenttrack;

import static com.example.studenttrack.MyDialog.PICK_PDF_REQUEST;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.net.Uri;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;


public class CheckAbsencesActivity extends AppCompatActivity {
    RecyclerView check_absences;

    RecyclerView.LayoutManager layoutManager;
    AbsenceDetailsAdapter adapter;
    ArrayList<AbsenceDetails> absenceDetailsList = new ArrayList<>();
    DBHelper dbHelper;
    String username;
    String pdfPath;
    String justification;
    String apogeeValue;

    private ArrayList<justificationitem> justificationitems = new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_absences);

        absenceDetailsList = new ArrayList<>();

        username = getIntent().getStringExtra("username");
        apogeeValue = getIntent().getStringExtra("apogee");

        dbHelper = new DBHelper(this);

        check_absences = findViewById(R.id.check_absences);
        layoutManager = new LinearLayoutManager(this);
        check_absences.setLayoutManager(layoutManager);
        adapter = new AbsenceDetailsAdapter(this, absenceDetailsList);
        check_absences.setAdapter(adapter);
        loadData();
        toolbar();
    }

    private void toolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);

        TextView title = toolbar.findViewById(R.id.title_toolbar);
        TextView subtitle = toolbar.findViewById(R.id.subtitle_toolbar);

        ImageButton backButton = findViewById(R.id.back);
        ImageButton logout = findViewById(R.id.logout);

        title.setText(username);
        subtitle.setText(apogeeValue);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle back button click
                onBackPressed();
            }
        });

        // Hide the save button
        ImageButton saveButton = findViewById(R.id.save);
        saveButton.setVisibility(View.GONE);
        logout.setVisibility(View.GONE);

    }


    private void loadData() {

        absenceDetailsList.clear();

        String userApogee = dbHelper.getApogeeForUser(username);

        // Replace "userApogee" with the actual user's apogee value
        ArrayList<AbsenceDetails> retrievedList = dbHelper.getAbsencesByUserApogee(userApogee);

        absenceDetailsList.addAll(retrievedList);

        // Notify the adapter that the data has changed
        adapter.notifyDataSetChanged();

    }

    @Override
    public boolean onContextItemSelected(@Nullable MenuItem item) {
        switch (item.getItemId()){
            case 0:
                justifyDialog();
                break;

        }
        return super.onContextItemSelected(item);
    }

    private void justifyDialog() {
        MyDialog dialog = new MyDialog();
        dialog.show(getSupportFragmentManager(),MyDialog.STUDENT_JUSTIFICATION);
        dialog.setListener2((apogee,justification, pdfPath) -> addJustification(apogee,justification,pdfPath));
    }

    private void addJustification(int apogee, String justification, String pdfPath) {

        Boolean isSuccess = dbHelper.addJustification(apogee,justification);

        if (isSuccess) {
            // Show a success message
            Toast.makeText(this, "Justification added successfully", Toast.LENGTH_SHORT).show();
            // Make the justifiedTextView visible
            TextView justifiedTextView = findViewById(R.id.justifiedTextView);
            justifiedTextView.setVisibility(View.VISIBLE);

        } else {
            // Show an error message
            Toast.makeText(this, "Failed to add justification", Toast.LENGTH_SHORT).show();
        }
    }















}


















