package com.coursemate.dao;

import com.coursemate.config.DBConnection;
import com.coursemate.models.Course;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {
    public void addCourse(Course course) throws SQLException {
        String query = "INSERT INTO Courses (CourseName, Description, Credits) VALUES (?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, course.getCourseName());
            statement.setString(2, course.getDescription());
            statement.setInt(3, course.getCredits());
            statement.executeUpdate();
        }
    }

    public List<Course> getAllCourses() throws SQLException {
        List<Course> courses = new ArrayList<>();
        String query = "SELECT * FROM Courses";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                courses.add(new Course(
                        resultSet.getInt("CourseID"),
                        resultSet.getString("CourseName"),
                        resultSet.getString("Description"),
                        resultSet.getInt("Credits")
                ));
            }
        }
        return courses;
    }

    public void updateCourse(Course course) throws SQLException {
        String query = "UPDATE Courses SET CourseName = ?, Description = ?, Credits = ? WHERE CourseID = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, course.getCourseName());
            statement.setString(2, course.getDescription());
            statement.setInt(3, course.getCredits());
            statement.setInt(4, course.getCourseId());
            statement.executeUpdate();
        }
    }

    public void deleteCourse(int courseId) throws SQLException {
        String query = "DELETE FROM Courses WHERE CourseID = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, courseId);
            statement.executeUpdate();
        }
    }
    // Retrieve courses enrolled by a specific student
    public List<Course> getEnrolledCourses(int studentId) throws SQLException {
        List<Course> courses = new ArrayList<>();
        String query = """
                SELECT c.CourseID, c.CourseName, c.Description, c.Credits
                FROM StudentCourses sc
                JOIN Courses c ON sc.CourseID = c.CourseID
                WHERE sc.StudentID = ?
                """;
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, studentId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    courses.add(new Course(
                            resultSet.getInt("CourseID"),
                            resultSet.getString("CourseName"),
                            resultSet.getString("Description"),
                            resultSet.getInt("Credits")
                    ));
                }
            }
        }
        return courses;
    }

    // Retrieve courses assigned to a specific teacher
    public List<Course> getAssignedCourses(int teacherId) throws SQLException {
        List<Course> courses = new ArrayList<>();
        String query = """
                SELECT c.CourseID, c.CourseName, c.Description, c.Credits
                FROM TeacherCourses tc
                JOIN Courses c ON tc.CourseID = c.CourseID
                WHERE tc.TeacherID = ?
                """;
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, teacherId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    courses.add(new Course(
                            resultSet.getInt("CourseID"),
                            resultSet.getString("CourseName"),
                            resultSet.getString("Description"),
                            resultSet.getInt("Credits")
                    ));
                }
            }
        }
        return courses;
    }
}
