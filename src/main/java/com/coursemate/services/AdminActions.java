package com.coursemate.services;

import com.coursemate.config.DBConnection;
import com.coursemate.dao.CourseDAO;
import com.coursemate.dao.TeacherDAO;
import com.coursemate.dao.StudentDAO;
import com.coursemate.dao.UserDAO;
import com.coursemate.models.Course;
import com.coursemate.models.Teacher;
import com.coursemate.models.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class AdminActions {
    private final CourseDAO courseDAO = new CourseDAO();
    private final TeacherDAO teacherDAO = new TeacherDAO();
    private final StudentDAO studentDAO = new StudentDAO();
    private final UserDAO userDAO = new UserDAO();
    public void addCourse(Course course) throws SQLException {
        courseDAO.addCourse(course);
    }

    public List<Course> viewAllCourses() throws SQLException {
        return courseDAO.getAllCourses();
    }

    public void updateCourse(Course course) throws SQLException {
        courseDAO.updateCourse(course);
    }

    public void deleteCourse(int courseId) throws SQLException {
        courseDAO.deleteCourse(courseId);
    }

    // Retrieve teacher details along with their assigned courses
    public Map<Teacher, List<String>> getTeachersWithCourses() throws SQLException {
        return teacherDAO.getTeachersWithCourses();
    }

    public void updateTeacher(Teacher teacher) throws SQLException {
        teacherDAO.updateTeacher(teacher);
    }

    public void deleteTeacher(int teacherId) throws SQLException {
        teacherDAO.deleteTeacher(teacherId);
    }

    public List<Student> viewAllStudents() throws SQLException {
        return studentDAO.getAllStudents();
    }

    public void updateStudent(Student student) throws SQLException {
        studentDAO.updateStudent(student);
    }

    public void deleteStudent(int studentId) throws SQLException {
        studentDAO.deleteStudent(studentId);
    }

    // Add a new teacher
    public void addTeacher(Teacher teacher, String username, String password) throws SQLException {
        teacherDAO.addTeacher(teacher, username, password); // Delegate to TeacherDAO
    }



    // Add a new student
    public void addStudent(Student student, String username, String password) throws SQLException {
        studentDAO.addStudent(student, username, password); // Delegate to StudentDAO
    }

    // Assign a course to a teacher
//    public void assignCourseToTeacher(int teacherId, int courseId) throws SQLException {
//        teacherDAO.assignCourseToTeacher(teacherId, courseId);
//        System.out.println("Course assigned successfully.");
//    }

    public void assignCourseToTeacher(int teacherId, int courseId) throws SQLException {
        teacherDAO.assignCourseToTeacher(teacherId, courseId);
    }

    // Assign a student to a course
    public void assignStudentToCourse(int studentId, int courseId) throws SQLException {
        studentDAO.assignStudentToCourse(studentId, courseId);
    }

}

