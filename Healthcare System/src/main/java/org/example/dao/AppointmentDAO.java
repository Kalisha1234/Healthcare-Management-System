package org.example.dao;

import org.example.models.Appointment;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {
    private Connection conn;

    public AppointmentDAO(Connection conn) {
        this.conn = conn;
    }

    public void create(Appointment appointment) throws SQLException {
        String sql = "INSERT INTO Appointments (PatientID, DoctorID, AppointmentDate, AppointmentTime, Status) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, appointment.getPatientID());
            stmt.setInt(2, appointment.getDoctorID());
            stmt.setDate(3, Date.valueOf(appointment.getAppointmentDate()));
            stmt.setTime(4, Time.valueOf(appointment.getAppointmentTime()));
            stmt.setString(5, appointment.getStatus());
            stmt.executeUpdate();
            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                appointment.setAppointmentID(rs.getInt(1));
            }
        }
    }

    public Appointment findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM Appointments WHERE AppointmentID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSet(rs);
            }
        }
        return null;
    }

    public List<Appointment> findAll() throws SQLException {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM Appointments";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                appointments.add(mapResultSet(rs));
            }
        }
        return appointments;
    }

    public void update(Appointment appointment) throws SQLException {
        String sql = "UPDATE Appointments SET PatientID=?, DoctorID=?, AppointmentDate=?, AppointmentTime=?, Status=?, UpdatedAt=CURRENT_TIMESTAMP WHERE AppointmentID=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, appointment.getPatientID());
            stmt.setInt(2, appointment.getDoctorID());
            stmt.setDate(3, Date.valueOf(appointment.getAppointmentDate()));
            stmt.setTime(4, Time.valueOf(appointment.getAppointmentTime()));
            stmt.setString(5, appointment.getStatus());
            stmt.setInt(6, appointment.getAppointmentID());
            stmt.executeUpdate();
        }
    }

    public void delete(Integer id) throws SQLException {
        String sql = "DELETE FROM Appointments WHERE AppointmentID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    private Appointment mapResultSet(ResultSet rs) throws SQLException {
        Appointment appointment = new Appointment();
        appointment.setAppointmentID(rs.getInt("AppointmentID"));
        appointment.setPatientID(rs.getInt("PatientID"));
        appointment.setDoctorID(rs.getInt("DoctorID"));
        appointment.setAppointmentDate(rs.getDate("AppointmentDate").toLocalDate());
        appointment.setAppointmentTime(rs.getTime("AppointmentTime").toLocalTime());
        appointment.setStatus(rs.getString("Status"));
        appointment.setCreatedAt(rs.getTimestamp("CreatedAt").toLocalDateTime());
        appointment.setUpdatedAt(rs.getTimestamp("UpdatedAt").toLocalDateTime());
        return appointment;
    }
}
