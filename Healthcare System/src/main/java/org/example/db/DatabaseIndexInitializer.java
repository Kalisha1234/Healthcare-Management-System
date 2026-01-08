package org.example.db;

import java.sql.*;

public class DatabaseIndexInitializer {
    public static void initialize(Connection conn) {
        try (Statement stmt = conn.createStatement()) {
            // Patient indexes
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_patient_email ON Patient(Email)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_patient_name ON Patient(FirstName, LastName)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_patient_phone ON Patient(Phone)");
            
            // Doctor indexes
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_doctor_name ON Dcotors(FirstName, LastName)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_doctor_email ON Dcotors(Email)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_doctor_dept ON Dcotors(DepartmentID)");
            
            // Appointment indexes
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_appointment_patient ON Appointments(PatientID)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_appointment_doctor ON Appointments(DoctorID)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_appointment_date ON Appointments(AppointmentDate)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_appointment_status ON Appointments(Status)");
            
            // Department indexes
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_department_name ON Departments(Name)");
            
            // Prescription indexes
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_prescription_patient ON Prescriptions(PatientID)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_prescription_doctor ON Prescriptions(DoctorID)");
            
            System.out.println("✓ Database indexes created successfully");
        } catch (SQLException e) {
            System.err.println("Error creating indexes: " + e.getMessage());
        }
    }
}
