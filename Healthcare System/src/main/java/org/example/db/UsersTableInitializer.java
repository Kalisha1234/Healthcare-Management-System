package org.example.db;

import java.sql.*;

public class UsersTableInitializer {
    public static void initialize(Connection conn) {
        String SQL = """
                CREATE TABLE IF NOT EXISTS Users (
                    UserID SERIAL PRIMARY KEY,
                    Username VARCHAR(50) UNIQUE NOT NULL,
                    Password VARCHAR(255) NOT NULL,
                    Role VARCHAR(20) NOT NULL CHECK (Role IN ('ADMIN', 'RECEPTIONIST')),
                    FirstName VARCHAR(100) NOT NULL,
                    LastName VARCHAR(100) NOT NULL,
                    Email VARCHAR(150) UNIQUE NOT NULL,
                    IsActive BOOLEAN DEFAULT TRUE,
                    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    UpdatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
                """;
                
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(SQL);
            
            // Create default admin user if not exists
            String checkAdmin = "SELECT COUNT(*) FROM Users WHERE Username = 'admin'";
            ResultSet rs = stmt.executeQuery(checkAdmin);
            rs.next();
            if (rs.getInt(1) == 0) {
                String insertAdmin = """
                    INSERT INTO Users (Username, Password, Role, FirstName, LastName, Email)
                    VALUES ('admin', 'admin123', 'ADMIN', 'System', 'Administrator', 'admin@hospital.com')
                    """;
                stmt.execute(insertAdmin);
                System.out.println("Default admin user created (username: admin, password: admin123)");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error initializing Users table", e);
        }
    }
}
