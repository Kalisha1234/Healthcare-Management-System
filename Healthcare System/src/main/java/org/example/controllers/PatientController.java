package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.HealthcareApp;
import org.example.models.Patient;
import org.example.services.PatientService;
import org.example.utils.SearchUtil;
import org.example.utils.ValidationException;

import java.util.List;


public class PatientController {
    @FXML private TableView<Patient> patientTable;
    @FXML private TableColumn<Patient, Integer> colId;
    @FXML private TableColumn<Patient, String> colFirstName;
    @FXML private TableColumn<Patient, String> colLastName;
    @FXML private TableColumn<Patient, String> colEmail;
    @FXML private TableColumn<Patient, String> colPhone;

    @FXML private TextField txtFirstName;
    @FXML private TextField txtLastName;
    @FXML private DatePicker dpDOB;
    @FXML private ComboBox<String> cbGender;
    @FXML private TextField txtEmail;
    @FXML private TextField txtPhone;
    @FXML private TextArea txtAddress;
    @FXML private TextField txtSearch;
    @FXML private Label lblCacheStatus;

    private PatientService patientService;
    private List<Patient> allPatients;

    @FXML
    public void initialize() {
        patientService = PatientService.getInstance(HealthcareApp.getConnection());

        // Setup table columns
        colId.setCellValueFactory(new PropertyValueFactory<>("patientID"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));

        // Setup gender combo box
        cbGender.setItems(FXCollections.observableArrayList("Male", "Female", "Other"));

        loadPatients();
        updateCacheStatus();
    }

    @FXML
    private void handleSearch() {
        try {
            String query = txtSearch.getText();
            
            // Ensure data is loaded
            if (allPatients == null) {
                allPatients = patientService.getAllPatients();
            }
            
            if (query == null || query.trim().isEmpty()) {
                patientTable.setItems(FXCollections.observableArrayList(allPatients));
                return;
            }
            
            List<Patient> filtered = SearchUtil.searchPatients(allPatients, query);
            patientTable.setItems(FXCollections.observableArrayList(filtered));
        } catch (Exception e) {
            showAlert("Error", "Search failed: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleClearSearch() {
        txtSearch.clear();
        loadPatients();
    }

    @FXML
    private void handleAdd() {
        try {
            Patient patient = new Patient(
                txtFirstName.getText(),
                txtLastName.getText(),
                dpDOB.getValue(),
                cbGender.getValue(),
                txtEmail.getText(),
                txtPhone.getText(),
                txtAddress.getText()
            );

            patientService.registerPatient(patient);
            showAlert("Success", "Patient registered successfully!", Alert.AlertType.INFORMATION);
            clearForm();
            loadPatients();
        } catch (ValidationException e) {
            showAlert("Validation Error", e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Error", "Failed to register patient: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleUpdate() {
        Patient selected = patientTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Warning", "Please select a patient to update", Alert.AlertType.WARNING);
            return;
        }

        try {
            selected.setFirstName(txtFirstName.getText());
            selected.setLastName(txtLastName.getText());
            selected.setDob(dpDOB.getValue());
            selected.setGender(cbGender.getValue());
            selected.setEmail(txtEmail.getText());
            selected.setPhone(txtPhone.getText());
            selected.setAddress(txtAddress.getText());

            patientService.updatePatient(selected);
            showAlert("Success", "Patient updated successfully!", Alert.AlertType.INFORMATION);
            clearForm();
            loadPatients();
        } catch (ValidationException e) {
            showAlert("Validation Error", e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Error", "Failed to update patient: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleDelete() {
        Patient selected = patientTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Warning", "Please select a patient to delete", Alert.AlertType.WARNING);
            return;
        }

        try {
            patientService.deletePatient(selected.getPatientID());
            showAlert("Success", "Patient deleted successfully!", Alert.AlertType.INFORMATION);
            clearForm();
            loadPatients();
        } catch (Exception e) {
            showAlert("Error", "Failed to delete patient: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleTableClick() {
        Patient selected = patientTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            txtFirstName.setText(selected.getFirstName());
            txtLastName.setText(selected.getLastName());
            dpDOB.setValue(selected.getDob());
            cbGender.setValue(selected.getGender());
            txtEmail.setText(selected.getEmail());
            txtPhone.setText(selected.getPhone());
            txtAddress.setText(selected.getAddress());
        }
    }

    private void loadPatients() {
        try {
            allPatients = patientService.getAllPatients();
            patientTable.setItems(FXCollections.observableArrayList(allPatients));
            updateCacheStatus();
        } catch (Exception e) {
            showAlert("Error", "Failed to load patients: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void updateCacheStatus() {
        try {
            lblCacheStatus.setText("Cache: " + patientService.getCacheStatus());
        } catch (Exception e) {
            lblCacheStatus.setText("Cache: Error");
            }
    }

    private void clearForm() {
        txtFirstName.clear();
        txtLastName.clear();
        dpDOB.setValue(null);
        cbGender.setValue(null);
        txtEmail.clear();
        txtPhone.clear();
        txtAddress.clear();
        patientTable.getSelectionModel().clearSelection();
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
