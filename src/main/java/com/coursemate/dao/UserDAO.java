package com.coursemate.dao;

import com.coursemate.config.DBConnection;
import com.coursemate.models.User;

import java.sql.*;

public class UserDAO {
    public User authenticateUser(String username, String passwordHash) throws SQLException {
        String query = "SELECT * FROM Users WHERE Username = ? AND PasswordHash = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, passwordHash);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new User(
                            resultSet.getInt("UserID"),
                            resultSet.getString("Username"),
                            resultSet.getString("PasswordHash"),
                            resultSet.getString("Role")
                    );
                }
            }
        }
        return null; // User not found or invalid credentials
    }

    public void addUser(String username, String password, String role) throws SQLException {
        String query = "INSERT INTO Users (Username, PasswordHash, Role) VALUES (?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, role);
            statement.executeUpdate();
        }
    }

    // Get the last inserted UserID
    public int getLastGeneratedUserId() throws SQLException {
        String query = "SELECT LAST_INSERT_ID()";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        }
        return -1; // If no ID was generated
    }
}
