package org.example.db;

import java.sql.*;

public class AppointmentsTableInitializer {
    public static void initialize(Connection conn) {
        String SQL = """
                CREATE TABLE IF NOT EXISTS Appointments (
                    AppointmentID SERIAL PRIMARY KEY,
                    PatientID INTEGER NOT NULL,
                    DoctorID INTEGER NOT NULL,
                    AppointmentDate DATE NOT NULL,
                    AppointmentTime TIME NOT NULL,
                    Status VARCHAR(20) NOT NULL CHECK (Status IN ('Scheduled', 'Completed', 'Cancelled')),
                    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    UpdatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (PatientID) REFERENCES Patient(PatientID),
                    FOREIGN KEY (DoctorID) REFERENCES Dcotors(DoctorID)
                )
                """;
                
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(SQL);
        } catch (SQLException e) {
            throw new RuntimeException("Error initializing Appointments table", e);
        }
    }
}
