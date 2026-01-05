package org.example;

import java.sql.Connection;

import org.example.config.DBConfig;
import org.example.db.DatabaseInitializer;

public class Main {
    public static void main(String[] args) {
        DBConfig dbConfig = new DBConfig();

        try(Connection connection = dbConfig.connect()) {
            System.out.println("Database connection established successfully.");
            DatabaseInitializer.initializeAll(connection);
            System.out.println("All tables initialized successfully.");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
