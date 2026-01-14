package org.example.db;

import java.sql.*;

public class PrescriptionsTableInitializer {
    public static void initialize(Connection conn) {
        String SQL = """
                CREATE TABLE IF NOT EXISTS Prescriptions (
                    PrescriptionID SERIAL PRIMARY KEY,
                    PatientID INTEGER NOT NULL,
                    DoctorID INTEGER NOT NULL,
                    DateIssued DATE NOT NULL,
                    Notes TEXT,
                    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (PatientID) REFERENCES Patient(PatientID),
                    FOREIGN KEY (DoctorID) REFERENCES Doctors(DoctorID)
                )
                """;
                
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(SQL);
        } catch (SQLException e) {
            throw new RuntimeException("Error initializing Prescriptions table", e);
        }
    }
}
