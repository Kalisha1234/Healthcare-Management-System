package org.example.db;

import java.sql.*;

public class DepartmentsTableInitializer {
    public static void initialize(Connection conn) {
        String SQL = """
                CREATE TABLE IF NOT EXISTS Departments (
                    DepartmentID SERIAL PRIMARY KEY,
                    Name VARCHAR(100) NOT NULL,
                    Description TEXT,
                    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    UpdatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
                """;
                
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(SQL);
        } catch (SQLException e) {
            throw new RuntimeException("Error initializing Departments table", e);
        }
    }
}
