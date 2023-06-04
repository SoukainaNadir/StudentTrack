package com.example.studenttrack;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class HomeProfActivity extends AppCompatActivity {
    FloatingActionButton fab;
    RecyclerView recyclerView;
    ClassAdapter classAdapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Classitem> classitems =new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homeprof);

        fab = findViewById(R.id.fab_main);
        fab.setOnClickListener(v->showDialog());
        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        classAdapter = new ClassAdapter(this,classitems);
        recyclerView.setAdapter(classAdapter);
        classAdapter.setOnItemClickListener(position -> gotoItemActivity(position));
    }

    private void gotoItemActivity(int position) {
        Intent intent = new Intent(this,StudentActivity.class);

        intent.putExtra("ClassName", classitems.get(position).getClass());
        intent.putExtra("subjectName", classitems.get(position).getClass());
        intent.putExtra("position", position);
        startActivity(intent);
    }

    private void showDialog(){
        MyDialog dialog = new MyDialog();
        dialog.show(getSupportFragmentManager(),MyDialog.CLASS_ADD_DIALOG);
        dialog.setListener((className, subjectName)->addClass(className,subjectName));

    }

    private void addClass(String className,String subjectName){

        classitems.add(new Classitem(className,subjectName));
        classAdapter.notifyDataSetChanged();

    }
}