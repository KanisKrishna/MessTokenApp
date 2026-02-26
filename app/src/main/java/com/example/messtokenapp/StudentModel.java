package com.example.messtokenapp;

public class StudentModel {

    public String roll;
    public String dept;
    public String year;

    // Required empty constructor for Firebase
    public StudentModel() {
    }

    public StudentModel(String roll, String dept, String year) {
        this.roll = roll;
        this.dept = dept;
        this.year = year;
    }
}