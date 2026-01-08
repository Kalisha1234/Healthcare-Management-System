package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import org.example.HealthcareApp;
import org.example.models.Patient;
import org.example.models.PatientNote;
import org.example.models.MedicalLog;
import org.example.services.PatientService;
import org.example.services.PatientNoteService;
import org.example.services.MedicalLogService;
import org.example.utils.SearchUtil;
import org.example.utils.ValidationException;
import org.example.utils.SessionManager;

import java.util.List;
import java.util.Optional;


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
    private PatientNoteService noteService;
    private MedicalLogService logService;
    private List<Patient> allPatients;

    @FXML
    public void initialize() {
        patientService = PatientService.getInstance(HealthcareApp.getConnection());
        noteService = PatientNoteService.getInstance();
        logService = MedicalLogService.getInstance();

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
            
            // Log to MongoDB
            String username = SessionManager.getCurrentUser() != null ? 
                SessionManager.getCurrentUser().getUsername() : "System";
            MedicalLog log = new MedicalLog(
                patient.getPatientID(),
                "Patient Registered",
                "New patient: " + patient.getFirstName() + " " + patient.getLastName(),
                username
            );
            logService.addLog(log);
            
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
            
            // Log to MongoDB
            String username = SessionManager.getCurrentUser() != null ? 
                SessionManager.getCurrentUser().getUsername() : "System";
            MedicalLog log = new MedicalLog(
                selected.getPatientID(),
                "Patient Updated",
                "Updated patient information",
                username
            );
            logService.addLog(log);
            
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
            
            // Log to MongoDB
            String username = SessionManager.getCurrentUser() != null ? 
                SessionManager.getCurrentUser().getUsername() : "System";
            MedicalLog log = new MedicalLog(
                selected.getPatientID(),
                "Patient Deleted",
                "Deleted patient: " + selected.getFirstName() + " " + selected.getLastName(),
                username
            );
            logService.addLog(log);
            
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

    @FXML
    private void handleAddNote() {
        Patient selected = patientTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Warning", "Please select a patient to add a note", Alert.AlertType.WARNING);
            return;
        }

        Dialog<PatientNote> dialog = new Dialog<>();
        dialog.setTitle("Add Patient Note");
        dialog.setHeaderText("Add note for: " + selected.getFirstName() + " " + selected.getLastName());

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        ComboBox<String> noteType = new ComboBox<>();
        noteType.getItems().addAll("Consultation", "Diagnosis", "Treatment", "Follow-up", "General");
        noteType.setValue("General");
        
        TextArea noteText = new TextArea();
        noteText.setPromptText("Enter note details...");
        noteText.setPrefRowCount(5);

        grid.add(new Label("Type:"), 0, 0);
        grid.add(noteType, 1, 0);
        grid.add(new Label("Note:"), 0, 1);
        grid.add(noteText, 1, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                Integer doctorID = SessionManager.getCurrentUser() != null ? 
                    SessionManager.getCurrentUser().getUserID() : 0;
                return new PatientNote(selected.getPatientID(), doctorID, 
                    noteText.getText(), noteType.getValue());
            }
            return null;
        });

        Optional<PatientNote> result = dialog.showAndWait();
        result.ifPresent(note -> {
            noteService.addNote(note);
            showAlert("Success", "Note added successfully!", Alert.AlertType.INFORMATION);
        });
    }

    @FXML
    private void handleViewNotes() {
        Patient selected = patientTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Warning", "Please select a patient to view notes", Alert.AlertType.WARNING);
            return;
        }

        List<PatientNote> notes = noteService.getNotesByPatient(selected.getPatientID());
        
        StringBuilder notesText = new StringBuilder();
        if (notes.isEmpty()) {
            notesText.append("No notes found for this patient.");
        } else {
            for (PatientNote note : notes) {
                notesText.append("Type: ").append(note.getType()).append("\n");
                notesText.append("Date: ").append(note.getTimestamp()).append("\n");
                notesText.append("Note: ").append(note.getNote()).append("\n");
                notesText.append("---\n\n");
            }
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Patient Notes");
        alert.setHeaderText("Notes for: " + selected.getFirstName() + " " + selected.getLastName());
        
        TextArea textArea = new TextArea(notesText.toString());
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setPrefRowCount(15);
        
        alert.getDialogPane().setContent(textArea);
        alert.showAndWait();
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
