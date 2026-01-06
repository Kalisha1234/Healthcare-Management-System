package org.example.services;

import org.example.dao.AppointmentDAO;
import org.example.models.Appointment;
import org.example.utils.ValidationException;
import org.example.utils.Validator;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class AppointmentService {
    private AppointmentDAO appointmentDAO;

    public AppointmentService(Connection conn) {
        this.appointmentDAO = new AppointmentDAO(conn);
    }

    public void scheduleAppointment(Appointment appointment) throws SQLException, ValidationException {
        // Validate inputs
        Validator.validatePositiveInteger(appointment.getPatientID(), "Patient ID");
        Validator.validatePositiveInteger(appointment.getDoctorID(), "Doctor ID");
        Validator.validateDate(appointment.getAppointmentDate(), "Appointment Date");
        Validator.validateAppointmentStatus(appointment.getStatus());

        appointmentDAO.create(appointment);
    }

    public Appointment getAppointment(Integer id) throws SQLException {
        return appointmentDAO.findById(id);
    }

    public List<Appointment> getAllAppointments() throws SQLException {
        return appointmentDAO.findAll();
    }

    public void updateAppointment(Appointment appointment) throws SQLException, ValidationException {
        // Validate inputs
        Validator.validatePositiveInteger(appointment.getPatientID(), "Patient ID");
        Validator.validatePositiveInteger(appointment.getDoctorID(), "Doctor ID");
        Validator.validateDate(appointment.getAppointmentDate(), "Appointment Date");
        Validator.validateAppointmentStatus(appointment.getStatus());

        appointmentDAO.update(appointment);
    }

    public void cancelAppointment(Integer id) throws SQLException {
        Appointment appointment = appointmentDAO.findById(id);
        if (appointment != null) {
            appointment.setStatus("Cancelled");
            appointmentDAO.update(appointment);
        }
    }

    public void deleteAppointment(Integer id) throws SQLException {
        appointmentDAO.delete(id);
    }
}
