package org.example.db;

import java.sql.Connection;

public class DatabaseInitializer {
    public static void initializeAll(Connection conn) {
        // Initialize tables in order respecting foreign key dependencies
        DepartmentsTableInitializer.initialize(conn);
        PatientTableInitializer.initialize(conn);
        DoctorTableInitializer.initialize(conn);
        AppointmentsTableInitializer.initialize(conn);
        PrescriptionsTableInitializer.initialize(conn);
        PrescriptionItemsTableInitializer.initialize(conn);
        PatientFeedbackTableInitializer.initialize(conn);
        MedicalInventoryTableInitializer.initialize(conn);
    }
}
