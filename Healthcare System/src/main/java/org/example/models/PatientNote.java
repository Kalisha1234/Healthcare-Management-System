package org.example.models;

import java.time.LocalDateTime;

public class PatientNote {
    private String id;
    private Integer patientID;
    private Integer doctorID;
    private String note;
    private String type;
    private LocalDateTime timestamp;

    public PatientNote() {
        this.timestamp = LocalDateTime.now();
    }

    public PatientNote(Integer patientID, Integer doctorID, String note, String type) {
        this.patientID = patientID;
        this.doctorID = doctorID;
        this.note = note;
        this.type = type;
        this.timestamp = LocalDateTime.now();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Integer getPatientID() { return patientID; }
    public void setPatientID(Integer patientID) { this.patientID = patientID; }

    public Integer getDoctorID() { return doctorID; }
    public void setDoctorID(Integer doctorID) { this.doctorID = doctorID; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
