package com.example.studentmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DBHelper {
    String DATABASE_NAME = "StudentsDB.db";
    private static final String DB_PATH_SUFFIX = "/databases/";
    SQLiteDatabase db = null;
    Context context;

    public DBHelper(Context context) {
        this.context = context;
        processSQLite();
    }

    private void processSQLite() {
        File dbFile = context.getDatabasePath(DATABASE_NAME);
        if(!dbFile.exists()){
            try{
                CopyDatabaseFromAsset();
                Toast.makeText(context, "Copy thanh cong.", Toast.LENGTH_SHORT).show();

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void CopyDatabaseFromAsset() {
        try{
            InputStream databaseInputStream = context.getAssets().open(DATABASE_NAME);
            String outputStream = getPathDatabaseSystem();

            File file = new File(context.getApplicationInfo().dataDir + DB_PATH_SUFFIX);
            if(!file.exists()){
                file.mkdir();
            }

            OutputStream databaseOutputStream = new FileOutputStream(outputStream);

            byte[] buffer = new byte[1024];
            int length;
            while((length=databaseInputStream.read(buffer)) > 0){
                databaseOutputStream.write(buffer,0,length);
            }

            databaseOutputStream.flush();
            databaseOutputStream.close();
            databaseInputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private String getPathDatabaseSystem() {
        return  context.getApplicationInfo().dataDir + DB_PATH_SUFFIX+DATABASE_NAME;
    }

    public ArrayList<Student> getAllStudent(){
        ArrayList<Student> arrayList = new ArrayList<>();

        db = context.openOrCreateDatabase(DATABASE_NAME, context.MODE_PRIVATE, null);
        String sql = "SELECT * FROM Students";

        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()){
            String id = cursor.getString(0);
            String name = cursor.getString(1);
            String dob = cursor.getString(2);
            String email = cursor.getString(3);
            String address = cursor.getString(4);

            arrayList.add(new Student(id, name, dob, email, address));
        }
        return arrayList;
    }

    public void insertStudent(Student student){
        db=context.openOrCreateDatabase(DATABASE_NAME, context.MODE_PRIVATE, null);

        ContentValues contentValues = new ContentValues();
        contentValues.put("id", student.getID());
        contentValues.put("name", student.getName());
        contentValues.put("dob", student.getDoB());
        contentValues.put("email", student.getEmail());
        contentValues.put("Address", student.getAddress());

        if(db.insert("Students", null, contentValues)>0){
            Toast.makeText(context, "them thanh cong.", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateStudent(Student student){
        db=context.openOrCreateDatabase(DATABASE_NAME, context.MODE_PRIVATE, null);

        ContentValues contentValues = new ContentValues();
        contentValues.put("id", student.getID());
        contentValues.put("name", student.getName());
        contentValues.put("dob", student.getDoB());
        contentValues.put("email", student.getEmail());
        contentValues.put("Address", student.getAddress());

        if(db.update("Students", contentValues, "id="+student.getID(), null)>0){
            Toast.makeText(context, "thay doi thanh cong.", Toast.LENGTH_SHORT).show();
        }
    }
    public void deleteStudent(Student student){
        db=context.openOrCreateDatabase(DATABASE_NAME, context.MODE_PRIVATE, null);
        if(db.delete("Students", "id="+student.getID(), null)>0){
            Toast.makeText(context, "xoa thanh cong.", Toast.LENGTH_SHORT).show();
        }
    }
}
