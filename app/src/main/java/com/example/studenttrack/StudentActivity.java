package com.example.studenttrack;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class StudentActivity extends AppCompatActivity {
    Toolbar toolbar;
    private String className;
    private String subjectName;
    private int position;
    private RecyclerView recyclerView;
    private StudentAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Studentitem> studentitems = new ArrayList<>();
    private DBHelper dbHelper;
    private int cid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        dbHelper = new DBHelper(this);
        Intent intent = getIntent();
        className = intent.getStringExtra("className");
        subjectName = intent.getStringExtra("subjectName");
        position = intent.getIntExtra("position", -1);
        cid = intent.getIntExtra("cid", -1);


        setToolbar();
        loadData();
        recyclerView = findViewById(R.id.student_recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new StudentAdapter(this, studentitems);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(position -> changeStatus(position));

    }

    private void loadData() {
        Cursor cursor = dbHelper.getStudentTable(cid);
        studentitems.clear();
        while (cursor.moveToNext()){
            long sid = cursor.getLong(cursor.getColumnIndexOrThrow(DBHelper.S_ID));
            int roll = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.STUDENT_ROLL_KEY));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.STUDENT_NAME_KEY));
            studentitems.add(new Studentitem(sid, roll, name));
        }
        cursor.close();
    }

    private void changeStatus(int position) {
        String status = studentitems.get(position).getStatus();

        if (status.equals("P")) status = "A";
        else status = "P";

        studentitems.get(position).setStatus(status);
        adapter.notifyItemChanged(position);
    }

    private void setToolbar() {
        toolbar = findViewById(R.id.toolbar);
        TextView title = toolbar.findViewById(R.id.title_toolbar);
        TextView subtitle = toolbar.findViewById(R.id.subtitle_toolbar);
        ImageButton back = toolbar.findViewById(R.id.back);
        ImageButton save = toolbar.findViewById(R.id.save);

        title.setText(className);
        subtitle.setText(subjectName);

        back.setOnClickListener(v->onBackPressed());
        toolbar.inflateMenu(R.menu.student_menu);
        toolbar.setOnMenuItemClickListener(menuItem->onMenuItemClick(menuItem));

    }

    private boolean onMenuItemClick(MenuItem menuItem) {
        if(menuItem.getItemId()== R.id.add_student){
            showAddStudentDialog();
        }
        return true;
    }

    private void showAddStudentDialog() {
        MyDialog dialog = new MyDialog();
        dialog.show(getSupportFragmentManager(),MyDialog.STUDENT_ADD_DIALOG);
        dialog.setListener((roll,name)->addStudent(roll,name));
    }

    private void addStudent(String roll_string, String name) {
        int roll = Integer.parseInt(roll_string);
        long sid = dbHelper.addStudent(cid,roll,name);
        Studentitem  studentitem = new Studentitem(sid,roll,name);
        studentitems.add(studentitem);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onContextItemSelected(@Nullable MenuItem item) {
        switch (item.getItemId()){
            case 0:
                showUpdateStudentDialog(item.getGroupId());
                break;
            case 1:
                deleteStudent(item.getGroupId());
        }

        return super.onContextItemSelected(item);
    }

    private void showUpdateStudentDialog(int position) {
        MyDialog dialog = new MyDialog(studentitems.get(position).getRoll(),studentitems.get(position).getName());
        dialog.show(getSupportFragmentManager(),MyDialog.STUDENT_UPDATE_DIALOG);
        dialog.setListener((roll_string,name)->updateStudent(position,name));
    }

    private void updateStudent(int position, String name) {
        dbHelper.updateStudent(studentitems.get(position).getSid(),name);
        studentitems.get(position).setName(name);
        adapter.notifyItemChanged(position);
    }


    private void deleteStudent(int position) {
        dbHelper.deleteStudent(studentitems.get(position).getSid());
        studentitems.remove(position);
        adapter.notifyItemRemoved(position);
    }
}