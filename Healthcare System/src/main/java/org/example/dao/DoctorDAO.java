package org.example.dao;

import org.example.models.Doctor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoctorDAO {
    private Connection conn;

    public DoctorDAO(Connection conn) {
        this.conn = conn;
    }

    public void create(Doctor doctor) throws SQLException {
        String sql = "INSERT INTO Dcotors (FirstName, LastName, DepartmentID, Phone, Email, HireDate) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, doctor.getFirstName());
            stmt.setString(2, doctor.getLastName());
            stmt.setInt(3, doctor.getDepartmentID());
            stmt.setString(4, doctor.getPhone());
            stmt.setString(5, doctor.getEmail());
            stmt.setDate(6, Date.valueOf(doctor.getHireDate()));
            stmt.executeUpdate();
            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                doctor.setDoctorID(rs.getInt(1));
            }
        }
    }

    public Doctor findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM Dcotors WHERE DoctorID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSet(rs);
            }
        }
        return null;
    }

    public List<Doctor> findAll() throws SQLException {
        List<Doctor> doctors = new ArrayList<>();
        String sql = "SELECT * FROM Dcotors";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                doctors.add(mapResultSet(rs));
            }
        }
        return doctors;
    }

    public void update(Doctor doctor) throws SQLException {
        String sql = "UPDATE Dcotors SET FirstName=?, LastName=?, DepartmentID=?, Phone=?, Email=?, HireDate=?, UpdatedAt=CURRENT_TIMESTAMP WHERE DoctorID=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, doctor.getFirstName());
            stmt.setString(2, doctor.getLastName());
            stmt.setInt(3, doctor.getDepartmentID());
            stmt.setString(4, doctor.getPhone());
            stmt.setString(5, doctor.getEmail());
            stmt.setDate(6, Date.valueOf(doctor.getHireDate()));
            stmt.setInt(7, doctor.getDoctorID());
            stmt.executeUpdate();
        }
    }

    public void delete(Integer id) throws SQLException {
        String sql = "DELETE FROM Dcotors WHERE DoctorID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    private Doctor mapResultSet(ResultSet rs) throws SQLException {
        Doctor doctor = new Doctor();
        doctor.setDoctorID(rs.getInt("DoctorID"));
        doctor.setFirstName(rs.getString("FirstName"));
        doctor.setLastName(rs.getString("LastName"));
        doctor.setDepartmentID(rs.getInt("DepartmentID"));
        doctor.setPhone(rs.getString("Phone"));
        doctor.setEmail(rs.getString("Email"));
        doctor.setHireDate(rs.getDate("HireDate").toLocalDate());
        doctor.setCreatedAt(rs.getTimestamp("CreatedAt").toLocalDateTime());
        doctor.setUpdatedAt(rs.getTimestamp("UpdatedAt").toLocalDateTime());
        return doctor;
    }
}
