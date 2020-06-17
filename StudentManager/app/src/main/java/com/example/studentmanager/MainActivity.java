package com.example.studentmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<Student> arrayList;
    StudentAdapter studentAdapter;
    DBHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DBHelper(MainActivity.this);

        listView = findViewById(R.id.list);

        arrayList = db.getAllStudent();
        studentAdapter = new StudentAdapter(MainActivity.this, R.layout.layout_row, arrayList);

        listView.setAdapter(studentAdapter);

        registerForContextMenu(listView);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menu_add){
            final Student student = new Student();

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

            builder.setTitle("them sinh vien");
            builder.setCancelable(false);

            LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
            View view = inflater.inflate(R.layout.layout_input, null);

            final EditText editID = view.findViewById(R.id.editID);
            final EditText editName = view.findViewById(R.id.editName);
            final EditText editDoB = view.findViewById(R.id.editDoB);
            final EditText editEmail = view.findViewById(R.id.editEmail);
            final EditText editAddress = view.findViewById(R.id.editAddress);

            builder.setView(view);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    student.setID(editID.getText().toString());
                    student.setName(editName.getText().toString());
                    student.setDoB(editDoB.getText().toString());
                    student.setAddress(editAddress.getText().toString());
                    student.setEmail(editEmail.getText().toString());

                    db.insertStudent(student);
                    arrayList.add(student);
                    studentAdapter.notifyDataSetChanged();
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.menu_update:
                final Student student = arrayList.get(info.position);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setTitle("cap nhap: ");
                builder.setCancelable(false);

                LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                View view = inflater.inflate(R.layout.layout_input, null);

                final EditText editID = view.findViewById(R.id.editID);
                final EditText editName = view.findViewById(R.id.editName);
                final EditText editDoB = view.findViewById(R.id.editDoB);
                final EditText editEmail = view.findViewById(R.id.editEmail);
                final EditText editAddress = view.findViewById(R.id.editAddress);

                builder.setView(view);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        student.setID(editID.getText().toString());
                        student.setName(editName.getText().toString());
                        student.setDoB(editDoB.getText().toString());
                        student.setAddress(editAddress.getText().toString());
                        student.setEmail(editEmail.getText().toString());

                        db.updateStudent(student);
                        arrayList.add(student);
                        studentAdapter.notifyDataSetChanged();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                break;
            case R.id.menu_xoa:
                final Student student1 = arrayList.get(info.position);

                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);

                builder1.setTitle("xoa");
                builder1.setCancelable(false);
                builder1.setMessage("an ok de xoa");

                builder1.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.deleteStudent(student1);
                        arrayList.remove(info.position);
                        studentAdapter.notifyDataSetChanged();
                    }
                });

                builder1.setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog alertDialog1 = builder1.create();
                alertDialog1.show();
                break;
            }


        return super.onContextItemSelected(item);
    }
}
