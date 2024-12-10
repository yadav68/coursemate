package com.coursemate.services;

import com.coursemate.dao.CourseDAO;
import com.coursemate.dao.StudentDAO;
import com.coursemate.dao.TeacherDAO;
import com.coursemate.models.Course;
import com.coursemate.models.Student;

import java.sql.SQLException;
import java.util.List;

public class TeacherActions {
    private final TeacherDAO teacherDAO = new TeacherDAO();
    private final StudentDAO studentDAO = new StudentDAO();

    // View courses assigned to a specific teacher
    public List<Course> viewAssignedCourses(int teacherId) throws SQLException {
        return teacherDAO.getAssignedCourses(teacherId);
    }

    // Retrieve students for the teacher's courses
    public List<Student> viewStudentsForMyCourses(int teacherId) throws SQLException {
        return studentDAO.getStudentsForTeacher(teacherId);
    }
}
