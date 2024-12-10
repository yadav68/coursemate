package com.coursemate.dao;

import com.coursemate.config.DBConnection;
import com.coursemate.models.Course;
import com.coursemate.models.Teacher;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeacherDAO {

    // Retrieve courses assigned to a specific teacher
//    public List<Course> getAssignedCourses(int teacherId) throws SQLException {
//        List<Course> courses = new ArrayList<>();
//        String query = """
//                SELECT c.CourseID, c.CourseName, c.Description, c.Credits
//                FROM TeacherCourses tc
//                JOIN Courses c ON tc.CourseID = c.CourseID
//                WHERE tc.TeacherID = ?
//                """;
//        try (Connection connection = DBConnection.getConnection();
//             PreparedStatement statement = connection.prepareStatement(query)) {
//            // Set the teacher ID in the query
//            statement.setInt(1, teacherId);
//
//            // Execute the query and process the result set
//            try (ResultSet resultSet = statement.executeQuery()) {
//                while (resultSet.next()) {
//                    // Create a Course object for each row in the result set
//                    courses.add(new Course(
//                            resultSet.getInt("CourseID"),
//                            resultSet.getString("CourseName"),
//                            resultSet.getString("Description"),
//                            resultSet.getInt("Credits")
//                    ));
//                }
//            }
//        }
//        return courses;
//    }

    public int getTeacherIdByUserId(int userId) throws SQLException {
        String query = "SELECT TeacherID FROM Teachers WHERE UserID = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("TeacherID");
                }
            }
        }
        throw new SQLException("No teacher found for UserID: " + userId);
    }

    // Retrieve courses assigned to a specific teacher using TeacherID
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
            // Set TeacherID in the query
            statement.setInt(1, teacherId);

            // Execute the query and process the result set
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    // Create a Course object for each row in the result set
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



    // Retrieve teacher details along with assigned course names
public Map<Teacher, List<String>> getTeachersWithCourses() throws SQLException {
    Map<Teacher, List<String>> teacherCourseMap = new HashMap<>();
    String query = """
            SELECT t.TeacherID, t.UserID, t.FullName, t.Specialization, c.CourseName
            FROM Teachers t
            LEFT JOIN TeacherCourses tc ON t.TeacherID = tc.TeacherID
            LEFT JOIN Courses c ON tc.CourseID = c.CourseID
        """;

    try (Connection connection = DBConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(query);
         ResultSet resultSet = statement.executeQuery()) {

        while (resultSet.next()) {
            // Retrieve teacher details
            int teacherId = resultSet.getInt("TeacherID");
            Teacher teacher = new Teacher(
                    teacherId,
                    resultSet.getInt("UserID"),
                    resultSet.getString("FullName"),
                    resultSet.getString("Specialization")
            );

            // Retrieve the course name
            String courseName = resultSet.getString("CourseName");

            // Add teacher and courses to the map
            teacherCourseMap.putIfAbsent(teacher, new ArrayList<>());
            if (courseName != null) {
                teacherCourseMap.get(teacher).add(courseName);
            }
        }
    }
    return teacherCourseMap;
}
    public void updateTeacher(Teacher teacher) throws SQLException {
        String query = "UPDATE Teachers SET FullName = ?, Specialization = ? WHERE TeacherID = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, teacher.getFullName());
            statement.setString(2, teacher.getSpecialization());
            statement.setInt(3, teacher.getTeacherId());
            statement.executeUpdate();
        }
    }

    public void deleteTeacher(int teacherId) throws SQLException {
        String query = "DELETE FROM Teachers WHERE TeacherID = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, teacherId);
            statement.executeUpdate();
        }
    }

    public void addTeacher(Teacher teacher, String username, String password) throws SQLException {
        // Use a single connection for both operations
        try (Connection connection = DBConnection.getConnection()) {
            try {
                // Start transaction
                connection.setAutoCommit(false);

                // Step 1: Add user with role "teacher"
                String addUserQuery = "INSERT INTO Users (Username, PasswordHash, Role) VALUES (?, ?, ?)";
                try (PreparedStatement userStatement = connection.prepareStatement(addUserQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    userStatement.setString(1, username);
                    userStatement.setString(2, password);
                    userStatement.setString(3, "teacher");
                    userStatement.executeUpdate();

                    // Retrieve the generated UserID
                    try (ResultSet generatedKeys = userStatement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            int userId = generatedKeys.getInt(1);

                            // Step 2: Add the teacher profile
                            String addTeacherQuery = "INSERT INTO Teachers (UserID, FullName, Specialization) VALUES (?, ?, ?)";
                            try (PreparedStatement teacherStatement = connection.prepareStatement(addTeacherQuery)) {
                                teacherStatement.setInt(1, userId);
                                teacherStatement.setString(2, teacher.getFullName());
                                teacherStatement.setString(3, teacher.getSpecialization());
                                teacherStatement.executeUpdate();
                            }
                        } else {
                            throw new SQLException("Failed to retrieve UserID for the new teacher.");
                        }
                    }
                }

                // Commit transaction
                connection.commit();
                System.out.println("Teacher added successfully.");

            } catch (SQLException e) {
                // Rollback transaction on failure
                connection.rollback();
                throw new SQLException("Failed to add teacher: " + e.getMessage(), e);
            } finally {
                // Reset auto-commit to true
                connection.setAutoCommit(true);
            }
        }
    }

    // Assign a course to a teacher
//    public void assignCourseToTeacher(int teacherId, int courseId) throws SQLException {
//        String query = "INSERT INTO TeacherCourses (TeacherID, CourseID) VALUES (?, ?)";
//        try (Connection connection = DBConnection.getConnection();
//             PreparedStatement statement = connection.prepareStatement(query)) {
//            statement.setInt(1, teacherId);
//            statement.setInt(2, courseId);
//            statement.executeUpdate();
//        }
//    }
    // Assign a course to a teacher using TeacherID
    public void assignCourseToTeacher(int teacherId, int courseId) throws SQLException {
        String query = "INSERT INTO TeacherCourses (TeacherID, CourseID) VALUES (?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            // Set TeacherID and CourseID in the query
            statement.setInt(1, teacherId);
            statement.setInt(2, courseId);

            // Execute the query
            statement.executeUpdate();
            System.out.println("Course assigned to teacher successfully.");
        }
    }


    // Fetch the list of teachers and the courses they teach
    public List<String> getTeachersAndCourses() throws SQLException {
        List<String> teacherCourseList = new ArrayList<>();
        String query = """
            SELECT t.FullName AS TeacherName, c.CourseName AS CourseName
            FROM TeacherCourses tc
            JOIN Teachers t ON tc.TeacherID = t.TeacherID
            JOIN Courses c ON tc.CourseID = c.CourseID
            """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String teacherName = resultSet.getString("TeacherName");
                String courseName = resultSet.getString("CourseName");
                teacherCourseList.add("Teacher: " + teacherName + " - Course: " + courseName);
            }
        }

        return teacherCourseList;
    }

}
