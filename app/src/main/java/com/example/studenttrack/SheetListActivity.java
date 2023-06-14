package com.example.studenttrack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;

public class SheetListActivity extends AppCompatActivity {
    private ListView sheetList;
    private ArrayAdapter adapter;
    private ArrayList<String> listItems = new ArrayList();
    private long cid;
    Toolbar toolbar;
    private String className;
    private String subjectName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheet_list);

        cid = getIntent().getLongExtra("cid",-1);
        loadListItems();
        sheetList = findViewById(R.id.sheetList);
        adapter = new ArrayAdapter(this,R.layout.sheet_list,R.id.date_list_item,listItems);
        sheetList.setAdapter(adapter);

        sheetList.setOnItemClickListener((parent,view,position,id)->openSheetActivity(position));

        cid = getIntent().getLongExtra("cid", -1);
        className = getIntent().getStringExtra("className");
        subjectName = getIntent().getStringExtra("subjectName");

        setToolbar();
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);


        // Find the views for title, subtitle, and back button
        TextView title = toolbar.findViewById(R.id.title_toolbar);
        TextView subtitle = toolbar.findViewById(R.id.subtitle_toolbar);
        ImageButton backButton = findViewById(R.id.back);




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

        // Hide the save button
        ImageButton saveButton = findViewById(R.id.save);
        saveButton.setVisibility(View.GONE);
    }
    private void openSheetActivity(int position) {
        long[] idArray = getIntent().getLongArrayExtra("idArray");
        int[] rollArray = getIntent().getIntArrayExtra("rollArray");
        String[] nameArray = getIntent().getStringArrayExtra("nameArray");
        Intent intent = new Intent(this, SheetActivity.class);
        intent.putExtra("idArray", idArray);
        intent.putExtra("rollArray", rollArray);
        intent.putExtra("nameArray", nameArray);
        intent.putExtra("month", listItems.get(position));
        intent.putExtra("className", className);
        intent.putExtra("subjectName", subjectName);

        startActivity(intent);

    }

    private void loadListItems() {
        Cursor cursor = new DBHelper(this).getDistinctMonths(cid);

        while(cursor.moveToNext()){
            String date = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.DATE_KEY));
            listItems.add(date.substring(3));
        }
    }

}