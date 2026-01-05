package org.example.db;

import java.sql.*;

public class PrescriptionItemsTableInitializer {
    public static void initialize(Connection conn) {
        String SQL = """
                CREATE TABLE IF NOT EXISTS PrescriptionItems (
                    ItemID SERIAL PRIMARY KEY,
                    PrescriptionID INTEGER NOT NULL,
                    MedicineName VARCHAR(150) NOT NULL,
                    Frequency VARCHAR(50) NOT NULL,
                    Duration VARCHAR(50) NOT NULL,
                    Instructions TEXT,
                    FOREIGN KEY (PrescriptionID) REFERENCES Prescriptions(PrescriptionID)
                )
                """;
                
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(SQL);
        } catch (SQLException e) {
            throw new RuntimeException("Error initializing PrescriptionItems table", e);
        }
    }
}
