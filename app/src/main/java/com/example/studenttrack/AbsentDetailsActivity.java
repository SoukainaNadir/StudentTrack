package com.example.studenttrack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class AbsentDetailsActivity extends AppCompatActivity {


    private String studentName;

    private int roll;
    private int apogee;

    private TextView rollTextView;
    private TextView apogee_textview;

    private DBHelper dbHelper;
    private TextView attendance_count_textview;

    
    private TextView justificationTextView;
    private String pdfFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absent_details);

        dbHelper = new DBHelper(this);

        studentName = getIntent().getStringExtra("studentName");
        roll = getIntent().getIntExtra("roll", 0);
        apogee = getIntent().getIntExtra("apogee", 0);


        rollTextView = findViewById(R.id.roll_textview);
        rollTextView.setText(String.valueOf(roll));

        apogee_textview = findViewById(R.id.apogee_textview);
        String apogeeValue = String.valueOf(apogee);

        // Fetch and display the updated apogee from the database
        int updatedApogee = dbHelper.getStudentApogee(roll);
        if (updatedApogee != -1) {
            apogeeValue = String.valueOf(updatedApogee);
        }

        apogee_textview.setText(apogeeValue);

        // Find the TextView in your layout
        attendance_count_textview = findViewById(R.id.attendance_count_textview);
        // Retrieve the cursor with absence count for each student
        Cursor cursor = dbHelper.countStudentAbsences();

        // Find the absence count for the student with the specific roll
        int absenceCount = 0;
        if (cursor.moveToFirst()) {
            do {
                int sid = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.S_ID));

                if (sid == roll) {
                    absenceCount = cursor.getInt(cursor.getColumnIndexOrThrow("AbsenceCount"));
                    break;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();

        // Set the absence count string as the text of the TextView
        attendance_count_textview.setText(String.valueOf(absenceCount));

        setToolbar();

    }




    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);

        TextView title = toolbar.findViewById(R.id.title_toolbar);
        TextView subtitle = toolbar.findViewById(R.id.subtitle_toolbar);

        title.setText(studentName);
            ImageButton backButton = findViewById(R.id.back);

            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle back button click
                    onBackPressed();
                }
            });

        // Hide the save button
        ImageButton saveButton = findViewById(R.id.save);
        ImageButton logout = findViewById(R.id.logout);
        saveButton.setVisibility(View.GONE);
        logout.setVisibility(View.GONE);

        subtitle.setVisibility(View.GONE);

        }
}