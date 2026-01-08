package org.example.db;

import java.sql.*;

public class PatientTableInitializer {
    public static void initialize(Connection conn) {
        String SQL = """
                CREATE TABLE IF NOT EXISTS Patient (
                    PatientID SERIAL PRIMARY KEY,
                    FirstName VARCHAR(100) NOT NULL,
                    LastName VARCHAR(100) NOT NULL,
                    DOB DATE NOT NULL,
                    Gender VARCHAR(10) NOT NULL,
                    Email VARCHAR(150) UNIQUE NOT NULL,
                    Phone VARCHAR(15) NOT NULL,
                    Address TEXT NOT NULL,
                    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    UpdatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
                """;
                
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(SQL);
        } catch (SQLException e) {
            throw new RuntimeException("Error initializing Patient table", e);
        }
    }
}
