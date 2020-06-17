package com.example.studentmanager;

public class Student {
    private String id;
    private String name;
    private String Dob;
    private String Email;
    private String Address;

    public Student(){

    }
    public Student(String id, String name, String dob, String email, String address) {
        this.id = id;
        this.name = name;
        Dob = dob;
        Email = email;
        Address = address;
    }

    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDoB() {
        return Dob;
    }

    public void setDoB(String dob) {
        Dob = dob;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }
}
