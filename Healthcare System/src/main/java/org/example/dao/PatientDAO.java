package org.example.dao;

import org.example.models.Patient;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientDAO {
    private Connection conn;

    public PatientDAO(Connection conn) {
        this.conn = conn;
    }

    public void create(Patient patient) throws SQLException {
        String sql = "INSERT INTO Patient (FirstName, LastName, DOB, Gender, Email, Phone, Address) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, patient.getFirstName());
            stmt.setString(2, patient.getLastName());
            stmt.setDate(3, Date.valueOf(patient.getDob()));
            stmt.setString(4, patient.getGender());
            stmt.setString(5, patient.getEmail());
            stmt.setString(6, patient.getPhone());
            stmt.setString(7, patient.getAddress());
            stmt.executeUpdate();
            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                patient.setPatientID(rs.getInt(1));
            }
        }
    }

    public Patient findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM Patient WHERE PatientID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSet(rs);
            }
        }
        return null;
    }

    public List<Patient> findAll() throws SQLException {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM Patient";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                patients.add(mapResultSet(rs));
            }
        }
        return patients;
    }

    public void update(Patient patient) throws SQLException {
        String sql = "UPDATE Patient SET FirstName=?, LastName=?, DOB=?, Gender=?, Email=?, Phone=?, Address=?, UpdatedAt=CURRENT_TIMESTAMP WHERE PatientID=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, patient.getFirstName());
            stmt.setString(2, patient.getLastName());
            stmt.setDate(3, Date.valueOf(patient.getDob()));
            stmt.setString(4, patient.getGender());
            stmt.setString(5, patient.getEmail());
            stmt.setString(6, patient.getPhone());
            stmt.setString(7, patient.getAddress());
            stmt.setInt(8, patient.getPatientID());
            stmt.executeUpdate();
        }
    }

    public void delete(Integer id) throws SQLException {
        String sql = "DELETE FROM Patient WHERE PatientID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    private Patient mapResultSet(ResultSet rs) throws SQLException {
        Patient patient = new Patient();
        patient.setPatientID(rs.getInt("PatientID"));
        patient.setFirstName(rs.getString("FirstName"));
        patient.setLastName(rs.getString("LastName"));
        patient.setDob(rs.getDate("DOB").toLocalDate());
        patient.setGender(rs.getString("Gender"));
        patient.setEmail(rs.getString("Email"));
        patient.setPhone(rs.getString("Phone"));
        patient.setAddress(rs.getString("Address"));
        patient.setCreatedAt(rs.getTimestamp("CreatedAt").toLocalDateTime());
        patient.setUpdatedAt(rs.getTimestamp("UpdatedAt").toLocalDateTime());
        return patient;
    }
}
