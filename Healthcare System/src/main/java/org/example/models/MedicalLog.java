package org.example.models;

import java.time.LocalDateTime;

public class MedicalLog {
    private String id;
    private Integer patientID;
    private String action;
    private String details;
    private String performedBy;
    private LocalDateTime timestamp;

    public MedicalLog() {
        this.timestamp = LocalDateTime.now();
    }

    public MedicalLog(Integer patientID, String action, String details, String performedBy) {
        this.patientID = patientID;
        this.action = action;
        this.details = details;
        this.performedBy = performedBy;
        this.timestamp = LocalDateTime.now();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Integer getPatientID() { return patientID; }
    public void setPatientID(Integer patientID) { this.patientID = patientID; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public String getPerformedBy() { return performedBy; }
    public void setPerformedBy(String performedBy) { this.performedBy = performedBy; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
