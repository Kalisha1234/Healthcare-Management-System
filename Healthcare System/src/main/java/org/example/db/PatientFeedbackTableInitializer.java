package org.example.db;

import java.sql.*;

public class PatientFeedbackTableInitializer {
    public static void initialize(Connection conn) {
        String SQL = """
                CREATE TABLE IF NOT EXISTS PatientFeedback (
                    FeedbackID SERIAL PRIMARY KEY,
                    PatientID INTEGER NOT NULL,
                    DoctorID INTEGER NOT NULL,
                    DepartmentID INTEGER NOT NULL,
                    Rating INTEGER NOT NULL,
                    Comments TEXT,
                    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (PatientID) REFERENCES Patient(PatientID),
                    FOREIGN KEY (DoctorID) REFERENCES Dcotors(DoctorID),
                    FOREIGN KEY (DepartmentID) REFERENCES Departments(DepartmentID)
                )
                """;
                
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(SQL);
        } catch (SQLException e) {
            throw new RuntimeException("Error initializing PatientFeedback table", e);
        }
    }
}
