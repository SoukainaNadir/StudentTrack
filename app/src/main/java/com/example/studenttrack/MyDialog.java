package com.example.studenttrack;

import static android.app.Activity.RESULT_OK;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;




import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class MyDialog extends DialogFragment {
    public static final String CLASS_ADD_DIALOG = "addClass";
    public static final String CLASS_UPDATE_DIALOG = "updateClass";
    public static final String STUDENT_ADD_DIALOG = "addStudent";
    public static final String STUDENT_UPDATE_DIALOG = "updateStudent";
    public static final String STUDENT_JUSTIFICATION = "justificationStudent";
    private final String name;
    private int apogee;
    private OnClickListener listener;

    private StudentDialogListener listener1;
    private JustificationDialogListener listener2;

    private TextView pdfNameTextView;
    public static final int PICK_PDF_REQUEST = 1;

    private int roll;
    public MyDialog(int roll, String name,int apogee) {
        this.roll=roll;
        this.name=name;
        this.apogee=apogee;
    }

    public MyDialog() {
        name = null;
    }


    public interface OnClickListener{
        void onClick(String text1, String text2);
    }

    public interface StudentDialogListener {
        void onStudentDialogClick(String roll, String name, String apogee);
    }

    public interface JustificationDialogListener {
        void onJustificationDialogClick(int apogee, String justification, String pdfPath);
    }

    public void setListener1(StudentDialogListener listener1) {
        this.listener1 = listener1;
    }
    public void setListener2(JustificationDialogListener listener2) {
        this.listener2 = listener2;
    }



    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }


    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = null;
        if (getTag().equals(CLASS_ADD_DIALOG))dialog=getAddClassDialog();
        if (getTag().equals(STUDENT_ADD_DIALOG))dialog=getAddStudentDialog();
        if (getTag().equals(CLASS_UPDATE_DIALOG))dialog=getUpdateClassDialog();
        if (getTag().equals(STUDENT_UPDATE_DIALOG))dialog=getUpdateStudentDialog();
        if (getTag().equals(STUDENT_JUSTIFICATION))dialog=getJustificationDialog();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return dialog;
    }

    private Dialog getJustificationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_justification, null);

        builder.setView(view);

        TextView title = view.findViewById(R.id.titleDialog);
        Button selectPdfBtn  = view.findViewById(R.id.select_pdf_btn);
        TextView pdfPathTextView = view.findViewById(R.id.pdfNameTextView);
        pdfPathTextView.setVisibility(View.GONE);

        title.setText("Add Justification");

        EditText apogee = view.findViewById(R.id.edt01);
        EditText justification = view.findViewById(R.id.edt02);

        apogee.setHint("Apogee");
        justification.setHint("Justification");

        Button cancel = view.findViewById(R.id.cancel_btn);
        Button add = view.findViewById(R.id.send_btn);

        cancel.setOnClickListener(v-> dismiss());
        add.setOnClickListener(v -> {
            String justificationText = justification.getText().toString();
            String apogeeString = apogee.getText().toString();
            int apogeeInt = Integer.parseInt(apogeeString);
            String pdfPath = pdfPathTextView.getText().toString();
            listener2.onJustificationDialogClick(apogeeInt,justificationText, pdfPath);
            dismiss();
        });

        selectPdfBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("application/pdf");
            startActivityForResult(Intent.createChooser(intent, "Select PDF"), PICK_PDF_REQUEST);
        });

        return builder.create();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri pdfUri = data.getData();
            String pdfPath = getPathFromUri(pdfUri, getContext());

            TextView pdfNameTextView = getDialog().findViewById(R.id.pdfNameTextView);
            pdfNameTextView.setText(getFileNameFromUri(pdfUri, getContext()));
            pdfNameTextView.setVisibility(View.VISIBLE);
        }
    }

    private String getPathFromUri(Uri uri, Context context) {
        String filePath = null;
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            filePath = cursor.getString(columnIndex);
            cursor.close();
        }
        return filePath;
    }



    private String getFileNameFromUri(Uri uri, Context context) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }















    private Dialog getUpdateStudentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_student, null);

        builder.setView(view);

        TextView title = view.findViewById(R.id.titleDialog);
        title.setText("Update Student");


        EditText roll_edt = view.findViewById(R.id.edt01);
        EditText name_edt = view.findViewById(R.id.edt02);
        EditText apogee_edt = view.findViewById(R.id.apogee);

        roll_edt.setHint("Roll");
        name_edt.setHint("Name");
        apogee_edt.setHint("Apogée");

        Button cancel = view.findViewById(R.id.cancel_btn);
        Button add = view.findViewById(R.id.add_btn);
        add.setText("Update");
        roll_edt.setText(roll+"");
        roll_edt.setEnabled(false);
        name_edt.setText(name);
        apogee_edt.setText(apogee+"");

        cancel.setOnClickListener(v-> dismiss());
        add.setOnClickListener(v-> {
            String roll = roll_edt.getText().toString();
            String name  = name_edt.getText().toString();
            String apogee = apogee_edt.getText().toString();

            listener1.onStudentDialogClick(roll,name,apogee);
            dismiss();

        });
        return builder.create();

    }

    private Dialog getUpdateClassDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog, null);

        builder.setView(view);

        TextView title = view.findViewById(R.id.titleDialog);
        title.setText("Update Class");


        EditText class_edt = view.findViewById(R.id.edt01);
        EditText subject_edt = view.findViewById(R.id.edt02);

        class_edt.setHint("Class Name");
        subject_edt.setHint("Subject Name");

        Button cancel = view.findViewById(R.id.cancel_btn);
        Button add = view.findViewById(R.id.add_btn);
        add.setText("Update");

        cancel.setOnClickListener(v-> dismiss());
        add.setOnClickListener(v-> {
            String className = class_edt.getText().toString();
            String subName  = subject_edt.getText().toString();
            listener.onClick(className,subName);
            dismiss();
        });
        return builder.create();
    }

    private Dialog getAddStudentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_student, null);

        builder.setView(view);

        TextView title = view.findViewById(R.id.titleDialog);
        title.setText("Add New Student");

        EditText roll_edt = view.findViewById(R.id.edt01);
        EditText name_edt = view.findViewById(R.id.edt02);
        EditText apogee_edt = view.findViewById(R.id.apogee);
        
        roll_edt.setHint("Roll");
        name_edt.setHint("Name");
        apogee_edt.setHint("Apogée");



        Button cancel = view.findViewById(R.id.cancel_btn);
        Button add = view.findViewById(R.id.add_btn);

        cancel.setOnClickListener(v-> dismiss());
        add.setOnClickListener(v-> {
            String roll = roll_edt.getText().toString();
            String name  = name_edt.getText().toString();
            String  apogee = apogee_edt.getText().toString();

            roll_edt.setText(String.valueOf(Integer.parseInt(roll)+1));
            name_edt.setText("");
            apogee_edt.setText("");
            listener1.onStudentDialogClick(roll, name, apogee);


        });
        return builder.create();

    }

    private Dialog getAddClassDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog, null);

        builder.setView(view);

        TextView title = view.findViewById(R.id.titleDialog);
        title.setText("Add New Class");


        EditText class_edt = view.findViewById(R.id.edt01);
        EditText subject_edt = view.findViewById(R.id.edt02);

        class_edt.setHint("Class Name");
        subject_edt.setHint("Subject Name");

        Button cancel = view.findViewById(R.id.cancel_btn);
        Button add = view.findViewById(R.id.add_btn);

        cancel.setOnClickListener(v-> dismiss());
        add.setOnClickListener(v-> {
            String className = class_edt.getText().toString();
            String subName  = subject_edt.getText().toString();
            listener.onClick(className,subName);
            dismiss();
        });
        return builder.create();
    }

}
