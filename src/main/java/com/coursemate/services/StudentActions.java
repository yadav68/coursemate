package com.coursemate.services;

import com.coursemate.dao.CourseDAO;
import com.coursemate.dao.StudentDAO;
import com.coursemate.dao.TeacherDAO;
import com.coursemate.models.Course;

import java.sql.SQLException;
import java.util.List;

public class StudentActions {
    private final CourseDAO courseDAO = new CourseDAO();
    private final StudentDAO studentDAO = new StudentDAO();
    private final TeacherDAO teacherDAO = new TeacherDAO();

//    public List<Course> viewEnrolledCourses(int studentId) throws SQLException {
//        return courseDAO.getEnrolledCourses(studentId);
//    }

    // View courses enrolled by the student
    public List<Course> viewEnrolledCourses(int studentId) throws SQLException {
        return studentDAO.getEnrolledCourses(studentId);
    }

    // View the list of teachers and the courses they teach
    public List<String> viewTeachersAndCourses() throws SQLException {
        return teacherDAO.getTeachersAndCourses();
    }
}
