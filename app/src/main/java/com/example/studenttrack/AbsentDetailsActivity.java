package com.example.studenttrack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class AbsentDetailsActivity extends AppCompatActivity {

    private String className;
    private String subjectName;

    private int roll;


    private TextView rollTextView;

    private DBHelper dbHelper;
    TextView attendance_count_textview;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absent_details);


        className = getIntent().getStringExtra("className");
        subjectName = getIntent().getStringExtra("subjectName");
        roll = getIntent().getIntExtra("roll", 0);




        // Find the TextView in your layout
        attendance_count_textview = findViewById(R.id.attendance_count_textview);

        // Create an instance of DBHelper
        dbHelper = new DBHelper(this);

        // Retrieve the cursor with absence count for each student
        Cursor cursor = dbHelper.countStudentAbsences();

        // Find the absence count for the student with the specific roll
        int absenceCount = 0;
        if (cursor.moveToFirst()) {
            do {
                int studentId = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.S_ID));
                if (studentId == roll) {
                    absenceCount = cursor.getInt(cursor.getColumnIndexOrThrow("AbsenceCount"));
                    break;
                }
            } while (cursor.moveToNext());
        }
        cursor.close();

        // Set the absence count string as the text of the TextView
        attendance_count_textview.setText(String.valueOf(absenceCount));


        rollTextView = findViewById(R.id.roll_textview);
        rollTextView.setText(String.valueOf(roll));

        setToolbar();


    }





    private void setToolbar() {
            Toolbar toolbar = findViewById(R.id.toolbar);


            // Find the views for title, subtitle, and back button
            TextView title = toolbar.findViewById(R.id.title_toolbar);
            TextView subtitle = toolbar.findViewById(R.id.subtitle_toolbar);
            ImageButton backButton = findViewById(R.id.back);


            Log.d("SheetActivity", "className: " + className);
            Log.d("SheetActivity", "subjectName: " + subjectName);

            // Set the title and subtitle text
            title.setText(className);
            subtitle.setText(subjectName);

            // Set a click listener for the back button
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle back button click
                    onBackPressed();
                }
            });
        }
}