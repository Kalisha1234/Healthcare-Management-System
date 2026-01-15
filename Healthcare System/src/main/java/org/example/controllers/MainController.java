package org.example.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.example.utils.SessionManager;

public class MainController {
    @FXML
    private BorderPane mainPane;

    @FXML
    private void showDashboard() {
        loadView("/fxml/DashboardView.fxml");
    }

    @FXML
    private void showUsers() {
        if (SessionManager.isAdmin()) {
            loadView("/fxml/UserView.fxml");
        }
    }

    @FXML
    private void handleLogout() {
        try {
            SessionManager.logout();
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginView.fxml"));
            Scene scene = new Scene(loader.load(), 900, 600);
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            
            Stage stage = (Stage) mainPane.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Healthcare Management System - Login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showPatients() {
        loadView("/fxml/PatientView.fxml");
    }

    @FXML
    private void showDoctors() {
        loadView("/fxml/DoctorView.fxml");
    }

    @FXML
    private void showAppointments() {
        loadView("/fxml/AppointmentView.fxml");
    }

    @FXML
    private void showDepartments() {
        loadView("/fxml/DepartmentView.fxml");
    }

    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Pane view = loader.load();
            mainPane.setCenter(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
