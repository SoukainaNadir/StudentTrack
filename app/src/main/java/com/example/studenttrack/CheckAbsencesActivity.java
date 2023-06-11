package com.example.studenttrack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;

import java.util.ArrayList;


public class CheckAbsencesActivity extends AppCompatActivity {
    RecyclerView check_absences;

    RecyclerView.LayoutManager layoutManager;
    AbsenceDetailsAdapter adapter;
    ArrayList<AbsenceDetails> absenceDetailsList = new ArrayList<>();
    DBHelper dbHelper;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_absences);

        username = getIntent().getStringExtra("username");
        dbHelper = new DBHelper(this);
        loadData();
        check_absences = findViewById(R.id.check_absences);
        layoutManager = new LinearLayoutManager(this);
        check_absences.setLayoutManager(layoutManager);
        adapter = new AbsenceDetailsAdapter(this, absenceDetailsList);
        check_absences.setAdapter(adapter);

    }

    private void loadData() {
        Cursor cursor = dbHelper.getStatusTable();

        absenceDetailsList.clear();
        int idColumnIndex = cursor.getColumnIndex(DBHelper.S_ID);
        int dateColumnIndex = cursor.getColumnIndex(DBHelper.DATE_KEY);
        int statusColumnIndex = cursor.getColumnIndex(DBHelper.STATUS_KEY);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(idColumnIndex);
            String date = (dateColumnIndex != -1) ? cursor.getString(dateColumnIndex) : null;
            String status = (statusColumnIndex != -1) ? cursor.getString(statusColumnIndex) : null;

            int apogee = dbHelper.getStudentApogee(id);
            String userApogee = dbHelper.getApogeeForUser(username);
            String subjectName = dbHelper.getSubjectName(username);

            if (String.valueOf(apogee).equals(userApogee)&& "A".equals(status)) {
                absenceDetailsList.add(new AbsenceDetails(id, date, status,subjectName));
            }
        }
    }
}















