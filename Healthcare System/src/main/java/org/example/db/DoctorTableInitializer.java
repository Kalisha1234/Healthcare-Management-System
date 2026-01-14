package org.example.db;

import java.sql.*;

public class DoctorTableInitializer {
    public static void initialize(Connection conn) {
        String SQL = """
                CREATE TABLE IF NOT EXISTS Doctors (
                    DoctorID SERIAL PRIMARY KEY,
                    FirstName VARCHAR(100) NOT NULL,
                    LastName VARCHAR(100) NOT NULL,
                    DepartmentID INTEGER NOT NULL,
                    Phone VARCHAR(15) NOT NULL,
                    Email VARCHAR(150) NOT NULL,
                    HireDate DATE NOT NULL,
                    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    UpdatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (DepartmentID) REFERENCES Departments(DepartmentID)
                )
                """;
                
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(SQL);
        } catch (SQLException e) {
            throw new RuntimeException("Error initializing Doctors table", e);
        }
    }
}
