package com.coursemate.models;

public class Teacher {
    private int teacherId;
    private int userId;
    private String fullName;
    private String specialization;

    public Teacher(int teacherId, int userId, String fullName, String specialization) {
        this.teacherId = teacherId;
        this.userId = userId;
        this.fullName = fullName;
        this.specialization = specialization;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public int getUserId() {
        return userId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getSpecialization() {
        return specialization;
    }

    @Override
    public String toString() {
        return "Teacher ID: " + teacherId + ", Name: " + fullName +
                ", Specialization: " + specialization + ", User ID: " + userId;
    }
}
