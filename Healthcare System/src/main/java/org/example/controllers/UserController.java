package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.HealthcareApp;
import org.example.models.User;
import org.example.services.UserService;
import org.example.utils.SessionManager;
import org.example.utils.ValidationException;

public class UserController {
    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, Integer> colId;
    @FXML private TableColumn<User, String> colUsername;
    @FXML private TableColumn<User, String> colFirstName;
    @FXML private TableColumn<User, String> colLastName;
    @FXML private TableColumn<User, String> colRole;
    @FXML private TableColumn<User, String> colEmail;

    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private TextField txtFirstName;
    @FXML private TextField txtLastName;
    @FXML private ComboBox<String> cbRole;
    @FXML private TextField txtEmail;
    @FXML private CheckBox chkActive;

    private UserService userService;

    @FXML
    public void initialize() {
        // Check if user is admin
        if (!SessionManager.isAdmin()) {
            showAlert("Access Denied", "Only administrators can manage users", Alert.AlertType.ERROR);
            return;
        }

        userService = new UserService(HealthcareApp.getConnection());

        colId.setCellValueFactory(new PropertyValueFactory<>("userID"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        cbRole.setItems(FXCollections.observableArrayList("ADMIN", "RECEPTIONIST"));
        chkActive.setSelected(true);

        loadUsers();
    }

    @FXML
    private void handleAdd() {
        try {
            User user = new User(
                txtUsername.getText(),
                txtPassword.getText(),
                cbRole.getValue(),
                txtFirstName.getText(),
                txtLastName.getText(),
                txtEmail.getText()
            );
            user.setIsActive(chkActive.isSelected());

            userService.createUser(user);
            showAlert("Success", "User created successfully!", Alert.AlertType.INFORMATION);
            clearForm();
            loadUsers();
        } catch (ValidationException e) {
            showAlert("Validation Error", e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Error", "Failed to create user: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleUpdate() {
        User selected = userTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Warning", "Please select a user to update", Alert.AlertType.WARNING);
            return;
        }

        try {
            selected.setUsername(txtUsername.getText());
            if (!txtPassword.getText().isEmpty()) {
                selected.setPassword(txtPassword.getText());
            }
            selected.setFirstName(txtFirstName.getText());
            selected.setLastName(txtLastName.getText());
            selected.setRole(cbRole.getValue());
            selected.setEmail(txtEmail.getText());
            selected.setIsActive(chkActive.isSelected());

            userService.updateUser(selected);
            showAlert("Success", "User updated successfully!", Alert.AlertType.INFORMATION);
            clearForm();
            loadUsers();
        } catch (ValidationException e) {
            showAlert("Validation Error", e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Error", "Failed to update user: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleDelete() {
        User selected = userTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Warning", "Please select a user to delete", Alert.AlertType.WARNING);
            return;
        }

        // Prevent deleting current user
        if (selected.getUserID().equals(SessionManager.getCurrentUser().getUserID())) {
            showAlert("Error", "You cannot delete your own account", Alert.AlertType.ERROR);
            return;
        }

        try {
            userService.deleteUser(selected.getUserID());
            showAlert("Success", "User deleted successfully!", Alert.AlertType.INFORMATION);
            clearForm();
            loadUsers();
        } catch (Exception e) {
            showAlert("Error", "Failed to delete user: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleTableClick() {
        User selected = userTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            txtUsername.setText(selected.getUsername());
            txtPassword.clear(); // Don't show password
            txtFirstName.setText(selected.getFirstName());
            txtLastName.setText(selected.getLastName());
            cbRole.setValue(selected.getRole());
            txtEmail.setText(selected.getEmail());
            chkActive.setSelected(selected.getIsActive());
        }
    }

    private void loadUsers() {
        try {
            userTable.setItems(FXCollections.observableArrayList(userService.getAllUsers()));
        } catch (Exception e) {
            showAlert("Error", "Failed to load users: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void clearForm() {
        txtUsername.clear();
        txtPassword.clear();
        txtFirstName.clear();
        txtLastName.clear();
        cbRole.setValue(null);
        txtEmail.clear();
        chkActive.setSelected(true);
        userTable.getSelectionModel().clearSelection();
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
