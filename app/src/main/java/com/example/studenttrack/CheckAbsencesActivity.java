package com.example.studenttrack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;


public class CheckAbsencesActivity extends AppCompatActivity {
    RecyclerView check_absences;

    RecyclerView.LayoutManager layoutManager;
    AbsenceDetailsAdapter adapter;
    ArrayList<AbsenceDetails> absenceDetailsList = new ArrayList<>();
    DBHelper dbHelper;
    String username;

    private ArrayList<justificationitem> justificationitems = new ArrayList<>();

    private long sid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_absences);

        absenceDetailsList = new ArrayList<>();

        username = getIntent().getStringExtra("username");
        dbHelper = new DBHelper(this);

        check_absences = findViewById(R.id.check_absences);
        layoutManager = new LinearLayoutManager(this);
        check_absences.setLayoutManager(layoutManager);
        adapter = new AbsenceDetailsAdapter(this, absenceDetailsList);
        check_absences.setAdapter(adapter);
        loadData();



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
                justifyDialog(item.getGroupId());
                break;

        }

        return super.onContextItemSelected(item);
    }

    private void justifyDialog(int position) {
        MyDialog dialog = new MyDialog();
        dialog.show(getSupportFragmentManager(),MyDialog.STUDENT_JUSTIFICATION);
        dialog.setListener2((justification) -> addJustification(position,justification));
    }

    private void addJustification(int position,String justification) {

        

    }


}


















