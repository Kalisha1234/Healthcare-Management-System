package org.example.models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

public class Appointment {
    private Integer appointmentID;
    private Integer patientID;
    private Integer doctorID;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Appointment() {}

    public Appointment(Integer patientID, Integer doctorID, LocalDate appointmentDate, LocalTime appointmentTime, String status) {
        this.patientID = patientID;
        this.doctorID = doctorID;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.status = status;
    }

    public Integer getAppointmentID() { return appointmentID; }
    public void setAppointmentID(Integer appointmentID) { this.appointmentID = appointmentID; }
    public Integer getPatientID() { return patientID; }
    public void setPatientID(Integer patientID) { this.patientID = patientID; }
    public Integer getDoctorID() { return doctorID; }
    public void setDoctorID(Integer doctorID) { this.doctorID = doctorID; }
    public LocalDate getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(LocalDate appointmentDate) { this.appointmentDate = appointmentDate; }
    public LocalTime getAppointmentTime() { return appointmentTime; }
    public void setAppointmentTime(LocalTime appointmentTime) { this.appointmentTime = appointmentTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
