package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.example.HealthcareApp;
import org.example.dao.AppointmentDAO;
import org.example.dao.DoctorDAO;
import org.example.dao.PatientDAO;

public class DashboardController {
    @FXML
    private Label lblPatientCount;
    @FXML
    private Label lblDoctorCount;
    @FXML
    private Label lblAppointmentCount;

    @FXML
    public void initialize() {
        loadStatistics();
    }

    private void loadStatistics() {
        try {
            PatientDAO patientDAO = new PatientDAO(HealthcareApp.getConnection());
            DoctorDAO doctorDAO = new DoctorDAO(HealthcareApp.getConnection());
            AppointmentDAO appointmentDAO = new AppointmentDAO(HealthcareApp.getConnection());

            lblPatientCount.setText(String.valueOf(patientDAO.findAll().size()));
            lblDoctorCount.setText(String.valueOf(doctorDAO.findAll().size()));
            lblAppointmentCount.setText(String.valueOf(appointmentDAO.findAll().size()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
