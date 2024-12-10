package com.coursemate.models;

public class Student {
    private int studentId;
    private int userId;
    private String fullName;

    public Student(int studentId, int userId, String fullName) {
        this.studentId = studentId;
        this.userId = userId;
        this.fullName = fullName;
    }

    public int getStudentId() {
        return studentId;
    }

    public int getUserId() {
        return userId;
    }

    public String getFullName() {
        return fullName;
    }

    @Override
    public String toString() {
        return "Student ID: " + studentId + ", Name: " + fullName + ", User ID: " + userId;
    }
}
