package org.example.db;

import java.sql.*;

public class MedicalInventoryTableInitializer {
    public static void initialize(Connection conn) {
        String SQL = """
                CREATE TABLE IF NOT EXISTS MedicalInventory (
                    InventoryID SERIAL PRIMARY KEY,
                    ItemName VARCHAR(150) NOT NULL,
                    Category VARCHAR(100) NOT NULL,
                    Quantity INTEGER NOT NULL,
                    UnitPrice DECIMAL(10,2) NOT NULL,
                    Supplier VARCHAR(150) NOT NULL,
                    ExpiryDate DATE NOT NULL,
                    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
                """;
                
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(SQL);
        } catch (SQLException e) {
            throw new RuntimeException("Error initializing MedicalInventory table", e);
        }
    }
}
