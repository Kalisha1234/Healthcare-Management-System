package org.example.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.HealthcareApp;
import org.example.models.User;
import org.example.services.UserService;
import org.example.utils.SessionManager;
import org.example.utils.ValidationException;

public class LoginController {
    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblError;

    private UserService userService;

    @FXML
    public void initialize() {
        userService = new UserService(HealthcareApp.getConnection());
    }

    @FXML
    private void handleLogin() {
        lblError.setText("");
        
        try {
            String username = txtUsername.getText();
            String password = txtPassword.getText();
            
            User user = userService.authenticate(username, password);
            SessionManager.setCurrentUser(user);
            
            // Load main application
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainView.fxml"));
            Scene scene = new Scene(loader.load(), 1200, 750);
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            
            Stage stage = (Stage) txtUsername.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Healthcare Management System - " + user.getFirstName() + " " + user.getLastName() + " (" + user.getRole() + ")");
            
        } catch (ValidationException e) {
            lblError.setText(e.getMessage());
        } catch (Exception e) {
            lblError.setText("Login failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleExit() {
        System.exit(0);
    }
}
