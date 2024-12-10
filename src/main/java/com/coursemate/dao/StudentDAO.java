package com.coursemate.dao;

import com.coursemate.config.DBConnection;
import com.coursemate.models.Course;
import com.coursemate.models.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {
    public List<Student> getAllStudents() throws SQLException {
        List<Student> students = new ArrayList<>();
        String query = "SELECT * FROM Students";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                students.add(new Student(
                        resultSet.getInt("StudentID"),
                        resultSet.getInt("UserID"),
                        resultSet.getString("FullName")
                ));
            }
        }
        return students;
    }


    // Retrieve courses assigned to a specific student
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
            // Set StudentID in the query
            statement.setInt(1, studentId);

            // Execute the query and process the result set
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

    public int getStudentIdByUserId(int userId) throws SQLException {
        String query = "SELECT StudentID FROM Students WHERE UserID = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("StudentID");
                }
            }
        }
        throw new SQLException("No student found for UserID: " + userId);
    }




    // Retrieve all students assigned to the teacher's courses
    public List<Student> getStudentsForTeacher(int teacherId) throws SQLException {
        List<Student> students = new ArrayList<>();
        String query = """
            SELECT DISTINCT s.StudentID, s.UserID, s.FullName
            FROM StudentCourses sc
            JOIN Students s ON sc.StudentID = s.StudentID
            JOIN TeacherCourses tc ON sc.CourseID = tc.CourseID
            WHERE tc.TeacherID = ?
            """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            // Set TeacherID in the query
            statement.setInt(1, teacherId);

            // Execute the query and process the result set
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    students.add(new Student(
                            resultSet.getInt("StudentID"),
                            resultSet.getInt("UserID"),
                            resultSet.getString("FullName")
                    ));
                }
            }
        }
        return students;
    }


    public void updateStudent(Student student) throws SQLException {
        String query = "UPDATE Students SET FullName = ? WHERE StudentID = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, student.getFullName());
            statement.setInt(2, student.getStudentId());
            statement.executeUpdate();
        }
    }

    public void deleteStudent(int studentId) throws SQLException {
        String query = "DELETE FROM Students WHERE StudentID = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, studentId);
            statement.executeUpdate();
        }
    }

    // Add a new student profile
    public void addStudent(Student student, String username, String password) throws SQLException {
        try (Connection connection = DBConnection.getConnection()) {
            try {
                // Start transaction
                connection.setAutoCommit(false);

                // Step 1: Add user with role "student"
                String addUserQuery = "INSERT INTO Users (Username, PasswordHash, Role) VALUES (?, ?, ?)";
                try (PreparedStatement userStatement = connection.prepareStatement(addUserQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    userStatement.setString(1, username);
                    userStatement.setString(2, password);
                    userStatement.setString(3, "student");
                    userStatement.executeUpdate();

                    // Retrieve the generated UserID
                    try (ResultSet generatedKeys = userStatement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            int userId = generatedKeys.getInt(1);

                            // Step 2: Add the student profile
                            String addStudentQuery = "INSERT INTO Students (UserID, FullName) VALUES (?, ?)";
                            try (PreparedStatement studentStatement = connection.prepareStatement(addStudentQuery)) {
                                studentStatement.setInt(1, userId);
                                studentStatement.setString(2, student.getFullName());
                                studentStatement.executeUpdate();
                            }
                        } else {
                            throw new SQLException("Failed to retrieve UserID for the new student.");
                        }
                    }
                }

                // Commit transaction
                connection.commit();
                System.out.println("Student added successfully.");

            } catch (SQLException e) {
                // Rollback transaction on failure
                connection.rollback();
                throw new SQLException("Failed to add student: " + e.getMessage(), e);
            } finally {
                // Reset auto-commit to true
                connection.setAutoCommit(true);
            }
        }
    }


    // Assign a student to a course
    public void assignStudentToCourse(int studentId, int courseId) throws SQLException {
        String query = "INSERT INTO StudentCourses (StudentID, CourseID) VALUES (?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, studentId);
            statement.setInt(2, courseId);
            statement.executeUpdate();
            System.out.println("Student assigned to course successfully.");
        }
    }
}
