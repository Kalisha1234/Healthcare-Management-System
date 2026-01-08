package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.config.DBConfig;
import org.example.config.MongoDBConfig;
import org.example.db.DatabaseInitializer;
import org.example.utils.DataSeeder;

import java.sql.Connection;

public class HealthcareApp extends Application {
    private static Connection connection;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Initialize database
        DBConfig dbConfig = new DBConfig();

        connection = dbConfig.connect();
        DatabaseInitializer.initializeAll(connection);
        
        // Seed sample data
//        DataSeeder.seedData(connection);

        // Load login view
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginView.fxml"));
        Scene scene = new Scene(loader.load(), 900, 600);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        
        primaryStage.setTitle("Healthcare Management System - Login");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
        MongoDBConfig.close();
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
