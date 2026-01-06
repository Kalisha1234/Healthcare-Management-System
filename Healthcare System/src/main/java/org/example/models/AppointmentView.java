package org.example.models;

import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentView {
    private Integer appointmentID;
    private Integer patientID;
    private String patientName;
    private Integer doctorID;
    private String doctorName;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String status;

    public AppointmentView(Integer appointmentID, Integer patientID, String patientName, 
                          Integer doctorID, String doctorName, LocalDate appointmentDate, 
                          LocalTime appointmentTime, String status) {
        this.appointmentID = appointmentID;
        this.patientID = patientID;
        this.patientName = patientName;
        this.doctorID = doctorID;
        this.doctorName = doctorName;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.status = status;
    }

    public Integer getAppointmentID() { return appointmentID; }
    public Integer getPatientID() { return patientID; }
    public String getPatientName() { return patientName; }
    public Integer getDoctorID() { return doctorID; }
    public String getDoctorName() { return doctorName; }
    public LocalDate getAppointmentDate() { return appointmentDate; }
    public LocalTime getAppointmentTime() { return appointmentTime; }
    public String getStatus() { return status; }
}
