package com.example.studentmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import java.util.ArrayList;

public class StudentAdapter extends ArrayAdapter<Student>{

    Context context;
    int layout;
    ArrayList<Student> arrayList;
    public StudentAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<Student> objects){
        super(context, resource, objects);
        this.context =context;
        this.layout=resource;
        this.arrayList=objects;
    }
    @Override
    public View getView(int position, View view, ViewGroup parant){
        LayoutInflater inflater = LayoutInflater.from(context);
        View convertView = inflater.inflate(layout, null);

        TextView textView = convertView.findViewById(R.id.txtMSV);
        TextView textView1 = convertView.findViewById(R.id.txtName);
        TextView textView2 = convertView.findViewById(R.id.txtDoB);
        TextView textView3 = convertView.findViewById(R.id.txtEmail);
        TextView textView4 = convertView.findViewById(R.id.txtAddress);

        if(!arrayList.isEmpty()) {
            textView.setText("MSSV " + arrayList.get(position).getID());
            textView1.setText("Ten " + arrayList.get(position).getName());
            textView2.setText("Ngay sinh " + arrayList.get(position).getDoB());
            textView3.setText("Email " + arrayList.get(position).getEmail());
            textView4.setText("Dia chi  " + arrayList.get(position).getAddress());
        }
        return convertView;
    }
}
