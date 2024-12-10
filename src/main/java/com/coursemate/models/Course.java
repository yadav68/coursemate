package com.coursemate.models;

public class Course {
    private int courseId;
    private String courseName;
    private String description;
    private int credits;

    public Course(int courseId, String courseName, String description, int credits) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.description = description;
        this.credits = credits;
    }

    public int getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getDescription() {
        return description;
    }

    public int getCredits() {
        return credits;
    }

    @Override
    public String toString() {
        return "Course ID: " + courseId + ", Name: " + courseName +
                ", Description: " + description + ", Credits: " + credits;
    }
}
