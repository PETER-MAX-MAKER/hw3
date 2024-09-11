package service;

import java.sql.*;

public class UserService {
    public static final String DB_URL = "jdbc:mysql://localhost:3306/new_schema";
    public static final String DB_USER = "root";
    public static final String DB_PASSWORD = "1234";

    private UserService() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static boolean isUsernameExists(String username) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement("SELECT COUNT(*) FROM users WHERE username = ?")) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }

    public static boolean addUser(String username, String password) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)")) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            return pstmt.executeUpdate() > 0;
        }
    }

    public static boolean updateUser(String username, String password) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement("UPDATE users SET password = ? WHERE username = ?")) {
            pstmt.setString(1, password);
            pstmt.setString(2, username);
            return pstmt.executeUpdate() > 0;
        }
    }

    public static boolean deleteUser(String username) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM users WHERE username = ?")) {
            pstmt.setString(1, username);
            return pstmt.executeUpdate() > 0;
        }
    }

    public static ResultSet getAllUsers() throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        Statement stmt = conn.createStatement();
        return stmt.executeQuery("SELECT username, password FROM users");
    }

    public static ResultSet getTreasures(String username) throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        PreparedStatement pstmt = conn.prepareStatement("SELECT reward FROM draw_results WHERE username = ?");
        pstmt.setString(1, username);
        return pstmt.executeQuery();
    }

    public static boolean clearTreasures(String username) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM draw_results WHERE username = ?")) {
            pstmt.setString(1, username);
            return pstmt.executeUpdate() > 0;
        }
    }

    // Validate user credentials
    public static boolean validateUser(String username, String password) throws SQLException {
        String query = "SELECT COUNT(*) FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    
    
    
    
    // Validate admin credentials
    public static boolean validateAdmin(String username, String password) throws SQLException {
        // This method could check admin credentials similarly to validateUser
        // Assuming admin credentials are stored differently or have specific criteria
        return "admin123".equals(username) && "admin123".equals(password);
    }
}
