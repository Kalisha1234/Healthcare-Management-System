package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.HealthcareApp;
import org.example.models.*;
import org.example.services.*;
import org.example.utils.ValidationException;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppointmentController {
    @FXML private TableView<AppointmentView> appointmentTable;
    @FXML private TableColumn<AppointmentView, Integer> colId;
    @FXML private TableColumn<AppointmentView, String> colPatientName;
    @FXML private TableColumn<AppointmentView, String> colDoctorName;
    @FXML private TableColumn<AppointmentView, String> colDate;
    @FXML private TableColumn<AppointmentView, String> colTime;
    @FXML private TableColumn<AppointmentView, String> colStatus;

    @FXML private ComboBox<PatientItem> cbPatient;
    @FXML private ComboBox<DoctorItem> cbDoctor;
    @FXML private DatePicker dpDate;
    @FXML private TextField txtTime;
    @FXML private ComboBox<String> cbStatus;

    private AppointmentService appointmentService;
    private PatientService patientService;
    private DoctorService doctorService;
    
    private Map<Integer, String> patientMap = new HashMap<>();
    private Map<Integer, String> doctorMap = new HashMap<>();

    @FXML
    public void initialize() {
        appointmentService = new AppointmentService(HealthcareApp.getConnection());
        patientService = new PatientService(HealthcareApp.getConnection());
        doctorService = new DoctorService(HealthcareApp.getConnection());

        colId.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        colPatientName.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        colDoctorName.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("appointmentDate"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("appointmentTime"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        cbStatus.setItems(FXCollections.observableArrayList("Scheduled", "Completed", "Cancelled"));

        loadPatients();
        loadDoctors();
        loadAppointments();
    }

    @FXML
    private void handleAdd() {
        try {
            Appointment appointment = new Appointment(
                cbPatient.getValue().getId(),
                cbDoctor.getValue().getId(),
                dpDate.getValue(),
                LocalTime.parse(txtTime.getText()),
                cbStatus.getValue()
            );

            appointmentService.scheduleAppointment(appointment);
            showAlert("Success", "Appointment scheduled successfully!", Alert.AlertType.INFORMATION);
            clearForm();
            loadAppointments();
        } catch (ValidationException e) {
            showAlert("Validation Error", e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Error", "Failed to schedule appointment: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleUpdate() {
        AppointmentView selected = appointmentTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Warning", "Please select an appointment to update", Alert.AlertType.WARNING);
            return;
        }

        try {
            Appointment appointment = new Appointment(
                cbPatient.getValue().getId(),
                cbDoctor.getValue().getId(),
                dpDate.getValue(),
                LocalTime.parse(txtTime.getText()),
                cbStatus.getValue()
            );
            appointment.setAppointmentID(selected.getAppointmentID());

            appointmentService.updateAppointment(appointment);
            showAlert("Success", "Appointment updated successfully!", Alert.AlertType.INFORMATION);
            clearForm();
            loadAppointments();
        } catch (ValidationException e) {
            showAlert("Validation Error", e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Error", "Failed to update appointment: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleCancel() {
        AppointmentView selected = appointmentTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Warning", "Please select an appointment to cancel", Alert.AlertType.WARNING);
            return;
        }

        try {
            appointmentService.cancelAppointment(selected.getAppointmentID());
            showAlert("Success", "Appointment cancelled successfully!", Alert.AlertType.INFORMATION);
            clearForm();
            loadAppointments();
        } catch (Exception e) {
            showAlert("Error", "Failed to cancel appointment: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleDelete() {
        AppointmentView selected = appointmentTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Warning", "Please select an appointment to delete", Alert.AlertType.WARNING);
            return;
        }

        try {
            appointmentService.deleteAppointment(selected.getAppointmentID());
            showAlert("Success", "Appointment deleted successfully!", Alert.AlertType.INFORMATION);
            clearForm();
            loadAppointments();
        } catch (Exception e) {
            showAlert("Error", "Failed to delete appointment: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleTableClick() {
        AppointmentView selected = appointmentTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            for (PatientItem item : cbPatient.getItems()) {
                if (item.getId().equals(selected.getPatientID())) {
                    cbPatient.setValue(item);
                    break;
                }
            }
            
            for (DoctorItem item : cbDoctor.getItems()) {
                if (item.getId().equals(selected.getDoctorID())) {
                    cbDoctor.setValue(item);
                    break;
                }
            }
            
            dpDate.setValue(selected.getAppointmentDate());
            txtTime.setText(selected.getAppointmentTime().toString());
            cbStatus.setValue(selected.getStatus());
        }
    }

    private void loadAppointments() {
        try {
            List<Appointment> appointments = appointmentService.getAllAppointments();
            List<AppointmentView> appointmentViews = appointments.stream()
                .map(a -> new AppointmentView(
                    a.getAppointmentID(),
                    a.getPatientID(),
                    patientMap.getOrDefault(a.getPatientID(), "Unknown"),
                    a.getDoctorID(),
                    doctorMap.getOrDefault(a.getDoctorID(), "Unknown"),
                    a.getAppointmentDate(),
                    a.getAppointmentTime(),
                    a.getStatus()
                ))
                .toList();
            
            appointmentTable.setItems(FXCollections.observableArrayList(appointmentViews));
        } catch (Exception e) {
            showAlert("Error", "Failed to load appointments: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void loadPatients() {
        try {
            List<Patient> patients = patientService.getAllPatients();
            
            // Build patient map
            patientMap.clear();
            for (Patient p : patients) {
                patientMap.put(p.getPatientID(), p.getFirstName() + " " + p.getLastName());
            }
            
            // Load combo box
            cbPatient.setItems(FXCollections.observableArrayList(
                patients.stream()
                    .map(p -> new PatientItem(p.getPatientID(), p.getFirstName(), p.getLastName()))
                    .toList()
            ));
        } catch (Exception e) {
            showAlert("Error", "Failed to load patients: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void loadDoctors() {
        try {
            List<Doctor> doctors = doctorService.getAllDoctors();
            
            // Build doctor map
            doctorMap.clear();
            for (Doctor d : doctors) {
                doctorMap.put(d.getDoctorID(), "Dr. " + d.getFirstName() + " " + d.getLastName());
            }
            
            // Load combo box
            cbDoctor.setItems(FXCollections.observableArrayList(
                doctors.stream()
                    .map(d -> new DoctorItem(d.getDoctorID(), d.getFirstName(), d.getLastName()))
                    .toList()
            ));
        } catch (Exception e) {
            showAlert("Error", "Failed to load doctors: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void clearForm() {
        cbPatient.setValue(null);
        cbDoctor.setValue(null);
        dpDate.setValue(null);
        txtTime.clear();
        cbStatus.setValue(null);
        appointmentTable.getSelectionModel().clearSelection();
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
