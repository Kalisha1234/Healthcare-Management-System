package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.HealthcareApp;
import org.example.models.Doctor;
import org.example.models.DepartmentItem;
import org.example.services.DoctorService;
import org.example.services.DepartmentService;
import org.example.utils.SearchUtil;
import org.example.utils.ValidationException;

import java.util.List;

public class DoctorController {
    @FXML private TableView<Doctor> doctorTable;
    @FXML private TableColumn<Doctor, Integer> colId;
    @FXML private TableColumn<Doctor, String> colFirstName;
    @FXML private TableColumn<Doctor, String> colLastName;
    @FXML private TableColumn<Doctor, String> colEmail;
    @FXML private TableColumn<Doctor, String> colPhone;

    @FXML private TextField txtFirstName;
    @FXML private TextField txtLastName;
    @FXML private ComboBox<DepartmentItem> cbDepartment;
    @FXML private TextField txtPhone;
    @FXML private TextField txtEmail;
    @FXML private DatePicker dpHireDate;
    @FXML private TextField txtSearch;

    private DoctorService doctorService;
    private DepartmentService departmentService;
    private List<Doctor> allDoctors;

    @FXML
    public void initialize() {
        doctorService = DoctorService.getInstance(HealthcareApp.getConnection());
        departmentService = DepartmentService.getInstance(HealthcareApp.getConnection());

        colId.setCellValueFactory(new PropertyValueFactory<>("doctorID"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));

        loadDepartments();
        loadDoctors();
    }

    @FXML
    private void handleSearch() {
        try {
            String query = txtSearch.getText();
            
            // Ensure data is loaded
            if (allDoctors == null) {
                allDoctors = doctorService.getAllDoctors();
            }
            
            if (query == null || query.trim().isEmpty()) {
                doctorTable.setItems(FXCollections.observableArrayList(allDoctors));
                return;
            }
            
            List<Doctor> filtered = SearchUtil.searchDoctors(allDoctors, query);
            doctorTable.setItems(FXCollections.observableArrayList(filtered));
        } catch (Exception e) {
            showAlert("Error", "Search failed: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleClearSearch() {
        txtSearch.clear();
        loadDoctors();
    }

    @FXML
    private void handleAdd() {
        try {
            Doctor doctor = new Doctor(
                txtFirstName.getText(),
                txtLastName.getText(),
                cbDepartment.getValue().getId(),
                txtPhone.getText(),
                txtEmail.getText(),
                dpHireDate.getValue()
            );

            doctorService.addDoctor(doctor);
            showAlert("Success", "Doctor added successfully!", Alert.AlertType.INFORMATION);
            clearForm();
            loadDoctors();
        } catch (ValidationException e) {
            showAlert("Validation Error", e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Error", "Failed to add doctor: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleUpdate() {
        Doctor selected = doctorTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Warning", "Please select a doctor to update", Alert.AlertType.WARNING);
            return;
        }

        try {
            selected.setFirstName(txtFirstName.getText());
            selected.setLastName(txtLastName.getText());
            selected.setDepartmentID(cbDepartment.getValue().getId());
            selected.setPhone(txtPhone.getText());
            selected.setEmail(txtEmail.getText());
            selected.setHireDate(dpHireDate.getValue());

            doctorService.updateDoctor(selected);
            showAlert("Success", "Doctor updated successfully!", Alert.AlertType.INFORMATION);
            clearForm();
            loadDoctors();
        } catch (ValidationException e) {
            showAlert("Validation Error", e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Error", "Failed to update doctor: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleDelete() {
        Doctor selected = doctorTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Warning", "Please select a doctor to delete", Alert.AlertType.WARNING);
            return;
        }

        try {
            doctorService.deleteDoctor(selected.getDoctorID());
            showAlert("Success", "Doctor deleted successfully!", Alert.AlertType.INFORMATION);
            clearForm();
            loadDoctors();
        } catch (Exception e) {
            showAlert("Error", "Failed to delete doctor: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleTableClick() {
        Doctor selected = doctorTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            txtFirstName.setText(selected.getFirstName());
            txtLastName.setText(selected.getLastName());
            
            for (DepartmentItem item : cbDepartment.getItems()) {
                if (item.getId().equals(selected.getDepartmentID())) {
                    cbDepartment.setValue(item);
                    break;
                }
            }
            
            txtPhone.setText(selected.getPhone());
            txtEmail.setText(selected.getEmail());
            dpHireDate.setValue(selected.getHireDate());
        }
    }

    private void loadDoctors() {
        try {
            allDoctors = doctorService.getAllDoctors();
            doctorTable.setItems(FXCollections.observableArrayList(allDoctors));
        } catch (Exception e) {
            showAlert("Error", "Failed to load doctors: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void loadDepartments() {
        try {
            cbDepartment.setItems(FXCollections.observableArrayList(
                departmentService.getAllDepartments().stream()
                    .map(d -> new DepartmentItem(d.getDepartmentID(), d.getName()))
                    .toList()
            ));
        } catch (Exception e) {
            showAlert("Error", "Failed to load departments: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void clearForm() {
        txtFirstName.clear();
        txtLastName.clear();
        cbDepartment.setValue(null);
        txtPhone.clear();
        txtEmail.clear();
        dpHireDate.setValue(null);
        doctorTable.getSelectionModel().clearSelection();
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
