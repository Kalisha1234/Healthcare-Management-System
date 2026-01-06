package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.HealthcareApp;
import org.example.models.Department;
import org.example.services.DepartmentService;
import org.example.utils.ValidationException;

public class DepartmentController {
    @FXML private TableView<Department> departmentTable;
    @FXML private TableColumn<Department, Integer> colId;
    @FXML private TableColumn<Department, String> colName;
    @FXML private TableColumn<Department, String> colDescription;

    @FXML private TextField txtName;
    @FXML private TextArea txtDescription;

    private DepartmentService departmentService;

    @FXML
    public void initialize() {
        departmentService = new DepartmentService(HealthcareApp.getConnection());

        colId.setCellValueFactory(new PropertyValueFactory<>("departmentID"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

        loadDepartments();
    }

    @FXML
    private void handleAdd() {
        try {
            Department department = new Department(txtName.getText(), txtDescription.getText());
            departmentService.addDepartment(department);
            showAlert("Success", "Department added successfully!", Alert.AlertType.INFORMATION);
            clearForm();
            loadDepartments();
        } catch (ValidationException e) {
            showAlert("Validation Error", e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Error", "Failed to add department: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleUpdate() {
        Department selected = departmentTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Warning", "Please select a department to update", Alert.AlertType.WARNING);
            return;
        }

        try {
            selected.setName(txtName.getText());
            selected.setDescription(txtDescription.getText());
            departmentService.updateDepartment(selected);
            showAlert("Success", "Department updated successfully!", Alert.AlertType.INFORMATION);
            clearForm();
            loadDepartments();
        } catch (ValidationException e) {
            showAlert("Validation Error", e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Error", "Failed to update department: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleDelete() {
        Department selected = departmentTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Warning", "Please select a department to delete", Alert.AlertType.WARNING);
            return;
        }

        try {
            departmentService.deleteDepartment(selected.getDepartmentID());
            showAlert("Success", "Department deleted successfully!", Alert.AlertType.INFORMATION);
            clearForm();
            loadDepartments();
        } catch (Exception e) {
            showAlert("Error", "Failed to delete department: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleTableClick() {
        Department selected = departmentTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            txtName.setText(selected.getName());
            txtDescription.setText(selected.getDescription());
        }
    }

    private void loadDepartments() {
        try {
            departmentTable.setItems(FXCollections.observableArrayList(departmentService.getAllDepartments()));
        } catch (Exception e) {
            showAlert("Error", "Failed to load departments: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void clearForm() {
        txtName.clear();
        txtDescription.clear();
        departmentTable.getSelectionModel().clearSelection();
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
